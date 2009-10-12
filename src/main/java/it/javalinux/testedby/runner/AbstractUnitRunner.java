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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import it.javalinux.testedby.instrumentation.InstrumentationTestRunner;
import it.javalinux.testedby.metadata.ClassLinkMetadata;
import it.javalinux.testedby.metadata.MethodLinkMetadata;
import it.javalinux.testedby.metadata.MethodMetadata;
import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.builder.annotations.AnnotationBasedMetadataBuilder;
import it.javalinux.testedby.metadata.builder.instrumentation.InstrumentationBasedMetadataBuilder;
import it.javalinux.testedby.metadata.builder.instrumentation.InvocationTracker;
import it.javalinux.testedby.metadata.impl.ImmutableMethodMetadata;

/**
 * An abstract unit test runner that provides basic implementation for both
 * TestRunner and InstrumentationRunner, while leaving test framework specific
 * implementation details to extending classes.
 * 
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * @author alessio.soldano@javalinux.it
 * 
 */
public abstract class AbstractUnitRunner implements TestRunner, InstrumentationTestRunner {

    /**
     * 
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.runner.TestRunner#run(java.util.List,
     *      java.util.List, it.javalinux.testedby.metadata.TestsMetadata)
     */
    public TestsMetadata run(List<Class<?>> changedClassesUnderTest, List<Class<?>> changedTestClasses, TestsMetadata metadata) throws Exception {
	// TODO issue 17
	// TODO it have to consider previous failed tests
	// TODO it have to enrich previous metadata with new one (merging of
	// metadata needed)
	// TODO it have to run instrumented tests to collect new metadata
	// TODO it have to run all new tests classes even if they arent
	// connected by annotation to class under tests to collect new metadata
	// via instrumentation
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	metadata = builder.build(changedClassesUnderTest, metadata.getAllTestClasses(), changedTestClasses);
	Set<MethodLinkMetadata> methodLinkToRun = new HashSet<MethodLinkMetadata>();

	// we need to run all the new test methods
	for (MethodLinkMetadata methodMetadata : metadata.getAllTestMethods()) {
	    if (methodMetadata.getStatus().isJustCreated()) {
		methodLinkToRun.add(methodMetadata);
	    }
	}

	// ... as well as all the methods testing changed classes
	for (Class<?> classUnderTest : changedClassesUnderTest) {
	    for (MethodLinkMetadata methodMetadata : metadata.getTestMethodsForRecursive(classUnderTest, true)) {
		methodLinkToRun.add(methodMetadata);
	    }
	}

	// ... as well as all the changed test classes' methods
	for (Class<?> testClass : changedTestClasses) {
	    for (Method method : getTestMethods(testClass)) {
		if (method.getAnnotation(Test.class) != null) {
		    methodLinkToRun.add(new MethodLinkMetadata(new StatusMetadata(true, true, false, false), testClass.getCanonicalName(), new ImmutableMethodMetadata(method)));
		}
	    }
	}

	for (MethodLinkMetadata methodLinkMetadata : methodLinkToRun) {
	    String clazz = methodLinkMetadata.getClazz();
	    MethodMetadata method = methodLinkMetadata.getMethod();
	    List<ClassLinkMetadata> list = metadata.getClassesTestedBy(clazz, method);
	    boolean success = runTest(clazz, method.getName(), list.toArray(new ClassLinkMetadata[list.size()]));// TODO
	    // update
	    // status
	    // inside
	    // metadata
	}
	return metadata;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.instrumentation.InstrumentationTestRunner#run(java.util.List)
     */
    public TestsMetadata run(List<Class<?>> tests) throws Exception {
	InstrumentationBasedMetadataBuilder builder = new InstrumentationBasedMetadataBuilder();
	for (Class<?> test : tests) {
	    for (Method method : getTestMethods(test)) {
		InvocationTracker.cleanUp();
		InvocationTracker tracker = InvocationTracker.getInstance();
		tracker.setTestClass(test.getName());
		tracker.setTestMethod(method.getName());
		tracker.setSkipTestClass(true);
		runTest(test.getName(), method.getName());
		StatusMetadata status = new StatusMetadata();
		status.setFromInstrumentation(true);
		status.setValid(true);
		// TODO further set the status, perhaps also according to the
		// test result?
		builder.performBuildStep(test, method, status);
	    }
	}
	return builder.getMetadata();
    }

    /**
     * @param testClass
     * @param methodName
     * @param classesUnderTest
     * @return true if test pass, false if it fails or got errors
     * @throws Exception
     * @throws ClassNotFoundException
     */
    public abstract boolean runTest(String testClass, String methodName, ClassLinkMetadata... classesUnderTest) throws Exception, ClassNotFoundException;

    /**
     * Return the test methods of the given test class
     * 
     * @param testClass
     *            The test class
     * @return The test methods of the provided test class
     */
    protected abstract Collection<Method> getTestMethods(Class<?> testClass);

}
