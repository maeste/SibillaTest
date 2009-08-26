package it.javalinux.testedby.metadata.builder.annotations;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

import it.javalinux.testedby.metadata.ClassUnderTestMetadata;
import it.javalinux.testedby.metadata.TestClassMetadata;
import it.javalinux.testedby.metadata.TestMethodMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestClassMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestMethodMetadata;
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
import it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceOne;
import it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceTwo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnnotationBasedMetadataBuilderTest {

    private static AnnotationBasedMetadataBuilder builder;

    private final static TestClassMetadata TEST_CLASS_ONE_METADATA = new ImmutableTestClassMetadata(true, "it.javalinux.testedby.testsupport.TestClassOne", "");

    private final static TestClassMetadata TEST_CLASS_TWO_METADATA = new ImmutableTestClassMetadata(true, "it.javalinux.testedby.testsupport.TestClassTwo", "");

    private final static TestClassMetadata TEST_CLASS_ONE_ON_INTERFACE_METADATA = new ImmutableTestClassMetadata(true, "it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceOne", "");

    private final static TestClassMetadata TEST_CLASS_TWO_ON_INTERFACE_METADATA = new ImmutableTestClassMetadata(true, "it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceTwo", "");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();

    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void buildShouldRUnOnClassUnderTestAnnotationListOnClass() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestAnnotationListOnClass.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestAnnotationListOnClass"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestAnnotationListOnClass");
	assertThat(metadata.getAllTestMetadata().size(), is(2));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(2));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata)), equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}
    }

    @Test
    public void buildShouldRUnOnClassUnderTestOneAnnotationAndListOnClass() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationAndListOnClass.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationAndListOnClass"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationAndListOnClass");
	assertThat(metadata.getAllTestMetadata().size(), is(2));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(2));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata)), equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}
    }

    @Test
    public void shouldRunClassUnderTestOneAnnotationAndListOnMethods() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationAndListOnMethods.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationAndListOnMethods"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationAndListOnMethods");
	assertThat(metadata.getTestClassesMetadata().size(), is(0));

	for (TestClassMetadata classMetadata : metadata.getMethodsSpecificMetadata().get("methodOne").getTestClassesMetadata()) {
	    if (classMetadata.equals(TEST_CLASS_ONE_METADATA)) {
		assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(1));
		assertThat(classMetadata.getMethodsSpecificMetadata(), hasItem(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata))));
	    } else {
		fail("method One Should not have testClasseMetadata!=TEST_CLASS_ONE_METADATA");
	    }
	}

	for (TestClassMetadata classMetadata : metadata.getMethodsSpecificMetadata().get("methodTwo").getTestClassesMetadata()) {
	    if (classMetadata.equals(TEST_CLASS_ONE_METADATA)) {
		assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(1));
		assertThat(classMetadata.getMethodsSpecificMetadata(), hasItem(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	    } else {
		assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(2));
		assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata)), equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));

	    }
	}
	assertThat(metadata.getAllTestMetadata().size(), is(2));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));

	// merging is fine
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(2));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata)), equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}

    }

    @Test
    public void buildShouldRunOnClassUnderTestOneAnnotationOnClass() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationOnClass.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnClass"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnClass");
	assertThat(metadata.getAllTestMetadata().size(), is(1));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_ONE_METADATA)));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(1));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItem(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata))));
	}
    }

    @Test
    public void buildShouldRunClassUnderTestOneAnnotationOnMethod() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationOnMethod.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnMethod"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnMethod");
	// no metadata directly on class
	assertThat(metadata.getTestClassesMetadata().size(), is(0));
	// metadatas from methods
	assertThat(metadata.getAllTestMetadata().size(), is(1));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_ONE_METADATA)));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(2));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata)), equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}
    }

    @Test
    public void buildShouldRunClassUnderTestOneAnnotationOnWrongMethod() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationOnWrongMethod.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnWrongMethod"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationOnWrongMethod");
	// no metadata directly on class
	assertThat(metadata.getTestClassesMetadata().size(), is(0));
	// metadatas from methods
	assertThat(metadata.getAllTestMetadata().size(), is(1));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_ONE_METADATA)));
	assertThat(metadata.getAllTestMetadata().iterator().next().isValid(), is(false));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(1));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItem(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("wrongMethod", classMetadata))));
	}
    }

    @Test
    public void buildShouldRunClassUnderTestOneAnnotationWithWrongClassName() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationWithWrongClassName.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationWithWrongClassName"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestOneAnnotationWithWrongClassName");
	// no metadata directly on class
	assertThat(metadata.getTestClassesMetadata().size(), is(1));
	// metadatas from methods
	assertThat(metadata.getAllTestMetadata().size(), is(1));
	TestClassMetadata testClassWrong = new ImmutableTestClassMetadata(true, "it.javalinux.testedby.testsupport.TestClassWrong", "");
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(testClassWrong)));
	assertThat(metadata.getAllTestMetadata().iterator().next().isValid(), is(false));

    }

    @Test
    public void buildShouldRunOnClassUnderTestWithAllAnnotations() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestWithAllAnnotations.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestWithAllAnnotations"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestWithAllAnnotations");
	assertThat(metadata.getAllTestMetadata().size(), is(2));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(2));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata)), equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}
    }

    @Test
    public void buildShouldRunOnClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses, true);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods");
	assertThat(metadata.getAllTestMetadata().size(), is(1));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_TWO_METADATA)));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(1));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}
    }

    @Test
    public void buildShouldRunOnClassImplementingInterfaceUnderTestWithItsOwnAnnotations() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class, TestClassOnInterfaceOne.class, TestClassOnInterfaceTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassImplementingInterfaceUnderTestWithItsOwnAnnotations.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses, false);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassImplementingInterfaceUnderTestWithItsOwnAnnotations"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassImplementingInterfaceUnderTestWithItsOwnAnnotations");
	assertThat(metadata.getAllTestMetadata().size(), is(2));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_TWO_METADATA), equalTo(TEST_CLASS_TWO_ON_INTERFACE_METADATA)));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(2));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}
    }

    @Test
    public void buildShouldRunOnClassExtendingAbstractClass() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class, TestClassOnInterfaceOne.class, TestClassOnInterfaceTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassExtendingAbstractClass.class);
	Map<String, ClassUnderTestMetadata> metadatas = builder.build(classesUnderTest, testClasses, true);
	assertThat(metadatas.keySet(), hasItem("it.javalinux.testedby.testsupport.ClassExtendingAbstractClass"));
	ClassUnderTestMetadata metadata = metadatas.get("it.javalinux.testedby.testsupport.ClassExtendingAbstractClass");
	assertThat(metadata.getAllTestMetadata().size(), is(2));
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo(TEST_CLASS_ONE_ON_INTERFACE_METADATA), equalTo(TEST_CLASS_TWO_ON_INTERFACE_METADATA)));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetadata().size(), is(2));
	    assertThat(classMetadata.getMethodsSpecificMetadata(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}
    }
}
