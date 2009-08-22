/*
 * Stefano Maestri, javalinuxlabs.org Copyright 2008, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package it.javalinux.testedby.metadata.impl.immutable;

import it.javalinux.testedby.metadata.ClassUnderTestMetadata;
import it.javalinux.testedby.metadata.MethodUnderTestMetadata;
import it.javalinux.testedby.metadata.TestClassMetadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Immutable implementation of 
 * @author Stefano Maestri stefano.maestri@javalinux.it
 *
 */
public class ImmutableClassUnderTestMetadata implements ClassUnderTestMetadata {

    private final String className;

    private final Collection<TestClassMetadata> testClassesMetadatas = new LinkedList<TestClassMetadata>();
    
    private final Map<String, MethodUnderTestMetadata> methodsSpecificMetaDatas = new HashMap<String, MethodUnderTestMetadata>();
    
    /**
     * @param className
     * @param testClassesMetadatas
     * @param methodsSpecificMetaDatas
     */
    public ImmutableClassUnderTestMetadata(String className, Collection<TestClassMetadata> testClassesMetadatas, Map<String, MethodUnderTestMetadata> methodsSpecificMetaDatas) {
	super();
	this.className = className;
	this.testClassesMetadatas.addAll(testClassesMetadatas);
	this.methodsSpecificMetaDatas.putAll(methodsSpecificMetaDatas);
    }
    
    
    
    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata.ClassUnderTestMetadata#getClassUnderTestName()
     */
    public String getClassUnderTestName() {
	return className;
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata.ClassUnderTestMetadata#getMethodsSpecificMetaDatas()
     */
    public Map<String, MethodUnderTestMetadata> getMethodsSpecificMetaDatas() {
	HashMap<String, MethodUnderTestMetadata> map = new HashMap<String, MethodUnderTestMetadata>();
	map.putAll(methodsSpecificMetaDatas);
	return map;
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata.CodeUnderTestMetadata#getTestClassesMetadatas()
     */
    public Collection<TestClassMetadata> getTestClassesMetadatas() {
	LinkedList<TestClassMetadata> list = new LinkedList<TestClassMetadata>();
	list.addAll(testClassesMetadatas);
	return list;
    }

     
    
}
