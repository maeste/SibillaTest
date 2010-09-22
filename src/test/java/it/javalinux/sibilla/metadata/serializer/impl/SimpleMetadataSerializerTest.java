package it.javalinux.sibilla.metadata.serializer.impl;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import it.javalinux.sibilla.metadata.ClassLinkMetadata;
import it.javalinux.sibilla.metadata.MethodLinkMetadata;
import it.javalinux.sibilla.metadata.StatusMetadata;
import it.javalinux.sibilla.metadata.TestsMetadata;
import it.javalinux.sibilla.metadata.builder.annotations.AnnotationBasedMetadataBuilder;
import it.javalinux.sibilla.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.sibilla.metadata.impl.MetadataRepository;
import it.javalinux.sibilla.metadata.serializer.MetadataSerializer;
import it.javalinux.sibilla.metadata.serializer.impl.SimpleMetadataSerializer;
import it.javalinux.sibilla.testsupport.ClassUnderTestOneAnnotationAndListOnClass;
import it.javalinux.sibilla.testsupport.TestClassOne;
import it.javalinux.sibilla.testsupport.TestClassTwo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class SimpleMetadataSerializerTest {

    private final static StatusMetadata status = new StatusMetadata().setFromAnnotation(true).setValid(true);

    private final static ClassLinkMetadata TEST_CLASS_ONE_METADATA = new ClassLinkMetadata(status, "it.javalinux.sibilla.testsupport.TestClassOne");

    private final static ClassLinkMetadata TEST_CLASS_TWO_METADATA = new ClassLinkMetadata(status, "it.javalinux.sibilla.testsupport.TestClassTwo");

    private final static MethodLinkMetadata TEST_METHOD_ONE_IN_CLASS_ONE = new MethodLinkMetadata(status, TEST_CLASS_ONE_METADATA.getClazz(), new ImmutableMethodMetadata(TEST_CLASS_ONE_METADATA.getClazz(), "testMethodOne", null));

    private final static MethodLinkMetadata TEST_METHOD_TWO_IN_CLASS_ONE = new MethodLinkMetadata(status, TEST_CLASS_ONE_METADATA.getClazz(), new ImmutableMethodMetadata(TEST_CLASS_ONE_METADATA.getClazz(), "testMethodTwo", null));

    private final static MethodLinkMetadata TEST_METHOD_ONE_IN_CLASS_TWO = new MethodLinkMetadata(status, TEST_CLASS_TWO_METADATA.getClazz(), new ImmutableMethodMetadata(TEST_CLASS_TWO_METADATA.getClazz(), "testMethodOne", null));

    private final static MethodLinkMetadata TEST_METHOD_TWO_IN_CLASS_TWO = new MethodLinkMetadata(status, TEST_CLASS_TWO_METADATA.getClazz(), new ImmutableMethodMetadata(TEST_CLASS_TWO_METADATA.getClazz(), "testMethodTwo", null));

    @Test
    public void shouldSerializeAndDeserializeOnSameEmptyObject() {
	TestsMetadata metadata = new MetadataRepository();
	MetadataSerializer serializer = new SimpleMetadataSerializer();
	assertThat(serializer.serialize(metadata), is(true));
	assertThat(serializer.deserialize(), equalTo(metadata));
    }

    @Test
    @SuppressWarnings(value = { "unchecked" })
    public void shouldSerializeAndDeserializewithSameContent() {
	List<Class<?>> testClasses = Arrays.asList(TestClassOne.class, TestClassTwo.class);
	AnnotationBasedMetadataBuilder builder = new AnnotationBasedMetadataBuilder();
	List<Class<?>> classesUnderTest = new LinkedList<Class<?>>();
	classesUnderTest.add(ClassUnderTestOneAnnotationAndListOnClass.class);
	TestsMetadata metadatas = builder.build(classesUnderTest, testClasses);
	assertThat(metadatas.getTestClassesFor(ClassUnderTestOneAnnotationAndListOnClass.class, false), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnClass.class, false).size(), is(4));
	assertThat(metadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnClass.class, false), hasItems(equalTo(TEST_METHOD_ONE_IN_CLASS_ONE), equalTo(TEST_METHOD_TWO_IN_CLASS_ONE), equalTo(TEST_METHOD_ONE_IN_CLASS_TWO), equalTo(TEST_METHOD_TWO_IN_CLASS_TWO)));

	MetadataSerializer serializer = new SimpleMetadataSerializer();
	assertThat(serializer.serialize(metadatas), is(true));

	TestsMetadata DeSmetadatas = serializer.deserialize();

	assertThat(DeSmetadatas, equalTo(metadatas));
	assertThat(DeSmetadatas.getTestClassesFor(ClassUnderTestOneAnnotationAndListOnClass.class, false), hasItems(equalTo(TEST_CLASS_ONE_METADATA), equalTo(TEST_CLASS_TWO_METADATA)));
	assertThat(DeSmetadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnClass.class, false).size(), is(4));
	assertThat(DeSmetadatas.getTestMethodsFor(ClassUnderTestOneAnnotationAndListOnClass.class, false), hasItems(equalTo(TEST_METHOD_ONE_IN_CLASS_ONE), equalTo(TEST_METHOD_TWO_IN_CLASS_ONE), equalTo(TEST_METHOD_ONE_IN_CLASS_TWO), equalTo(TEST_METHOD_TWO_IN_CLASS_TWO)));

    }

}
