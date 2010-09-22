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
package it.javalinux.sibilla.runner.impl;

import static org.junit.matchers.JUnitMatchers.hasItem;

import static org.hamcrest.core.IsNot.not;

import static org.junit.matchers.JUnitMatchers.hasItems;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import it.javalinux.sibilla.metadata.ClassLinkMetadata;
import it.javalinux.sibilla.metadata.StatusMetadata;
import it.javalinux.sibilla.metadata.TestsMetadata;
import it.javalinux.sibilla.metadata.builder.annotations.AnnotationBasedMetadataBuilder;
import it.javalinux.sibilla.metadata.builder.instrumentation.InvocationTracker;
import it.javalinux.sibilla.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.sibilla.runner.impl.JunitTestRunner;
import it.javalinux.sibilla.testsupport.ClassExtendingAbstractClass;
import it.javalinux.sibilla.testsupport.TestClassOne;
import it.javalinux.sibilla.testsupport.TestClassThree;
import it.javalinux.sibilla.testsupport.TestClassTwo;
import it.javalinux.sibilla.testsupport.interfaces.AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations;
import it.javalinux.sibilla.testsupport.interfaces.InterfaceUnderTestOne;
import it.javalinux.sibilla.testsupport.interfaces.TestClassOnInterfaceOne;
import it.javalinux.sibilla.testsupport.interfaces.TestClassOnInterfaceTwo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author stefano.maestri@javalinux.it
 * 
 */
public class JunitTestRunnerTest {

    private final static StatusMetadata status = new StatusMetadata().setFromAnnotation(true).setValid(true).setJustCreated(true).setOnAbstract(false);

    private final static StatusMetadata statusInstrumentation = new StatusMetadata().setFromAnnotation(false).setValid(true).setJustCreated(true).setOnAbstract(false);
    
    private final static ClassLinkMetadata CLASS_UNDERTEST_ONE_METADATA = new ClassLinkMetadata(status, "it.javalinux.sibilla.testsupport.ClassUnderTestThreeAnnotationOnMethod");

    private final static ClassLinkMetadata CLASS_UNDERTEST_TWO_METADATA = new ClassLinkMetadata(status, "it.javalinux.sibilla.testsupport.ClassUnderTestThreeAnnotationOnClass");

    private final static ClassLinkMetadata CLASS_UNDERTEST_INSTRUMENTATION_METADATA = new ClassLinkMetadata(statusInstrumentation, "it.javalinux.sibilla.testsupport.ClassUnderTestTwoAnnotationOnClass");

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
    
    @Test
    public void shouldReturnTrueIfAllTestPasses() {
	JunitTestRunner runner = new JunitTestRunner();
	
	boolean result = runner.runTest(TestClassOne.class.getCanonicalName(), "testMethodTwo", CLASS_UNDERTEST_ONE_METADATA, CLASS_UNDERTEST_TWO_METADATA);
	assertThat(result, is(true));
    }
    
    @Test
    public void shouldReturnFalseIfAtLeasetOneTestDoesntPass() {
	JunitTestRunner runner = new JunitTestRunner();
	
	boolean result = runner.runTest(TestClassThree.class.getCanonicalName(), "testMethodOne", CLASS_UNDERTEST_ONE_METADATA, CLASS_UNDERTEST_TWO_METADATA);
	assertThat(result, is(false));    
    }
    
    @Test
    public void shouldReturnTrueIfSingleTestMethodPasses() {
	JunitTestRunner runner = new JunitTestRunner();
	
	boolean result = runner.runTest(TestClassOne.class.getCanonicalName(), "testMethodTwo");
	assertThat(result, is(true));
    }
    
    @Test
    public void shouldReturnFalseIfSingleTestMethodDoesntPass() {
	JunitTestRunner runner = new JunitTestRunner();
	
	boolean result = runner.runTest(TestClassThree.class.getCanonicalName(), "testMethodOne");
	assertThat(result, is(false));    
    }
    
    @Test
    public void fileterClassLinkShouldReturnOnlyFromAnnotation() throws Exception {
	JunitTestRunner runner = new JunitTestRunner();
	assertThat(runner.filterOnlyFromAnnotation(CLASS_UNDERTEST_INSTRUMENTATION_METADATA, CLASS_UNDERTEST_TWO_METADATA, CLASS_UNDERTEST_ONE_METADATA), hasItems(CLASS_UNDERTEST_ONE_METADATA, CLASS_UNDERTEST_TWO_METADATA));
	assertThat(runner.filterOnlyFromAnnotation(CLASS_UNDERTEST_INSTRUMENTATION_METADATA, CLASS_UNDERTEST_TWO_METADATA, CLASS_UNDERTEST_ONE_METADATA), not((hasItem(CLASS_UNDERTEST_INSTRUMENTATION_METADATA))));
	
    }

}
