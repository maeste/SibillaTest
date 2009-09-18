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
 * Immutable implementation of {@link ClassUnderTestMetadata}.
 * 
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public class ImmutableClassUnderTestMetadata extends ClassUnderTestMetadata {

    private static final long serialVersionUID = 1L;

    private final String className;

    private final Collection<TestClassMetadata> testClassesMetadata = new LinkedList<TestClassMetadata>();

    private final Map<Integer, MethodUnderTestMetadata> methodsSpecificMetadata = new HashMap<Integer, MethodUnderTestMetadata>();

    /**
     * @param className
     * @param testClassesMetadata
     * @param methodsSpecificMetadata
     */
    public ImmutableClassUnderTestMetadata(String className, Collection<? extends TestClassMetadata> testClassesMetadata, Map<Integer, MethodUnderTestMetadata> methodsSpecificMetadata) {
	super();
	this.className = className;
	this.testClassesMetadata.addAll(testClassesMetadata);
	this.methodsSpecificMetadata.putAll(methodsSpecificMetadata);
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.ClassUnderTestMetadata#getClassUnderTestName()
     */
    @Override
    public String getClassUnderTestName() {
	return className;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.ClassUnderTestMetadata#getMethodsSpecificMetadata()
     */
    @Override
    public Map<Integer, MethodUnderTestMetadata> getMethodsSpecificMetadata() {
	HashMap<Integer, MethodUnderTestMetadata> map = new HashMap<Integer, MethodUnderTestMetadata>();
	map.putAll(methodsSpecificMetadata);
	return map;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.CodeUnderTestMetadata#getTestClassesMetadata()
     */
    public Collection<TestClassMetadata> getTestClassesMetadata() {
	LinkedList<TestClassMetadata> list = new LinkedList<TestClassMetadata>();
	list.addAll(testClassesMetadata);
	return list;
    }

}
