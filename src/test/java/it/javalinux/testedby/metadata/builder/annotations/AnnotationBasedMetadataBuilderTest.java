package it.javalinux.testedby.metadata.builder.annotations;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import it.javalinux.testedby.metadata.ClassLinkMetadata;
import it.javalinux.testedby.metadata.MethodLinkMetadata;
import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.testedby.testsupport.ClassExtendingAbstractClass;
import it.javalinux.testedby.testsupport.ClassImplementingInterfaceUnderTestWithItsOwnAnnotations;
import it.javalinux.testedby.testsupport.ClassUnderTestAnnotationListOnClass;
import it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationAndListOnClass;
import it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationAndListOnMethods;
import it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnClass;
import it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnMethod;
import it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnWrongMethod;
import it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationWithWrongClassName;
import it.javalinux.testedby.testsupport.ClassUnderTestWithAllAnnotations;
import it.javalinux.testedby.testsupport.ClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods;
import it.javalinux.testedby.testsupport.TestClassOne;
import it.javalinux.testedby.testsupport.TestClassTwo;
import it.javalinux.testedby.testsupport.interfaces.AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations;
import it.javalinux.testedby.testsupport.interfaces.InterfaceUnderTestOne;
import it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceOne;
import it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceTwo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnnotationBasedMetadataBuilderTest {

    private final static StatusMetadata status = new StatusMetadata().setFromAnnotation(true).setValid(true).setJustCreated(true);

    private final static ClassLinkMetadata TEST_CLASS_ONE_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.TestClassOne");

    private final static ClassLinkMetadata TEST_CLASS_TWO_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.TestClassTwo");

    private final static MethodLinkMetadata TEST_METHOD_ONE_IN_CLASS_ONE = new MethodLinkMetadata(status, TEST_CLASS_ONE_METADATA.getClazz(), new ImmutableMethodMetadata(TEST_CLASS_ONE_METADATA.getClazz(), "testMethodOne", null));

    private final static MethodLinkMetadata TEST_METHOD_TWO_IN_CLASS_ONE = new MethodLinkMetadata(status, TEST_CLASS_ONE_METADATA.getClazz(), new ImmutableMethodMetadata(TEST_CLASS_ONE_METADATA.getClazz(), "testMethodTwo", null));

    private final static MethodLinkMetadata TEST_METHOD_ONE_IN_CLASS_TWO = new MethodLinkMetadata(status, TEST_CLASS_TWO_METADATA.getClazz(), new ImmutableMethodMetadata(TEST_CLASS_TWO_METADATA.getClazz(), "testMethodOne", null));

    private final static MethodLinkMetadata TEST_METHOD_TWO_IN_CLASS_TWO = new MethodLinkMetadata(status, TEST_CLASS_TWO_METADATA.getClazz(), new ImmutableMethodMetadata(TEST_CLASS_TWO_METADATA.getClazz(), "testMethodTwo", null));

    private final static ClassLinkMetadata TEST_CLASS_ONE_ON_INTERFACE_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceOne");

    private final static ClassLinkMetadata TEST_CLASS_TWO_ON_INTERFACE_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceTwo");

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void buildShouldRUnOnClassUnderTestAnnotationListOnClass() throws Exception {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestAnnotationListOnClass.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.getTestClassesFor(ClassUnderTestAnnotationListOnClass.class, false), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestAnnotationListOnClass.class, false).size(), is(4));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestAnnotationListOnClass.class, false), hasItems(equalTo(TEST_METHOD_ONE_IN_CLASS_ONE), equalTo(TEST_METHOD_TWO_IN_CLASS_ONE), equalTo(TEST_METHOD_ONE_IN_CLASS_TWO), equalTo(TEST_METHOD_TWO_IN_CLASS_TWO)));

    }

    @Test
    public void buildShouldRunOnClassUnderTestOneAnnotationAndListOnClass() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationAndListOnClass.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationAndListOnClass.class, false), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnClass.class, false).size(), is(4));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnClass.class, false), hasItems(equalTo(TEST_METHOD_ONE_IN_CLASS_ONE), equalTo(TEST_METHOD_TWO_IN_CLASS_ONE), equalTo(TEST_METHOD_ONE_IN_CLASS_TWO), equalTo(TEST_METHOD_TWO_IN_CLASS_TWO)));

    }

    @Test
    public void shouldRunClassUnderTestOneAnnotationAndListOnMethods() throws Exception {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationAndListOnMethods.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	Method methodOne = ClassUnderTestOneAnnotationAndListOnMethods.class.getMethod("methodOne");
	Method methodTwo = ClassUnderTestOneAnnotationAndListOnMethods.class.getMethod("methodTwo");

	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationAndListOnMethods.class, false).size(), is(0));
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationAndListOnMethods.class, true).size(), is(2));
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationAndListOnMethods.class, true), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));

	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnMethods.class, methodOne).size(), is(1));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnMethods.class, methodOne), hasItems(equalTo(TEST_METHOD_ONE_IN_CLASS_ONE)));

	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnMethods.class, methodTwo).size(), is(3));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnMethods.class, methodTwo), hasItems(equalTo(TEST_METHOD_TWO_IN_CLASS_ONE), equalTo(TEST_METHOD_ONE_IN_CLASS_TWO), equalTo(TEST_METHOD_TWO_IN_CLASS_TWO)));

	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnMethods.class, false).size(), is(0));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnMethods.class, true).size(), is(4));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnMethods.class, true), hasItems(equalTo(TEST_METHOD_ONE_IN_CLASS_ONE), equalTo(TEST_METHOD_TWO_IN_CLASS_ONE), equalTo(TEST_METHOD_ONE_IN_CLASS_TWO), equalTo(TEST_METHOD_TWO_IN_CLASS_TWO)));

    }

    @Test
    public void buildShouldRunOnClassUnderTestOneAnnotationOnClass() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationOnClass.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationOnClass.class, false).size(), is(1));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationOnClass.class, false), hasItems(equalTo(TEST_METHOD_ONE_IN_CLASS_ONE)));

    }

    @Test
    public void buildShouldRunClassUnderTestOneAnnotationOnMethod() throws Exception {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationOnMethod.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationOnMethod.class, false).size(), is(0));
	Method methodOne = ClassUnderTestOneAnnotationOnMethod.class.getMethod("methodOne");
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationOnMethod.class, methodOne), hasItems(equalTo(TEST_CLASS_ONE_METADATA)));

	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationOnMethod.class, true).size(), is(1));
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationOnMethod.class, true), hasItems(equalTo(TEST_CLASS_ONE_METADATA)));

    }

    @Test
    public void buildShouldRunClassUnderTestOneAnnotationOnWrongMethod() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationOnWrongMethod.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	// no metadata directly on class
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationOnWrongMethod.class, true).size(), is(1));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationOnWrongMethod.class, true).size(), is(1));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationOnWrongMethod.class, true).get(0).getStatus().isValid(), is(false));

    }

    @Test
    public void buildShouldRunClassUnderTestOneAnnotationWithWrongClassName() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationWithWrongClassName.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	// no metadata directly on class
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationWithWrongClassName.class, false).size(), is(1));
	// metadatas from methods
	ClassLinkMetadata testClassWrong = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.TestClassWrong");
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationWithWrongClassName.class, false), hasItems(equalTo(testClassWrong)));
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationWithWrongClassName.class, false).iterator().next().getStatus().isValid(), is(false));

    }

    @Test
    public void buildShouldRunOnClassUnderTestWithAllAnnotations() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestWithAllAnnotations.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.getTestClassesFor(ClassUnderTestWithAllAnnotations.class, true).size(), is(2));
	assertThat(metadatas.getTestClassesFor(ClassUnderTestWithAllAnnotations.class, true), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestWithAllAnnotations.class, true), hasItems(equalTo(TEST_METHOD_ONE_IN_CLASS_ONE), equalTo(TEST_METHOD_TWO_IN_CLASS_ONE), equalTo(TEST_METHOD_ONE_IN_CLASS_TWO), equalTo(TEST_METHOD_TWO_IN_CLASS_TWO)));

    }

    @Test
    public void buildShouldRunOnClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses, true);
	assertThat(metadatas.getTestClassesFor(ClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods.class, true).size(), is(1));
	assertThat(metadatas.getTestClassesFor(ClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods.class, true), hasItems(equalTo(TEST_CLASS_TWO_METADATA)));

    }

    @Test
    public void buildShouldRunOnClassImplementingInterfaceUnderTestWithItsOwnAnnotations() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class, TestClassOnInterfaceOne.class, TestClassOnInterfaceTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassImplementingInterfaceUnderTestWithItsOwnAnnotations.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses, true);
	assertThat(metadatas.getTestClassesFor(ClassImplementingInterfaceUnderTestWithItsOwnAnnotations.class, true).size(), is(2));
	assertThat(metadatas.getTestClassesFor(ClassImplementingInterfaceUnderTestWithItsOwnAnnotations.class, true), hasItems(equalTo(TEST_CLASS_TWO_METADATA), equalTo(TEST_CLASS_TWO_ON_INTERFACE_METADATA)));
	assertThat(metadatas.getTestMethodsFor(ClassImplementingInterfaceUnderTestWithItsOwnAnnotations.class, true).size(), is(4));

    }

    @Test
    public void buildShouldRunOnClassExtendingAbstractClass() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class, TestClassOnInterfaceOne.class, TestClassOnInterfaceTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassExtendingAbstractClass.class);
	classesUnderTest.add(AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations.class);
	classesUnderTest.add(InterfaceUnderTestOne.class);	
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses, true);
	assertThat(metadatas.getTestClassesFor(ClassExtendingAbstractClass.class, true).size(), is(2));
	assertThat(metadatas.getTestClassesFor(ClassExtendingAbstractClass.class, true), hasItems(equalTo(TEST_CLASS_ONE_ON_INTERFACE_METADATA), equalTo(TEST_CLASS_TWO_ON_INTERFACE_METADATA)));
	assertThat(metadatas.getTestMethodsFor(ClassExtendingAbstractClass.class, true).size(), is(4));
	assertThat(metadatas.getTestClassesFor(AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations.class, true).size(), is(2));
	assertThat(metadatas.getTestClassesFor(InterfaceUnderTestOne.class, true).size(), is(1));
    }
    
    @Test
    public void buildShouldRunOnClassExtendingAbstractClassSettingCorrectStatuses() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class, TestClassOnInterfaceOne.class, TestClassOnInterfaceTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassExtendingAbstractClass.class);
	classesUnderTest.add(AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations.class);
	classesUnderTest.add(InterfaceUnderTestOne.class);	
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses, true);
	assertThat(metadatas.getTestMethodsFor(ClassExtendingAbstractClass.class, true).size(), is(4));
	for (ClassLinkMetadata classLink : metadatas.getTestClassesFor(ClassExtendingAbstractClass.class, true)) {
	    assertThat(classLink.getStatus().isOnAbstract(), is(false));
	}
	assertThat(metadatas.getTestClassesFor(AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations.class, true).size(), is(2));
	for (ClassLinkMetadata classLink : metadatas.getTestClassesFor(AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations.class, true)) {
	    assertThat(classLink.getStatus().isOnAbstract(), is(true));
	}
	assertThat(metadatas.getTestClassesFor(InterfaceUnderTestOne.class, true).size(), is(1));
	for (ClassLinkMetadata classLink : metadatas.getTestClassesFor(InterfaceUnderTestOne.class, true)) {
	    assertThat(classLink.getStatus().isOnAbstract(), is(true));
	}
	
    }
}
