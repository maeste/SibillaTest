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
package it.javalinux.testedby.metadata;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import it.javalinux.testedby.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.testedby.testsupport.TestClassOne;

import org.junit.Test;

/**
 * Tests for MethodMetadata
 * 
 * @author alessio.soldano@javalinux.it
 * @since 07-Nov-2009
 * 
 */
public class MethodMetadataTest {
    
    @Test
    public void shouldCorrectlyDetectConstructors() {
	//basic tests
	assertTrue("Constructor expected", new ImmutableMethodMetadata("it.javalinux.Foo", "Foo", null).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata("it.javalinux.Foo", "bar", null).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata("it.javalinux.Foo", "foo", null).isConstructor());
	//default package tests
	assertTrue("Constructor expected", new ImmutableMethodMetadata("Foo", "Foo", null).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata("Foo", "bar", null).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata("Foo", "foo", null).isConstructor());
	//tests with class name breaking java convention
	assertTrue("Constructor expected", new ImmutableMethodMetadata("it.javalinux.foo", "foo", null).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata("it.javalinux.foo", "bar", null).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata("it.javalinux.foo", "Foo", null).isConstructor());
	assertTrue("Constructor expected", new ImmutableMethodMetadata("foo", "foo", null).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata("foo", "bar", null).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata("foo", "Foo", null).isConstructor());
	//tests using reflection
	assertTrue("Constructor expected", new ImmutableMethodMetadata(TestClassOne.class.getConstructors()[0]).isConstructor());
	assertFalse("Did not expect a constructor", new ImmutableMethodMetadata(TestClassOne.class.getMethods()[0]).isConstructor());
    }
}
