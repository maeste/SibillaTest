/*
 * Unilan S.r.l. 
 * Copyright 2008
 */
package it.javalinux.testedby.factories;

public class MyOwnMetadataFactory implements ClassUnderTestInstanceFactory{
    @SuppressWarnings("unused")
    public MyOwnMetadataFactory() {
       
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.factories.ClassUnderTestInstanceFactory#createInstance(java.lang.Class)
     */
    public <T> T createInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return (T) new MyOwnMetadata("foo");
    }
}