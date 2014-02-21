/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.entity.EPetCategory_;
import com.jabaraster.petshop.entity.EPet_;
import com.jabaraster.petshop.service.IPetCategoryService;
import com.jabaraster.petshop.service.IPetService;

/**
 * @author jabaraster
 */
public class PetServiceImpl extends JpaDaoBase implements IPetService {
    private static final long         serialVersionUID = -7494555889492182446L;

    private final IPetCategoryService petCategoryService;

    /**
     * @param pEntityManagerFactory
     * @param pPetCategoryService -
     */
    @Inject
    public PetServiceImpl(final EntityManagerFactory pEntityManagerFactory, final IPetCategoryService pPetCategoryService) {
        super(pEntityManagerFactory);
        this.petCategoryService = ArgUtil.checkNull(pPetCategoryService, "pPetCategoryService"); //$NON-NLS-1$
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#countAll()
     */
    @Override
    public long countAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<EPet> root = query.from(EPet.class);

        query.select(builder.count(root));

        try {
            return getSingleResult(em.createQuery(query)).longValue();
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#fetch(int, int, java.lang.String, boolean)
     */
    @Override
    public List<EPet> fetch(final int pFirst, final int pCount, final String pSortProperty, final boolean pAscending) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EPet> query = builder.createQuery(EPet.class);
        final Root<EPet> root = query.from(EPet.class);

        root.fetch(EPet_.category);

        final Sort sort = pAscending ? Sort.asc(pSortProperty) : Sort.desc(pSortProperty);
        if ("category.name".equals(pSortProperty)) { //$NON-NLS-1$
            final Path<String> path = root.get(EPet_.category).get(EPetCategory_.name);
            query.orderBy(pAscending ? builder.asc(path) : builder.desc(path));
        } else {
            query.orderBy(convertOrder(sort, builder, root));
        }

        return em.createQuery(query).setFirstResult(pFirst).setMaxResults(pCount).getResultList();
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#findById(long)
     */
    @Override
    public EPet findById(final long pId) throws NotFound {
        return findByIdCore(EPet.class, pId);
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#register(com.jabaraster.petshop.entity.EPet)
     */
    @Override
    public void register(final EPet pPet) {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        if (pPet.isPersisted()) {
            throw new IllegalArgumentException();
        }
        getEntityManager().persist(pPet);
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#register(com.jabaraster.petshop.entity.EPet, java.lang.String)
     */
    @Override
    public void register(final EPet pPet, final String pNewCategoryName) {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pNewCategoryName, "pNewCategoryName"); //$NON-NLS-1$
        try {
            final EPetCategory category = this.petCategoryService.findByName(pNewCategoryName);
            pPet.setCategory(category);
            getEntityManager().persist(pPet);
        } catch (final NotFound e) {
            final EPetCategory newCategory = new EPetCategory(pNewCategoryName);
            this.petCategoryService.insert(newCategory);
            pPet.setCategory(newCategory);
            getEntityManager().persist(pPet);
        }
    }

}
