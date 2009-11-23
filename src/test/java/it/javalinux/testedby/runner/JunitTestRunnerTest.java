/*
 * Unilan S.r.l. 
 * Copyright 2008
 */
package it.javalinux.testedby.runner;

import static org.mockito.Mockito.never;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import it.javalinux.testedby.metadata.ClassLinkMetadata;
import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.builder.annotations.AnnotationBasedMetadataBuilder;
import it.javalinux.testedby.metadata.builder.instrumentation.InvocationTracker;
import it.javalinux.testedby.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.testedby.runner.impl.JunitTestRunner;
import it.javalinux.testedby.testsupport.ClassExtendingAbstractClass;
import it.javalinux.testedby.testsupport.TestClassOne;
import it.javalinux.testedby.testsupport.TestClassTwo;
import it.javalinux.testedby.testsupport.interfaces.AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations;
import it.javalinux.testedby.testsupport.interfaces.InterfaceUnderTestOne;
import it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceOne;
import it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceTwo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author oracle
 * 
 */
public class JunitTestRunnerTest {

    private final static StatusMetadata status = new StatusMetadata().setFromAnnotation(true).setValid(true).setJustCreated(true).setOnAbstract(false);

    private final static ClassLinkMetadata CLASS_UNDERTEST_ONE_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnMethod");

    private final static ClassLinkMetadata CLASS_UNDERTEST_TWO_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnClass");

    private static InvocationTracker tracker;
    
    @BeforeClass
    public static void setUpTracker() {
	tracker = spy(InvocationTracker.getInstance());
	InvocationTracker.setInstance(tracker);

    }
   
    @Test
    public void shouldNotKeepTrackAndRunTestForAbstractClassesAndInterface() throws Exception {
	JunitTestRunner runner = new JunitTestRunner();
	
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class, TestClassOnInterfaceOne.class, TestClassOnInterfaceTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassExtendingAbstractClass.class);
	classesUnderTest.add(AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations.class);
	classesUnderTest.add(InterfaceUnderTestOne.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses, true);

	List<ClassLinkMetadata> list = metadatas.getClassesTestedBy(TestClassOnInterfaceOne.class.getCanonicalName(), new ImmutableMethodMetadata(TestClassOnInterfaceOne.class.getMethod("testMethodOne")));
	runner.runTest(TestClassOne.class.getCanonicalName(), "testMethodOne", list.toArray(new ClassLinkMetadata[list.size()]));

	verify(tracker,never()).setCurrentClassUnderTest(AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations.class.getCanonicalName());
	verify(tracker,never()).setCurrentClassUnderTest(InterfaceUnderTestOne.class.getCanonicalName());
	verify(tracker, times(1)).setCurrentClassUnderTest(ClassExtendingAbstractClass.class.getCanonicalName());

    }
    
    @Test
    public void shouldKeepTrackAndRunTestForAllClassesUnderTest() throws Exception {
	JunitTestRunner runner = new JunitTestRunner();
	
	runner.runTest(TestClassOne.class.getCanonicalName(), "testMethodOne", CLASS_UNDERTEST_ONE_METADATA, CLASS_UNDERTEST_TWO_METADATA);

	verify(tracker, times(1)).setCurrentClassUnderTest(CLASS_UNDERTEST_ONE_METADATA.getClazz());
	verify(tracker, times(1)).setCurrentClassUnderTest(CLASS_UNDERTEST_TWO_METADATA.getClazz());

    }

}
