/*
 * Unilan S.r.l. 
 * Copyright 2008
 */
package it.javalinux.testedby.factories;

import it.javalinux.testedby.annotations.TestedByFactory;
import it.javalinux.testedby.metadata.Metadata;

@TestedByFactory(NonInstantiableFactory.class)
public class TestSupportClass implements Metadata {
    
}