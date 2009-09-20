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
package it.javalinux.testedby.metadata.builder.instrumentation;

import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.List;

import static org.junit.Assert.assertTrue;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

import it.javalinux.testedby.metadata_v2.ClassLinkMetadata;
import it.javalinux.testedby.metadata_v2.LinkMetadata;
import it.javalinux.testedby.metadata_v2.MethodLinkMetadata;
import it.javalinux.testedby.metadata_v2.StatusMetadata;
import it.javalinux.testedby.metadata_v2.TestsMetadata;
import it.javalinux.testedby.metadata_v2.builder.instrumentation.InstrumentationBasedMetadataBuilder;
import it.javalinux.testedby.metadata_v2.builder.instrumentation.InvocationTracker;
import it.javalinux.testedby.metadata_v2.impl.ImmutableMethodMetadata;

import org.junit.Test;

/**
 * Some tests for the instrumentation based metadata builder
 * 
 * @author alessio.soldano@javalinux.it
 * @since 20-Sep-2009
 *
 */
public class InstrumentationBasedMetadataBuilderTest {
    
    private static final StatusMetadata STATUS = new StatusMetadata(true, false, false, true);
    private static final ClassLinkMetadata FOO_CLASS_MD = new ClassLinkMetadata(STATUS, Foo.class.getCanonicalName());
    private static final ClassLinkMetadata THIS_CLASS_MD = new ClassLinkMetadata(STATUS, InstrumentationBasedMetadataBuilderTest.class.getCanonicalName());
    private static final MethodLinkMetadata ECHO_METHOD_OF_FOO_CLASS_MD = new MethodLinkMetadata(STATUS, Foo.class.getCanonicalName(),
	    new ImmutableMethodMetadata("echo", new String[]{"java.lang.String"}));
    private static final MethodLinkMetadata MYTESTING_METHOD_OF_THIS_CLASS_MD = new MethodLinkMetadata(STATUS, InstrumentationBasedMetadataBuilderTest.class.getCanonicalName(),
	    new ImmutableMethodMetadata("myTestingMethod", new String[]{"java.lang.String"}));
    
    @Test
    public void testEmptyResult()
    {
	InvocationTracker.cleanUp();
	InvocationTracker.getInstance().addInvokedMethod(Foo.class.getCanonicalName(), "echo(java.lang.String)");
	InstrumentationBasedMetadataBuilder builder = new InstrumentationBasedMetadataBuilder();
	TestsMetadata metadata = builder.getMetadata();
	assertThat(metadata.getAllTestClasses().size(), is(0));
	assertThat(metadata.getAllTestMethods().size(), is(0));
	assertThat(metadata.getAllTestedClasses().size(), is(0));
	assertThat(metadata.getAllTestedMethods().size(), is(0));
	
	builder.performBuildStep(InstrumentationBasedMetadataBuilderTest.class, InstrumentationBasedMetadataBuilderTest.class.getMethods()[0]);
	metadata = builder.getMetadata();
	assertThat(metadata.getAllTestClasses().size(), is(1));
	assertThat(metadata.getAllTestMethods().size(), is(1));
	assertThat(metadata.getAllTestedClasses().size(), is(1));
	assertThat(metadata.getAllTestedMethods().size(), is(1));
	
	builder.reset();
	metadata = builder.getMetadata();
	assertThat(metadata.getAllTestClasses().size(), is(0));
	assertThat(metadata.getAllTestMethods().size(), is(0));
	assertThat(metadata.getAllTestedClasses().size(), is(0));
	assertThat(metadata.getAllTestedMethods().size(), is(0));
    }
    
    @Test
    public void testLinkStatusIsFromInstrumentationAndValid()
    {
	//while running the first method of InstrumentationBasedMetadataBuilderTest, the Foo.echo(String par) method is called...
	InvocationTracker.cleanUp();
	InvocationTracker.getInstance().addInvokedMethod(Foo.class.getCanonicalName(), "echo(java.lang.String)");
	InstrumentationBasedMetadataBuilder builder = new InstrumentationBasedMetadataBuilder();
	builder.performBuildStep(InstrumentationBasedMetadataBuilderTest.class, InstrumentationBasedMetadataBuilderTest.class.getMethods()[0]);
	TestsMetadata metadata = builder.getMetadata();
	for (LinkMetadata lm : metadata.getAllTestClasses())
	{
	    assertTrue(lm.getStatus().isFromInstrumentation());
	    assertTrue(lm.getStatus().isValid());
	}
	for (LinkMetadata lm : metadata.getAllTestedClasses())
	{
	    assertTrue(lm.getStatus().isFromInstrumentation());
	    assertTrue(lm.getStatus().isValid());
	}
	for (LinkMetadata lm : metadata.getAllTestMethods())
	{
	    assertTrue(lm.getStatus().isFromInstrumentation());
	    assertTrue(lm.getStatus().isValid());
	}
	for (LinkMetadata lm : metadata.getAllTestedMethods())
	{
	    assertTrue(lm.getStatus().isFromInstrumentation());
	    assertTrue(lm.getStatus().isValid());
	}
    }
    
