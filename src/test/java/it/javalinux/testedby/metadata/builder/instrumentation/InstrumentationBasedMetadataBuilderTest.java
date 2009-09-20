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

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

import it.javalinux.testedby.metadata_v2.TestsMetadata;
import it.javalinux.testedby.metadata_v2.builder.instrumentation.InstrumentationBasedMetadataBuilder;
import it.javalinux.testedby.metadata_v2.builder.instrumentation.InvocationTracker;

import org.junit.Test;

/**
 * 
 * @author alessio.soldano@javalinux.it
 * @since 20-Sep-2009
 *
 */
public class InstrumentationBasedMetadataBuilderTest {
    
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
    
    private static class Foo
    {
	@SuppressWarnings("unused")
	public String echo(String s)
	{
	    return s;
	}
    }
    
}
