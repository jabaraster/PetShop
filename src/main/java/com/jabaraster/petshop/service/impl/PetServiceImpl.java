/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.general.io.DataOperation;
import jabara.general.io.DataOperation.Operation;
import jabara.general.io.IReadableData;
import jabara.jpa.JpaDaoBase;
import jabara.jpa.entity.EntityBase_;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.entity.EPetImageData;
import com.jabaraster.petshop.entity.EPetImageData_;
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
     * @param pEntityManagerFactory -
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
     * @see com.jabaraster.petshop.service.IPetService#delete(com.jabaraster.petshop.entity.EPet)
     */
    @Override
    public void delete(final EPet pPet) {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        if (!pPet.isPersisted()) {
            return;
        }

        final EntityManager em = getEntityManager();

        em.remove(em.merge(pPet));

        // TODO カートから注文を消さなければいけない.

        em.createNamedQuery("deleteOrderByPet").setParameter("pet", pPet).executeUpdate(); //$NON-NLS-1$ //$NON-NLS-2$
        em.createNamedQuery("deletePetImageDataByPet").setParameter("pet", pPet).executeUpdate(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#fetch(long, long, java.util.List, java.lang.String, boolean)
     */
    @Override
    public List<EPet> fetch( //
            final long pFirst //
            , final long pCount //
            , final List<EPetCategory> pCategories //
            , final String pSortProperty //
            , final boolean pAscending) {

        final int first = convertToInt(pFirst, "pFirst"); //$NON-NLS-1$
        final int count = convertToInt(pCount, "pCount"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EPet> query = builder.createQuery(EPet.class);
        final Root<EPet> root = query.from(EPet.class);

        root.fetch(EPet_.category);

        final Sort sort = pAscending ? Sort.asc(pSortProperty) : Sort.desc(pSortProperty);
        query.orderBy(convertOrder(sort, builder, root));

        if (pCategories != null && !pCategories.isEmpty()) {
            query.where( //
            root.get(EPet_.category).in(pCategories) //
            );
        }
        return em.createQuery(query).setFirstResult(first).setMaxResults(count).getResultList();
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#findById(long)
     */
    @Override
    public EPet findById(final long pId) throws NotFound {
        return findByIdCore(EPet.class, pId);
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#findImageDataByPet(com.jabaraster.petshop.entity.EPet)
     */
    @Override
    public EPetImageData findImageDataByPet(final EPet pPet) throws NotFound {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        if (!pPet.isPersisted()) {
            throw NotFound.GLOBAL;
        }
        return findImageDataByPetCore(pPet);
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#findImageDataByPetImageDataId(long)
     */
    @Override
    public EPetImageData findImageDataByPetImageDataId(final long pPetImageDataId) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EPetImageData> query = builder.createQuery(EPetImageData.class);
        final Root<EPetImageData> root = query.from(EPetImageData.class);

        query.where(builder.equal(root.get(EntityBase_.id), Long.valueOf(pPetImageDataId)));

        return getSingleResult(em.createQuery(query));
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#findImageDataHashByImageDataId(long)
     */
    @Override
    public String findImageDataHashByImageDataId(final long pPetImageDataId) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        final Root<EPetImageData> root = query.from(EPetImageData.class);

        query.where(builder.equal(root.get(EntityBase_.id), Long.valueOf(pPetImageDataId)));
        query.select(root.get(EPetImageData_.hash));

        return getSingleResult(em.createQuery(query));
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#register(com.jabaraster.petshop.entity.EPet, jabara.general.io.DataOperation)
     */
    @Override
    public void register(final EPet pPet, final DataOperation pImageDataOperation) {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        ArgUtil.checkNull(pImageDataOperation, "pImageDataOperation"); //$NON-NLS-1$

        registerCore(pPet, pImageDataOperation);
    }

    /**
     * @see com.jabaraster.petshop.service.IPetService#register(com.jabaraster.petshop.entity.EPet, java.lang.String, jabara.general.io.DataOperation)
     */
    @Override
    public void register(final EPet pPet, final String pNewCategoryName, final DataOperation pImageDataOperation) {
        ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pNewCategoryName, "pNewCategoryName"); //$NON-NLS-1$
        ArgUtil.checkNull(pImageDataOperation, "pImageDataOperation"); //$NON-NLS-1$

        try {
            final EPetCategory category = this.petCategoryService.findByName(pNewCategoryName);
            pPet.setCategory(category);
        } catch (final NotFound e) {
            final EPetCategory newCategory = new EPetCategory(pNewCategoryName);
            this.petCategoryService.insert(newCategory);
            pPet.setCategory(newCategory);
        }
        registerCore(pPet, pImageDataOperation);
    }

    private void deletePetImage(final EPet pPet) {
        final EntityManager em = getEntityManager();
        try {
            em.remove(findImageDataByPetCore(pPet));
        } catch (final NotFound e) {
            // 処理なし
        }
    }

    private EPetImageData findImageDataByPetCore(final EPet pPet) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EPetImageData> query = builder.createQuery(EPetImageData.class);
        final Root<EPetImageData> root = query.from(EPetImageData.class);

        query.where(builder.equal(root.get(EPetImageData_.pet), pPet));

        return getSingleResult(em.createQuery(query));
    }

    private void insertCore(final EPet pPet, final DataOperation pImageDataOperation) {
        final EntityManager em = getEntityManager();
        em.persist(pPet);
        if (pImageDataOperation.getOperation() == Operation.UPDATE) {
            em.persist(new EPetImageData(pPet, pImageDataOperation.getData()));
        }
    }

    private void registerCore(final EPet pPet, final DataOperation pImageDataOperation) {
        if (pPet.isPersisted()) {
            updateCore(pPet, pImageDataOperation);
        } else {
            insertCore(pPet, pImageDataOperation);
        }
    }

    private void updateCore(final EPet pPet, final DataOperation pImageDataOperation) {
        switch (pImageDataOperation.getOperation()) {
        case UPDATE:
            updatePetImage(pPet, pImageDataOperation.getData());
            break;
        case DELETE:
            deletePetImage(pPet);
            break;
        case NOOP:
        default:
            break;
        }

        final EPet inDb = getEntityManager().merge(pPet);
        inDb.setCategory(pPet.getCategory());
        inDb.setName(pPet.getName());
        inDb.setUnitPrice(pPet.getUnitPrice());
    }

    private void updatePetImage(final EPet pPet, final IReadableData pData) {
        deletePetImage(pPet);
        getEntityManager().persist(new EPetImageData(pPet, pData));
    }
}
