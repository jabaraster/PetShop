/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jabaraster.petshop.entity.ECart;
import com.jabaraster.petshop.entity.ECart_;
import com.jabaraster.petshop.entity.EOrder;
import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.model.LoginUser;
import com.jabaraster.petshop.service.ICartService;
import com.jabaraster.petshop.service.IUserService;

/**
 * @author jabaraster
 */
public class CartServiceImpl extends JpaDaoBase implements ICartService {
    private static final long  serialVersionUID = -3218822702730736755L;

    private final IUserService userService;

    /**
     * @param pEntityManagerFactory -
     * @param pUserService -
     */
    @Inject
    public CartServiceImpl( //
            final EntityManagerFactory pEntityManagerFactory //
            , final IUserService pUserService //
    ) {
        super(pEntityManagerFactory);
        this.userService = pUserService;
    }

    /**
     * @see com.jabaraster.petshop.service.ICartService#addOrder(com.jabaraster.petshop.model.LoginUser, com.jabaraster.petshop.entity.EPet)
     */
    @Override
    public void addOrder(final LoginUser pUser, final EPet pPet) {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$

        final EUser user;
        try {
            user = this.userService.findById(pUser.getId());
        } catch (final NotFound e) {
            // ログインしているユーザが管理者によって削除された場合はここが実行される可能性がある.
            return;
        }

        final ECart cart = getUserCart(user);
        try {
            final EOrder order = cart.findOrderByPet(pPet);
            order.incrementQuantity();
        } catch (final NotFound e) {
            final EOrder order = new EOrder(pPet);
            getEntityManager().persist(order);
            cart.addOrder(order);
        }
    }

    /**
     * @see com.jabaraster.petshop.service.ICartService#findByUserId(long)
     */
    @Override
    public ECart findByUserId(final long pUserId) {
        try {
            final EUser user = this.userService.findById(pUserId);
            return getUserCart(user);
        } catch (final NotFound e) {
            // ログインしているユーザが管理者によって削除された場合はここが実行される可能性がある.
            throw new IllegalStateException();
        }
    }

    private ECart getUserCart(final EUser pUser) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<ECart> query = builder.createQuery(ECart.class);
        final Root<ECart> root = query.from(ECart.class);

        query.where(builder.equal(root.get(ECart_.user), pUser));

        try {
            return getSingleResult(em.createQuery(query));
        } catch (final NotFound e) {
            final ECart cart = new ECart(pUser);
            em.persist(cart);
            return cart;
        }
    }
}
