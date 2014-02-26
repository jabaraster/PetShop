/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.jabaraster.petshop.WebStarter;
import com.jabaraster.petshop.WebStarter.Mode;

/**
 * @author jabaraster
 */
@RunWith(Enclosed.class)
public class UserServiceImplTest {

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }

    /**
     * @author jabaraster
     */
    public static class TableIsEmpty {
        /**
         * 
         */
        @Rule
        public final JpaDaoRule<UserServiceImpl> tool = new JpaDaoRule<UserServiceImpl>() {
                                                          @Override
                                                          protected UserServiceImpl createService(final EntityManagerFactory pEntityManagerFactory) {
                                                              return new UserServiceImpl(pEntityManagerFactory);
                                                          }
                                                      };

        // /**
        // *
        // */
        // @SuppressWarnings("boxing")
        // @Test
        // public void getAllの結果が0件() {
        // final int actual = this.tool.getSut().getAll(Sort.asc(EntityBase_.id.getName())).size();
        // assertThat(actual, is(0));
        // }
    }
}
