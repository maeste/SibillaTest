package it.javalinux.testedby.metadata.builder.annotations;

import static org.junit.matchers.JUnitMatchers.hasItems;

import static org.hamcrest.core.IsEqual.equalTo;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import it.javalinux.testedby.metadata.ClassUnderTestMetadata;
import it.javalinux.testedby.metadata.TestClassMetadata;
import it.javalinux.testedby.metadata.TestMethodMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestClassMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestMethodMetadata;
import it.javalinux.testedby.testsupport.ClassUnderTestAnnotationListOnClass;
import it.javalinux.testedby.testsupport.TestClassOne;
import it.javalinux.testedby.testsupport.TestClassTwo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnnotationBasedMetadataBuilderTest {

    private static AnnotationBasedMetadataBuilder builder;

    private static List<Class<?>> testClasses;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
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
	assertThat(metadata.getAllTestMetadata(), hasItems(equalTo((TestClassMetadata) new ImmutableTestClassMetadata(true, "it.javalinux.testedby.testsupport.TestClassOne", "")), equalTo((TestClassMetadata) new ImmutableTestClassMetadata(true, "it.javalinux.testedby.testsupport.TestClassTwo", ""))));
	for (TestClassMetadata classMetadata : metadata.getAllTestMetadata()) {
	    assertThat(classMetadata.getMethodsSpecificMetaDatas().size(), is(2));
	    assertThat(classMetadata.getMethodsSpecificMetaDatas(), hasItems(equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodOne", classMetadata)), equalTo((TestMethodMetadata) new ImmutableTestMethodMetadata("testMethodTwo", classMetadata))));
	}
    }
}
