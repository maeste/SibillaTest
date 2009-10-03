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
package it.javalinux.testedby.runner;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import it.javalinux.testedby.metadata.ClassLinkMetadata;
import it.javalinux.testedby.metadata.MethodLinkMetadata;
import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.builder.annotations.AnnotationBasedMetadataBuilder;
import it.javalinux.testedby.metadata.impl.ImmutableMethodMetadata;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public abstract class AbstractUnitRunner implements TestRunner {

    public TestsMetadata run(List<Class<?>> changedClassesUnderTest, List<Class<?>> changedTestClasses, TestsMetadata metadata) throws Exception {

	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	metadata = builder.build(changedClassesUnderTest, metadata.getAllTestClasses(), changedTestClasses);
	Set<MethodLinkMetadata> methodLinkToRun = new HashSet<MethodLinkMetadata>();
	for (MethodLinkMetadata methodMetadata : metadata.getAllTestMethods()) {
	    if (methodMetadata.getStatus().isJustCreated()) {
		methodLinkToRun.add(methodMetadata);
	    }
	}
	for (Class<?> classUnderTest : changedClassesUnderTest) {
	    for (MethodLinkMetadata methodMetadata : metadata.getTestMethodsForRecursive(classUnderTest, true)) {
		methodLinkToRun.add(methodMetadata);
	    }
	}

	for (Class<?> testClass : changedTestClasses) {
	    for (Method method : testClass.getMethods()) {
		if (method.getAnnotation(Test.class) != null) {
		    methodLinkToRun.add(new MethodLinkMetadata(new StatusMetadata(true, true, false, false), testClass.getCanonicalName(), new ImmutableMethodMetadata(method)));
		}
	    }
	}

	for (MethodLinkMetadata methodLinkMetadata : methodLinkToRun) {
	    boolean success = runTestedByElement(metadata.getClassesTestedBy(methodLinkMetadata.getClazz(), methodLinkMetadata.getMethod()), methodLinkMetadata.getClazz(), methodLinkMetadata.getMethod().getName());
	    // TODO update status inside metadata
	}

	return metadata;

    }

    /**
     * @param classesUnderTest
     * @param testClass
     * @param methodName
     * @return true if test pass, false if it fails or got errors
     * @throws Exception
     * @throws ClassNotFoundException
     */

    public abstract boolean runTestedByElement(List<ClassLinkMetadata> classesUnderTest, String testClass, String methodName) throws Exception, ClassNotFoundException;

}
