/*
 * Unilan S.r.l. 
 * Copyright 2008
 */
package it.javalinux.testedby.testsupport.factories;

import it.javalinux.testedby.factories.ClassUnderTestInstanceFactory;

public abstract class NonInstantiableFactory implements ClassUnderTestInstanceFactory{
    

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.factories.ClassUnderTestInstanceFactory#createInstance(java.lang.Class)
     */
    public <T> T createInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return null;
    }
}