    @Test
    public void testBuilderWorksProperlyWithSingleInvocation() throws Exception
    {
	//while running InstrumentationBasedMetadataBuilderTest.myTestingMethod(String par), the Foo.echo(String par) method is called...
	InvocationTracker.cleanUp();
	InvocationTracker.getInstance().addInvokedMethod(Foo.class.getCanonicalName(), "echo(java.lang.String)");
	InstrumentationBasedMetadataBuilder builder = new InstrumentationBasedMetadataBuilder();
	builder.performBuildStep(InstrumentationBasedMetadataBuilderTest.class, InstrumentationBasedMetadataBuilderTest.class.getMethod("myTestingMethod", String.class));
	TestsMetadata metadata = builder.getMetadata();
	
	//Class checks
	List<ClassLinkMetadata> listTestedBy = metadata.getClassesTestedBy(InstrumentationBasedMetadataBuilderTest.class, true);
	List<ClassLinkMetadata> listTestsFoo = metadata.getTestClassesFor(Foo.class, true);
	assertThat(listTestedBy.size(), is(1));
	assertThat(listTestedBy, hasItem(FOO_CLASS_MD));
	assertThat(listTestsFoo.size(), is(1));
	assertThat(listTestsFoo, hasItem(THIS_CLASS_MD));
	
	listTestedBy = metadata.getClassesTestedBy(InstrumentationBasedMetadataBuilderTest.class, false);
	listTestsFoo = metadata.getTestClassesFor(Foo.class, false);
	assertThat(listTestedBy.size(), is(0));
	assertThat(listTestsFoo.size(), is(0));
	
	listTestedBy = metadata.getClassesTestedBy(InstrumentationBasedMetadataBuilderTest.class, InstrumentationBasedMetadataBuilderTest.class.getMethod("myTestingMethod", String.class));
	listTestsFoo = metadata.getTestClassesFor(Foo.class, Foo.class.getMethod("echo", String.class));
	assertThat(listTestedBy.size(), is(1));
	assertThat(listTestedBy, hasItem(FOO_CLASS_MD));
	assertThat(listTestsFoo.size(), is(1));
	assertThat(listTestsFoo, hasItem(THIS_CLASS_MD));
	
	//Method checks
	List<MethodLinkMetadata> methodsTestedBy = metadata.getMethodsTestedBy(InstrumentationBasedMetadataBuilderTest.class, true);
	List<MethodLinkMetadata> methodsTestsFoo = metadata.getTestMethodsFor(Foo.class, true);
	assertThat(methodsTestedBy.size(), is(1));
	assertThat(methodsTestedBy, hasItem(ECHO_METHOD_OF_FOO_CLASS_MD));
	assertThat(methodsTestsFoo.size(), is(1));
	assertThat(methodsTestsFoo, hasItem(MYTESTING_METHOD_OF_THIS_CLASS_MD));

	methodsTestedBy = metadata.getMethodsTestedBy(InstrumentationBasedMetadataBuilderTest.class, false);
	methodsTestsFoo = metadata.getTestMethodsFor(Foo.class, false);
	assertThat(methodsTestedBy.size(), is(0));
	assertThat(methodsTestsFoo.size(), is(0));
	
	methodsTestedBy = metadata.getMethodsTestedBy(InstrumentationBasedMetadataBuilderTest.class, InstrumentationBasedMetadataBuilderTest.class.getMethod("myTestingMethod", String.class));
	methodsTestsFoo = metadata.getTestMethodsFor(Foo.class, Foo.class.getMethod("echo", String.class));
	assertThat(methodsTestedBy.size(), is(1));
	assertThat(methodsTestedBy, hasItem(ECHO_METHOD_OF_FOO_CLASS_MD));
	assertThat(methodsTestsFoo.size(), is(1));
	assertThat(methodsTestsFoo, hasItem(MYTESTING_METHOD_OF_THIS_CLASS_MD));
	
	methodsTestedBy = metadata.getMethodsTestedBy(InstrumentationBasedMetadataBuilderTest.class, InstrumentationBasedMetadataBuilderTest.class.getMethod("anotherTestingMethod", String.class));
	methodsTestsFoo = metadata.getTestMethodsFor(Foo.class, Foo.class.getMethod("ping"));
	assertThat(methodsTestedBy.size(), is(0));
	assertThat(methodsTestsFoo.size(), is(0));
    }
    
    public void myTestingMethod(String par)
    {
	//NOOP
    }
    
    public void anotherTestingMethod(String par)
    {
	//NOOP
    }
    
    @SuppressWarnings("unused")
    private static class Foo
    {
	public String echo(String s)
	{
	    return s;
	}
	
	public String ping()
	{
	    return null;
	}
	
	public int coolMethod(String s, Float f)
	{
	    return 0;
	}
    }
    
    @SuppressWarnings("unused")
    private static class Bar
    {
	public String echo(String s)
	{
	    return s;
	}
	
	public String ping()
	{
	    return null;
	}
	
	public int coolMethod(String s, Float f)
	{
	    return 0;
	}
    }
    
}
