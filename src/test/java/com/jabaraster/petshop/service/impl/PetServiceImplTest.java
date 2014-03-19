/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import jabara.general.Empty;
import jabara.general.io.InputStreamReadableData;
import jabara.jpa.entity.IEntity;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.jabaraster.petshop.WebStarter;
import com.jabaraster.petshop.WebStarter.Mode;
import com.jabaraster.petshop.entity.EOrder;
import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.entity.EPetImageData;
import com.jabaraster.petshop.entity.EPet_;
import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.service.IPetCategoryService;

/**
 * @author jabaraster
 */
public class PetServiceImplTest {

    /**
     * 
     */
    @Rule
    public JpaDaoRule<PetServiceImpl> tool = new JpaDaoRule<PetServiceImpl>() {
                                               @Override
                                               protected PetServiceImpl createService(final EntityManagerFactory pEntityManagerFactory) {
                                                   return new PetServiceImpl(pEntityManagerFactory, createDummy(IPetCategoryService.class));
                                               }
                                           };

    /**
     * 
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Test
    public void test_delete() {
        final EOrder order0 = insertOrder("category0", "pet0", 100, "user0");
        insertOrder("category1", "pet1", 200, "user1");

        assertThat(count(EOrder.class), is(2L));
        assertThat(count(EPetImageData.class), is(2L));

        this.tool.getSut().delete(order0.getPet());

        assertThat(count(EOrder.class), is(1L));
        assertThat(count(EPetImageData.class), is(1L));
    }

    /**
     * 
     */
    @SuppressWarnings("boxing")
    @Test
    public void test_fetch() {
        final EPetCategory category = new EPetCategory("犬"); //$NON-NLS-1$
        this.tool.getEntityManager().persist(category);
        final List<EPet> result = this.tool.getSut().fetch(0, 10, Arrays.asList(category), EPet_.name.getName(), true);
        assertThat(result.size(), is(0));
    }

    private long count(final Class<? extends IEntity> pEntityType) {
        final EntityManager em = this.tool.getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<? extends IEntity> root = query.from(pEntityType);
        query.select(builder.count(root));
        return em.createQuery(query).getSingleResult().longValue();
    }

    @SuppressWarnings("nls")
    private EOrder insertOrder(final String pCategoryName, final String pPetName, final int pUnitPrice //
            , final String pUserId) {

        final EntityManager em = this.tool.getEntityManager();
        final EPetCategory category = new EPetCategory(pCategoryName);
        final EPet pet = new EPet(pPetName, category, pUnitPrice);
        final EUser user = new EUser(pUserId, false);
        final EPetImageData data = new EPetImageData(pet, new InputStreamReadableData("data", "image/png", 0, new ByteArrayInputStream(
                Empty.BYTE_ARRAY)));

        final EOrder order = new EOrder(user, pet);

        em.persist(category);
        em.persist(pet);
        em.persist(user);
        em.persist(data);
        em.persist(order);

        em.flush();
        em.clear();

        return order;
    }

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }

}
