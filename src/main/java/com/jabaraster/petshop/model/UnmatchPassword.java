/**
 * 
 */
package com.jabaraster.petshop.model;

/**
 * @author jabaraster
 */
public class UnmatchPassword extends Exception {
    private static final long  serialVersionUID = -3085571180136796852L;

    /**
     * 
     */
    public static final Global GLOBAL           = new Global();

    /**
     * @author jabaraster
     */
    public static final class Global extends UnmatchPassword {
        private static final long serialVersionUID = -2034348529687181095L;

        private Global() {
            //
        }
    }
}
