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
package it.javalinux.sibilla.metadata;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import it.javalinux.sibilla.metadata.builder.instrumentation.InvocationTracker;
import it.javalinux.sibilla.metadata.impl.Helper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * A simple collection of tests for the metadata Helper class
 * 
 * @author alessio.soldano@javalinux.it
 * @since 20-Sep-2009
 * 
 */
public class HelperTest {

    @Test
    @SuppressWarnings(value = { "unchecked" })
    public void shouldGetParametersStringArrayFromMethod() {
	Method m = TestClassOne.class.getMethods()[0];
	String[] parameters = Helper.getParameterTypesAsStringArray(m);
	assertThat(parameters.length, is(3));
	assertThat(Arrays.asList(parameters), hasItems(equalTo("java.lang.String"), equalTo("java.lang.String[]"), equalTo("it.javalinux.sibilla.metadata.HelperTest.TestClassTwo")));
    }

    @Test
    @SuppressWarnings(value = { "unchecked" })
    public void shouldMethodInfoFromJavaAssistLongName() {
	String s1 = "it.javalinux.sibilla.metadata.impl.MetadataRepository.addConnection(java.lang.String,java.lang.String," + "java.lang.String[],java.lang.String,java.lang.String,java.lang.String[],it.javalinux.sibilla.metadata_v2.StatusMetadata)";
	String s2 = "it.javalinux.sibilla.metadata.impl.MetadataRepository.getClassesTestedBy(java.lang.Class,boolean)";
	String s3 = "ping()";
	String s4 = "it.echo(java.lang.String)";

	assertThat(Helper.getMethodNameFromJavaAssistLongName(s1), equalTo("addConnection"));
	assertThat(Helper.getMethodNameFromJavaAssistLongName(s2), equalTo("getClassesTestedBy"));
	assertThat(Helper.getMethodNameFromJavaAssistLongName(s3), equalTo("ping"));
	assertThat(Helper.getMethodNameFromJavaAssistLongName(s4), equalTo("echo"));

	List<String> parameters1 = Arrays.asList(Helper.getMethodParametersFromJavaAssistLongName(s1));
	List<String> parameters2 = Arrays.asList(Helper.getMethodParametersFromJavaAssistLongName(s2));
	List<String> parameters3 = Arrays.asList(Helper.getMethodParametersFromJavaAssistLongName(s3));
	List<String> parameters4 = Arrays.asList(Helper.getMethodParametersFromJavaAssistLongName(s4));

	assertThat(parameters1.size(), is(7));
	assertThat(parameters2.size(), is(2));
	assertThat(parameters3.size(), is(0));
	assertThat(parameters4.size(), is(1));

	assertThat(parameters1, hasItems(equalTo("java.lang.String"), equalTo("java.lang.String[]"), equalTo("it.javalinux.sibilla.metadata_v2.StatusMetadata")));
	assertThat(parameters2, hasItems(equalTo("java.lang.Class"), equalTo("boolean")));
	assertThat(parameters4, hasItems(equalTo("java.lang.String")));
    }

    @Test
    public void shouldDetectJVMPackages() {
	assertThat(Helper.isInJVMPackage("javax/xml/bind/MyClass"), is(true));
	assertThat(Helper.isInJVMPackage("javax.xml.bind.MyClass"), is(true));
	assertThat(Helper.isInJVMPackage("java/xml/bind/MyClass"), is(true));
	assertThat(Helper.isInJVMPackage("java.xml.bind.MyClass"), is(true));
	assertThat(Helper.isInJVMPackage("foo/xml/bind/MyClass"), is(false));
	assertThat(Helper.isInJVMPackage("foo.xml.bind.MyClass"), is(false));
	assertThat(Helper.isInJVMPackage("java"), is(false));
	assertThat(Helper.isInJVMPackage("java.Foo"), is(true));
	assertThat(Helper.isInJVMPackage(Boolean.class), is(true));
	assertThat(Helper.isInJVMPackage(HelperTest.class), is(false));
    }

    @Test
    public void shouldDetectRestrictedPackages() {
	assertThat(Helper.isInRestrictedPackage("it/javalinux/sibilla/annotations/Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it.javalinux.sibilla.annotations.Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it/javalinux/sibilla/builder/Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it.javalinux.sibilla.builder.Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it/javalinux/sibilla/metadata/Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it.javalinux.sibilla.metadata.Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it/javalinux/sibilla/instrumentation/Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it.javalinux.sibilla.instrumentation.Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it/javalinux/sibilla/runner/Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it.javalinux.sibilla.runner.Foo"), is(true));
	assertThat(Helper.isInRestrictedPackage("it/javalinux/sibilla/legacy/Foo"), is(false));
	assertThat(Helper.isInRestrictedPackage("it.javalinux.sibilla.legacy.Foo"), is(false));
	assertThat(Helper.isInRestrictedPackage("it.javalinux.sibilla.annotations"), is(false));
	assertThat(Helper.isInRestrictedPackage(InvocationTracker.class), is(true));
    }

    private static class TestClassOne {
	public void getFoo(String a, String[] b, TestClassTwo c) {
	    // NOOP
	}
    }

    private static class TestClassTwo {

    }
}
