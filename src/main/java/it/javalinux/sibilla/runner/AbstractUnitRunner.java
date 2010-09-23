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
package it.javalinux.sibilla.runner;

import it.javalinux.sibilla.instrumentation.InstrumentationTestRunner;
import it.javalinux.sibilla.metadata.ClassLinkMetadata;
import it.javalinux.sibilla.metadata.MethodLinkMetadata;
import it.javalinux.sibilla.metadata.MethodMetadata;
import it.javalinux.sibilla.metadata.StatusMetadata;
import it.javalinux.sibilla.metadata.TestsMetadata;
import it.javalinux.sibilla.metadata.builder.annotations.AnnotationBasedMetadataBuilder;
import it.javalinux.sibilla.metadata.builder.instrumentation.InstrumentationBasedMetadataBuilder;
import it.javalinux.sibilla.metadata.builder.instrumentation.InvocationTracker;
import it.javalinux.sibilla.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.sibilla.metadata.serializer.MetadataSerializer;
import it.javalinux.sibilla.metadata.serializer.impl.SimpleMetadataSerializer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

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

    public TestsMetadata run(List<Class<?>> changedClassesUnderTest, List<Class<?>> changedTestClasses) throws Exception {
	return run(changedClassesUnderTest, changedTestClasses, new SimpleMetadataSerializer());
    }

    public TestsMetadata run(List<Class<?>> changedClassesUnderTest, List<Class<?>> changedTestClasses, MetadataSerializer serializer) throws Exception {
	TestsMetadata metadata = run(changedClassesUnderTest, changedTestClasses, serializer.deserialize());
	serializer.serialize(metadata);
	return metadata;

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see it.javalinux.sibilla.runner.TestRunner#run(java.util.List,
     *      java.util.List, it.javalinux.sibilla.metadata.TestsMetadata)
     */
    public TestsMetadata run(List<Class<?>> changedClassesUnderTest, List<Class<?>> changedTestClasses, TestsMetadata metadata) throws Exception {
	AnnotationBasedMetadataBuilder annotationBuilder = new AnnotationBasedMetadataBuilder();
	InstrumentationBasedMetadataBuilder instrumentationBuilder = new InstrumentationBasedMetadataBuilder();

	if (metadata != null) {
	    metadata.merge(annotationBuilder.build(changedClassesUnderTest, metadata.getAllTestClasses(), changedTestClasses));
		
	} else {
	    metadata = annotationBuilder.build(changedClassesUnderTest, null, changedTestClasses);
		
	}
	Set<MethodLinkMetadata> methodLinkToRun = new HashSet<MethodLinkMetadata>();

	// run all new tests changed except ones collected by annotation
	metadata = runNonNewTestsNotPointedByAnnotations(changedTestClasses, metadata, instrumentationBuilder);
	instrumentationBuilder.reset();
	
	// Now collect the tests to run...

	// we need to run all the new test methods collected by annotations and
	// those that failed previously
	for (MethodLinkMetadata methodMetadata : metadata.getAllTestMethods()) {
	    if (methodMetadata.getStatus().isJustCreated() || (methodMetadata.getStatus().isFailedOnLastRun() )) {
	    methodLinkToRun.add(methodMetadata);
	    }
	}

	// ... as well as all the methods testing changed classes
	if (changedClassesUnderTest != null) {
	    for (Class<?> classUnderTest : changedClassesUnderTest) {
		for (MethodLinkMetadata methodMetadata : metadata.getTestMethodsForRecursive(classUnderTest, true)) {
		    methodLinkToRun.add(methodMetadata);
		}
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

	runMethodLinkMetadatas(metadata, instrumentationBuilder, methodLinkToRun);
	metadata.merge(instrumentationBuilder.getMetadata());
	return metadata;
    }

    /**
     * @param changedTestClasses
     * @param metadata
     * @param instrumentationBuilder
     * @return changed metadata
     * @throws Exception
     */
    protected TestsMetadata runNonNewTestsNotPointedByAnnotations(List<Class<?>> changedTestClasses, TestsMetadata metadata, InstrumentationBasedMetadataBuilder instrumentationBuilder) throws Exception {
	for (Class<?> testClass : changedTestClasses) {
	    for (Method m : getTestMethods(testClass)) {
		if (metadata.getClassesTestedBy(testClass, m) != null && metadata.getClassesTestedBy(testClass, m).size() == 0) {
		    runSingleTestUsingInstrumentationBuilder(instrumentationBuilder, testClass.getCanonicalName(), m.getName(),false);
		}
	    }
	}
	metadata.merge(instrumentationBuilder.getMetadata());
	return metadata;
    }

    /**
     * @param metadata
     * @param instrumentationBuilder
     * @param methodLinkToRun
     * @throws Exception
     */
    protected void runMethodLinkMetadatas(TestsMetadata metadata, InstrumentationBasedMetadataBuilder instrumentationBuilder, Set<MethodLinkMetadata> methodLinkToRun) throws Exception {
	for (MethodLinkMetadata methodLinkMetadata : methodLinkToRun) {
	    String clazz = methodLinkMetadata.getClazz();
	    MethodMetadata method = methodLinkMetadata.getMethod();
	    List<ClassLinkMetadata> list = metadata.getClassesTestedBy(clazz, method);
	    runSingleTestUsingInstrumentationBuilder(instrumentationBuilder, clazz, method.getName(),false, list.toArray(new ClassLinkMetadata[list.size()]));
	}
    }

    protected boolean runSingleTestUsingInstrumentationBuilder(InstrumentationBasedMetadataBuilder builder, String testClassName, String methodName, boolean justCreated, ClassLinkMetadata... classesUnderTest) throws Exception {
	InvocationTracker.cleanUp();
	InvocationTracker tracker = InvocationTracker.getInstance();
	tracker.setTestClass(testClassName);
	tracker.setTestMethod(methodName);
	tracker.setSkipTestClass(true);
	boolean result = runTest(testClassName, methodName, classesUnderTest);
	StatusMetadata status = new StatusMetadata();
	status.setFromInstrumentation(true);
	status.setValid(true);
	status.setJustCreated(justCreated);
	status.setPassedOnLastRun(result);
	builder.performBuildStep(testClassName, methodName, null, status);
	return result;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see it.javalinux.sibilla.instrumentation.InstrumentationTestRunner#run(java.util.List)
     */
    public TestsMetadata run(List<Class<?>> tests) throws Exception {
	InstrumentationBasedMetadataBuilder builder = new InstrumentationBasedMetadataBuilder();
	for (Class<?> test : tests) {
	    for (Method method : getTestMethods(test)) {
		runSingleTestUsingInstrumentationBuilder(builder, test.getName(), method.getName(), true);
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
