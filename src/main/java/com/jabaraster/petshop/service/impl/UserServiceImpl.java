package com.jabaraster.petshop.service.impl;

import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.jabaraster.petshop.entity.ELoginPassword;
import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.service.IUserService;

/**
 * 
 */
public class UserServiceImpl extends JpaDaoBase implements IUserService {

    /**
     * @param pEntityManagerFactory DBアクセス用オブジェクト.
     */
    @Inject
    public UserServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see com.jabaraster.petshop.service.IUserService#findById(long)
     */
    @Override
    public EUser findById(final long pId) throws NotFound {
        return findByIdCore(EUser.class, pId);
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

    private boolean existsAministrator() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        query.from(EUser.class);

        final String DUMMY = "X"; //$NON-NLS-1$
        query.select(builder.literal(DUMMY).alias(DUMMY));

        return !em.createQuery(query).setMaxResults(1).getResultList().isEmpty();
    }

}
