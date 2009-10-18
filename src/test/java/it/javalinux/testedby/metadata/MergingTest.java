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

import org.junit.Test;

/**
 * Tests merging of metadata
 * 
 * @author alessio.soldano@javalinux.it
 * @since 18-Oct-2009
 * 
 */
public class MergingTest {

    @Test
    public void shouldCorrectlyMergeStatusMetadata() {
	//boolean valid, boolean justCreated, boolean fromAnnotation, boolean fromInstrumentation
	StatusMetadata status1 = new StatusMetadata(false, false, false, false);
	StatusMetadata status2 = new StatusMetadata(true, true, true, true);
	status1.merge(status2);
//	assertTrue(status1.isValid());
//	assertTrue(status1.isJustCreated());
	assertTrue(status1.isFromAnnotation());
	assertTrue(status1.isFromInstrumentation());
	//TODO!! provide further tests
    }
    
    @Test
    public void shouldCorrectlyMergeClassLinkMetadata() {
	StatusMetadata status1 = new StatusMetadata(false, false, false, false);
	StatusMetadata status2 = new StatusMetadata(true, true, true, true);

	ClassLinkMetadata link1 = new ClassLinkMetadata(status1, "it.javalinux.Foo");
	ClassLinkMetadata link2 = new ClassLinkMetadata(status2, "it.javalinux.FooBar");
	ClassLinkMetadata link3 = new ClassLinkMetadata(status2, "it.javalinux.Foo");
	link1.merge(link2);
	//no merge happened, different classes
	assertFalse(status1.isValid());
	assertFalse(status1.isJustCreated());
	assertFalse(status1.isFromAnnotation());
	assertFalse(status1.isFromInstrumentation());
	
	link1.merge(link3);
	//TODO!! provide status merge assertions
    }
    
    @Test
    public void shouldCorrectlyMergeMethodLinkMetadata() {
	StatusMetadata status1 = new StatusMetadata(false, false, false, false);
	StatusMetadata status2 = new StatusMetadata(true, true, true, true);

	MethodMetadata method1 = new ImmutableMethodMetadata("method1", new String[]{"boolean"});
	MethodMetadata method2 = new ImmutableMethodMetadata("method2", new String[]{"boolean, int"});
	
	MethodLinkMetadata link1 = new MethodLinkMetadata(status1, "it.javalinux.Foo", method1);
	MethodLinkMetadata link2 = new MethodLinkMetadata(status1, "it.javalinux.Foo", method2);
	MethodLinkMetadata link3 = new MethodLinkMetadata(status1, "it.javalinux.Foo", method1);
	link1.merge(link2);
	//no merge happened, different classes
	assertFalse(status1.isValid());
	assertFalse(status1.isJustCreated());
	assertFalse(status1.isFromAnnotation());
	assertFalse(status1.isFromInstrumentation());
	
	link1.merge(link3);
	//TODO!! provide status merge assertions
    }
}
