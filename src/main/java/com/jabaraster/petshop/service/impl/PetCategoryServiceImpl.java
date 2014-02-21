/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.entity.EPetCategory_;
import com.jabaraster.petshop.service.IPetCategoryService;

/**
 * @author jabaraster
 */
public class PetCategoryServiceImpl extends JpaDaoBase implements IPetCategoryService {
    private static final long serialVersionUID = -7229082780414807053L;

    /**
     * @param pEntityManagerFactory
     */
    @Inject
    public PetCategoryServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see com.jabaraster.petshop.service.IPetCategoryService#findByName(java.lang.String)
     */
    @Override
    public EPetCategory findByName(final String pName) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EPetCategory> query = builder.createQuery(EPetCategory.class);
        final Root<EPetCategory> root = query.from(EPetCategory.class);
        query.where(builder.equal(root.get(EPetCategory_.name), pName));
        return getSingleResult(em.createQuery(query));
    }

    /**
     * @see com.jabaraster.petshop.service.IPetCategoryService#getAll()
     */
    @Override
    public List<EPetCategory> getAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EPetCategory> query = builder.createQuery(EPetCategory.class);
        final Root<EPetCategory> root = query.from(EPetCategory.class);

        query.orderBy(builder.asc(root.get(EPetCategory_.name)));

        return em.createQuery(query).getResultList();
    }

    /**
     * @see com.jabaraster.petshop.service.IPetCategoryService#insert(com.jabaraster.petshop.entity.EPetCategory)
     */
    @Override
    public void insert(final EPetCategory pCategory) {
        ArgUtil.checkNull(pCategory, "pCategory"); //$NON-NLS-1$
        if (pCategory.isPersisted()) {
            throw new IllegalArgumentException();
        }
        getEntityManager().persist(pCategory);
    }

}
