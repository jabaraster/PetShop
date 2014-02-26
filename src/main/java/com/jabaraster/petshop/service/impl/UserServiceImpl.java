package com.jabaraster.petshop.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;
import jabara.jpa.entity.EntityBase_;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.jabaraster.petshop.entity.ELoginPassword;
import com.jabaraster.petshop.entity.ELoginPassword_;
import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.entity.EUser_;
import com.jabaraster.petshop.model.Duplicate;
import com.jabaraster.petshop.model.LoginUser;
import com.jabaraster.petshop.model.UnmatchPassword;
import com.jabaraster.petshop.service.IUserService;

/**
 * 
 */
public class UserServiceImpl extends JpaDaoBase implements IUserService {
    private static final long serialVersionUID = -1168069588289854262L;

    /**
     * @param pEntityManagerFactory DBアクセス用オブジェクト.
     */
    @Inject
    public UserServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#countAll()
     */
    @Override
    public long countAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<EUser> root = query.from(EUser.class);

        query.select(builder.count(root));

        try {
            return getSingleResult(em.createQuery(query)).longValue();
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#delete(com.jabaraster.petshop.entity.EUser)
     */
    @Override
    public void delete(final EUser pUser) {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final EUser c = em.merge(pUser);
        em.remove(findPasswordByUser(pUser));
        em.remove(c);
        em.flush();
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#enableDelete(com.jabaraster.petshop.model.LoginUser, com.jabaraster.petshop.entity.EUser)
     */
    @Override
    public boolean enableDelete(final LoginUser pLoginUser, final EUser pDeleteTargetUser) {
        ArgUtil.checkNull(pLoginUser, "pLoginUser"); //$NON-NLS-1$
        ArgUtil.checkNull(pDeleteTargetUser, "pDeleteTargetUser"); //$NON-NLS-1$

        // 自身は削除不可.
        if (pLoginUser.equal(pDeleteTargetUser)) {
            return false;
        }

        // 管理者でないユーザが管理者を削除することは出来ない.
        if (!pLoginUser.isAdministrator() && pDeleteTargetUser.isAdministrator()) {
            return false;
        }

        return true;
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#findById(long)
     */
    @Override
    public EUser findById(final long pId) throws NotFound {
        return findByIdCore(EUser.class, pId);
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#get(long, long, jabara.general.Sort)
     */
    @Override
    public List<EUser> get(final long pFirst, final long pCount, final Sort pSort) {
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$

        if (pFirst > Integer.MAX_VALUE) {
            throw new IllegalStateException();
        }

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EUser> query = builder.createQuery(EUser.class);
        final Root<EUser> root = query.from(EUser.class);

        query.orderBy(convertOrder(pSort, builder, root));

        return em.createQuery(query) //
                .setFirstResult(convertToInt(pFirst, "pFirst")) // //$NON-NLS-1$
                .setMaxResults(convertToInt(pCount, "pCount")) // //$NON-NLS-1$
                .getResultList();
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#insert(com.jabaraster.petshop.entity.EUser, java.lang.String)
     */
    @Override
    public void insert(final EUser pUser, final String pPassword) throws Duplicate {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$
        if (pUser.isPersisted()) {
            throw new IllegalStateException("既に永続化されているエンティティは処理できません."); //$NON-NLS-1$
        }
        insertCore(pUser, pPassword);
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#insertAdministratorIfNotExists()
     */
    @Override
    public void insertAdministratorIfNotExists() {
        if (existsAministrator()) {
            return;
        }

        final EntityManager em = getEntityManager();

        final EUser member = new EUser();
        member.setAdministrator(true);
        member.setUserId(EUser.DEFAULT_ADMINISTRATOR_USER_ID);
        em.persist(member);

        final ELoginPassword password = new ELoginPassword();
        password.setPassword(EUser.DEFAULT_ADMINISTRATOR_PASSWORD);
        password.setUser(member);
        em.persist(password);
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#update(com.jabaraster.petshop.entity.EUser)
     */
    @Override
    public void update(final EUser pUser) throws Duplicate {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$
        if (!pUser.isPersisted()) {
            throw new IllegalStateException("永続化していないエンティティは処理出来ません."); //$NON-NLS-1$
        }
        updateCore(pUser);
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#updatePassword(com.jabaraster.petshop.entity.EUser, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePassword(final EUser pUser, final String pCurrentPassword, final String pNewPassword) throws UnmatchPassword {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$

        final ELoginPassword password = findPasswordByUser(pUser);
        if (!password.equal(pCurrentPassword)) {
            throw UnmatchPassword.GLOBAL;
        }
        updatePasswordCore(pUser, pNewPassword);
    }

    private void checkCodeDuplicate(final EUser pUser, final DuplicateCheckMode pMode) throws Duplicate {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        final Root<EUser> root = query.from(EUser.class);

        final Subquery<String> sub = query.subquery(String.class);
        final Root<EUser> subRoot = sub.from(EUser.class);
        sub.select(getDummyExpression(builder));
        sub.where(new WhereBuilder() //
                .add(builder.equal(root.get(EntityBase_.id), subRoot.get(EntityBase_.id))) //
                .add(builder.equal(subRoot.get(EUser_.userId), pUser.getUserId())) //
                .addIf(pMode == DuplicateCheckMode.UPDATE, builder.notEqual(subRoot.get(EntityBase_.id), pUser.getId())) //
                .build() //
        );

        query.where(builder.exists(sub));
        query.select(getDummyExpression(builder));

        try {
            getSingleResult(em.createQuery(query));
            throw new Duplicate();
        } catch (final NotFound e) {
            // 重複なし
        }
    }

    private boolean existsAministrator() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        query.from(EUser.class);

        final String DUMMY = "X"; //$NON-NLS-1$
        query.select(builder.literal(DUMMY).alias(DUMMY));

        return !em.createQuery(query).setMaxResults(1).getResultList().isEmpty();
    }

    private ELoginPassword findPasswordByUser(final EUser pUser) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<ELoginPassword> query = builder.createQuery(ELoginPassword.class);
        final Root<ELoginPassword> root = query.from(ELoginPassword.class);
        query.where(builder.equal(root.get(ELoginPassword_.user), pUser));
        try {
            return getSingleResult(em.createQuery(query));
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private void insertCore(final EUser pUser, final String pPassword) throws Duplicate {
        checkCodeDuplicate(pUser, DuplicateCheckMode.INSERT);
        final EntityManager em = getEntityManager();
        em.persist(pUser);
        em.persist(new ELoginPassword(pUser, pPassword));
    }

    private void updateCore(final EUser pUser) throws Duplicate {
        checkCodeDuplicate(pUser, DuplicateCheckMode.UPDATE);
        final EntityManager em = getEntityManager();
        if (!em.contains(pUser)) {
            final EUser target = em.merge(pUser);
            target.setUserId(pUser.getUserId());
        }
    }

    private void updatePasswordCore(final EUser pUser, final String pPassword) {
        final ELoginPassword password = findPasswordByUser(pUser);
        password.setPassword(pPassword);
    }

    private enum DuplicateCheckMode {
        INSERT, UPDATE;
    }

}
