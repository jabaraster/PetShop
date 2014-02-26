/**
 * 
 */
package com.jabaraster.petshop.model;

/**
 * @author jabaraster
 */
public class Duplicate extends Exception {
    private static final long  serialVersionUID = 5881572755974445410L;

    /**
     * 
     */
    public static final Global GLOBAL           = new Global();

    /**
     * @author jabaraster
     */
    public static final class Global extends Duplicate {
        private static final long serialVersionUID = -1622891649754625414L;

        private Global() {
            //
        }
    }
}
