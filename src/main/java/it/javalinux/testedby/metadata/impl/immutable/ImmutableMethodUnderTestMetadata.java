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
import java.util.LinkedList;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public class ImmutableMethodUnderTestMetadata extends MethodUnderTestMetadata {

    private final String methodUnderTestName;

    private final Collection<TestClassMetadata> testClassesMetadatas = new LinkedList<TestClassMetadata>();

    private final MethodLineNumber methodLineNumber;

    private final ClassUnderTestMetadata classUnderTestMetadata;

    /**
     * @param methodUnderTestName
     * @param classUnderTestMetadata
     * @param testClassesMetadatas
     * @param startLine
     * @param endLine
     */
    public ImmutableMethodUnderTestMetadata(String methodUnderTestName, ClassUnderTestMetadata classUnderTestMetadata, Collection<? extends TestClassMetadata> testClassesMetadatas, int startLine, int endLine) {
	this.methodUnderTestName = methodUnderTestName;
	this.classUnderTestMetadata = classUnderTestMetadata;
	this.testClassesMetadatas.addAll(testClassesMetadatas);
	this.methodLineNumber = new MethodLineNumber(startLine, endLine);
    }

    /**
     * @param methodUnderTestName
     * @param classUnderTestMetadata
     * @param testClassesMetadatas
     */
    public ImmutableMethodUnderTestMetadata(String methodUnderTestName, ClassUnderTestMetadata classUnderTestMetadata, Collection<? extends TestClassMetadata> testClassesMetadatas) {
	this.methodUnderTestName = methodUnderTestName;
	this.classUnderTestMetadata = classUnderTestMetadata;
	this.testClassesMetadatas.addAll(testClassesMetadatas);
	this.methodLineNumber = null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.MethodUnderTestMetadata#getLineNumbers()
     */
    @Override
    public MethodLineNumber getLineNumbers() {
	return methodLineNumber;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.MethodUnderTestMetadata#getMethodUnderTestName()
     */
    @Override
    public String getMethodUnderTestName() {
	return methodUnderTestName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.CodeUnderTestMetadata#getTestClassesMetadatas()
     */
    public Collection<TestClassMetadata> getTestClassesMetadatas() {
	return testClassesMetadatas;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.MethodUnderTestMetadata#getClassUnderTestMetadata()
     */
    @Override
    public ClassUnderTestMetadata getClassUnderTestMetadata() {
	return classUnderTestMetadata;
    }

}
