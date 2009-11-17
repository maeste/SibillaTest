/*
 * Unilan S.r.l. 
 * Copyright 2008
 */
package it.javalinux.testedby.factories;

import it.javalinux.testedby.annotations.TestedByFactory;
import it.javalinux.testedby.metadata.Metadata;

@TestedByFactory(MyOwnMetadataFactory.class)
public class MyOwnMetadata implements Metadata {
    private final String foo;
    public MyOwnMetadata(String foo) {
        this.foo = foo;
    }
    /**
     * @return foo
     */
    public String getFoo() {
        return foo;
    }
}