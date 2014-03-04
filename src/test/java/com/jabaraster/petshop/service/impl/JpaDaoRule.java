/**
 * 
 */
package com.jabaraster.petshop.service.impl;

import jabara.jpa.ThreadLocalEntityManagerFactoryHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.jabaraster.petshop.Environment;

/**
 * @param <S> テスト対象のサービスの型.
 * @author jabaraster
 */
public abstract class JpaDaoRule<S> implements TestRule {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager        entityManager;
    private S                    sut;

    /**
     * @see org.junit.rules.TestRule#apply(org.junit.runners.model.Statement, org.junit.runner.Description)
     */
    @Override
    public Statement apply(final Statement pBase, @SuppressWarnings("unused") final Description pDescription) {
        return new Statement() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void evaluate() throws Throwable {
                final Map<String, String> prop = new HashMap<>();
                //                prop.put("hibernate.dialect", H2Dialect.class.getName()); //$NON-NLS-1$
                final EntityManagerFactory original = Persistence.createEntityManagerFactory( //
                        Environment.getApplicationName() + "_WithDataSource", prop); //$NON-NLS-1$
                JpaDaoRule.this.entityManagerFactory = ThreadLocalEntityManagerFactoryHandler.wrap(original);
                JpaDaoRule.this.entityManager = JpaDaoRule.this.entityManagerFactory.createEntityManager();
                JpaDaoRule.this.sut = createService(JpaDaoRule.this.entityManagerFactory);

                JpaDaoRule.this.entityManager.getTransaction().begin();
                try {
                    pBase.evaluate();
                } finally {
                    try {
                        JpaDaoRule.this.entityManager.getTransaction().rollback();
                    } catch (final Throwable e) {
                        e.printStackTrace();
                    }
                    JpaDaoRule.this.entityManagerFactory.close();
                }

            }
        };
    }

    /**
     * @return entityManagerを返す.
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * @param pInterface -
     * @param <I> -
     * @return インターフェイス I を実装したダミーオブジェクトを返します.
     */
    @SuppressWarnings("unchecked")
    public static <I> I createDummy(final Class<I> pInterface) {
        return (I) Proxy.newProxyInstance(pInterface.getClassLoader(), new Class<?>[] { pInterface }, new InvocationHandler() {
            @SuppressWarnings("unused")
            @Override
            public Object invoke(final Object pProxy, final Method pMethod, final Object[] pArgs) throws Throwable {
                return null;
            }
        });
    }

    /**
     * @return sutを返す.
     */
    public S getSut() {
        return this.sut;
    }

    /**
     * @param pEntityManagerFactory -
     * @return -
     */
    protected abstract S createService(final EntityManagerFactory pEntityManagerFactory);

}
