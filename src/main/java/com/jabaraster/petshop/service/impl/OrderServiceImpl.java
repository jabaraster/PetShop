/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jabaraster.petshop.entity.EOrder;
import com.jabaraster.petshop.entity.EOrder_;
import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.service.IOrderService;
import com.jabaraster.petshop.service.IUserService;

/**
 * @author jabaraster
 */
public class OrderServiceImpl extends JpaDaoBase implements IOrderService {
    private static final long  serialVersionUID = -3218822702730736755L;

    private final IUserService userService;

    /**
     * @param pEntityManagerFactory -
     * @param pUserService -
     */
    @Inject
    public OrderServiceImpl( //
            final EntityManagerFactory pEntityManagerFactory //
            , final IUserService pUserService //
    ) {
        super(pEntityManagerFactory);
        this.userService = pUserService;
    }

    /**
     * @see com.jabaraster.petshop.service.IOrderService#addOrder(long, com.jabaraster.petshop.entity.EPet)
     */
    @Override
    public void addOrder(final long pUserId, final EPet pPet) {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$

        final EUser user;
        try {
            user = this.userService.findById(pUserId);
        } catch (final NotFound e) {
            // ログインしているユーザが管理者によって削除された場合はここが実行される可能性がある.
            return;
        }

        try {
            find(user, pPet).incrementQuantity();
        } catch (final NotFound e) {
            final EOrder order = new EOrder(user, pPet);
            getEntityManager().persist(order);
        }
    }

    /**
     * @see com.jabaraster.petshop.service.IOrderService#findByUserId(long)
     */
    @Override
    public List<EOrder> findByUserId(final long pUserId) {
        try {
            final EUser user = this.userService.findById(pUserId);
            final EntityManager em = getEntityManager();
            final CriteriaBuilder builder = em.getCriteriaBuilder();
            final CriteriaQuery<EOrder> query = builder.createQuery(EOrder.class);
            final Root<EOrder> root = query.from(EOrder.class);

            query.where(builder.equal(root.get(EOrder_.user), user));

            return em.createQuery(query).getResultList();

        } catch (final NotFound e) {
            return Collections.emptyList();
        }
    }

    /**
     * @see com.jabaraster.petshop.service.IOrderService#removeOrder(com.jabaraster.petshop.entity.EOrder)
     */
    @Override
    public void removeOrder(final EOrder pOrder) {
        ArgUtil.checkNull(pOrder, "pOrder"); //$NON-NLS-1$

        if (!pOrder.isPersisted()) {
            return;
        }
        final EntityManager em = getEntityManager();
        em.remove(em.merge(pOrder));
    }

    private EOrder find(final EUser pUser, final EPet pPet) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EOrder> query = builder.createQuery(EOrder.class);
        final Root<EOrder> root = query.from(EOrder.class);

        query.where( //
                builder.equal(root.get(EOrder_.user), pUser) //
                , builder.equal(root.get(EOrder_.pet), pPet) //
        );

        return getSingleResult(em.createQuery(query));
    }
}
