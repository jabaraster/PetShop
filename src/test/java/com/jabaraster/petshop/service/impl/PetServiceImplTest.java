/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import java.util.Arrays;

import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.jabaraster.petshop.WebStarter;
import com.jabaraster.petshop.WebStarter.Mode;
import com.jabaraster.petshop.entity.EPetCategory;
import com.jabaraster.petshop.entity.EPet_;
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
    @Test
    public void test_fetch() {
        final EPetCategory category = new EPetCategory("犬"); //$NON-NLS-1$
        this.tool.getEntityManager().persist(category);
        this.tool.getSut().fetch(0, 10, Arrays.asList(category), EPet_.name.getName(), true);
    }

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }

}
