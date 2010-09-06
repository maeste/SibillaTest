package it.javalinux.testedby.factories;

import static org.hamcrest.core.Is.is;

import it.javalinux.testedby.metadata.Metadata;
import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.builder.instrumentation.InvocationTracker;
import it.javalinux.testedby.testsupport.factories.MyOwnMetadata;
import it.javalinux.testedby.testsupport.factories.SampleClass;
import static org.junit.Assert.*;
import static it.javalinux.testedby.factories.ClassUnderTestFactory.instanceClassUnderTest;

import org.junit.Test;


public class ClassUnderTestFactoryTest {

    @Test (expected = IllegalAccessException.class)
    public void shouldThrowExceptionIfCurrentClassUTIsntAssignableFromParameter() throws Exception {
	InvocationTracker tracker = InvocationTracker.getInstance();
	tracker.setCurrentClassUnderTest(this.getClass().getCanonicalName());
	Metadata metadata = instanceClassUnderTest(Metadata.class);
    }
    
    @Test (expected = ClassNotFoundException.class)
    public void shouldThrowExceptionIfCurrentClassUTDoesntExist() throws Exception {
	InvocationTracker tracker = InvocationTracker.getInstance();
	tracker.setCurrentClassUnderTest("it.javalinux.NoExistingClass");
	Metadata metadata = instanceClassUnderTest(Metadata.class);
    }

    @Test (expected = InstantiationException.class)
    public void shouldThrowExceptionIfCurrentClassUTInjectFactoryIsntInstantiable() throws Exception {
	InvocationTracker tracker = InvocationTracker.getInstance();
	tracker.setCurrentClassUnderTest(SampleClass.class.getCanonicalName());
	Metadata metadata = instanceClassUnderTest(Metadata.class);
    }
    
    @Test
    public void shouldWorkWithDefaultFactory() throws Exception {
	InvocationTracker tracker = InvocationTracker.getInstance();
	tracker.setCurrentClassUnderTest(StatusMetadata.class.getCanonicalName());
	Metadata metadata = instanceClassUnderTest(Metadata.class);
	assertThat(metadata, is(StatusMetadata.class));
    }
    
    @Test
    public void shouldWorkWithDefinedFactory() throws Exception {
	InvocationTracker tracker = InvocationTracker.getInstance();
	tracker.setCurrentClassUnderTest(MyOwnMetadata.class.getCanonicalName());
	Metadata metadata = instanceClassUnderTest(Metadata.class);
	assertThat(metadata, is(MyOwnMetadata.class));
    }

}
