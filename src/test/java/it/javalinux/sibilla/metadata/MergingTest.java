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

import java.util.List;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import it.javalinux.sibilla.metadata.ClassLinkMetadata;
import it.javalinux.sibilla.metadata.MethodLinkMetadata;
import it.javalinux.sibilla.metadata.MethodMetadata;
import it.javalinux.sibilla.metadata.StatusMetadata;
import it.javalinux.sibilla.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.sibilla.metadata.impl.MetadataRepository;

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
	// boolean valid, boolean justCreated, boolean fromAnnotation, boolean
	// fromInstrumentation
	StatusMetadata status1 = new StatusMetadata(false, false, true, false);
	StatusMetadata status2 = new StatusMetadata(true, true, false, true);
	status1.merge(status2);
	assertThat(status1.isFromAnnotation(), is(true));
	assertThat(status1.isFromInstrumentation(), is(true));
	assertThat(status1.isValid(), is(false));
	assertThat(status1.isJustCreated(), is(false));

    }

    @Test
    public void shouldCorrectlyMergeClassLinkMetadata() {
	StatusMetadata status1 = new StatusMetadata(false, false, false, false);
	StatusMetadata status2 = new StatusMetadata(true, true, true, true);

	ClassLinkMetadata link1 = new ClassLinkMetadata(status1, "it.javalinux.Foo");
	ClassLinkMetadata link2 = new ClassLinkMetadata(status2, "it.javalinux.FooBar");
	ClassLinkMetadata link3 = new ClassLinkMetadata(status2, "it.javalinux.Foo");
	link1.merge(link2);
	// no merge happened, different classes
	assertThat(status1.isFromAnnotation(), is(false));
	assertThat(status1.isFromInstrumentation(), is(false));
	assertThat(status1.isValid(), is(false));
	assertThat(status1.isJustCreated(), is(false));

	link1.merge(link3);
	// merge happened
	assertThat(status1.isFromAnnotation(), is(true));
	assertThat(status1.isFromInstrumentation(), is(true));
	assertThat(status1.isValid(), is(false));
	assertThat(status1.isJustCreated(), is(false));
    }

    @Test
    public void shouldCorrectlyMergeMethodLinkMetadata() {
	StatusMetadata status1 = new StatusMetadata(false, false, false, false);
	StatusMetadata status2 = new StatusMetadata(true, true, true, true);

	MethodMetadata method1 = new ImmutableMethodMetadata("it.javalinux.Foo", "method1", new String[] { "boolean" });
	MethodMetadata method2 = new ImmutableMethodMetadata("it.javalinux.Foo", "method2", new String[] { "boolean, int" });

	MethodLinkMetadata link1 = new MethodLinkMetadata(status1, "it.javalinux.Foo", method1);
	MethodLinkMetadata link2 = new MethodLinkMetadata(status2, "it.javalinux.Foo", method2);
	MethodLinkMetadata link3 = new MethodLinkMetadata(status2, "it.javalinux.Foo", method1);
	link1.merge(link2);
	// no merge happened, different classes
	assertThat(status1.isFromAnnotation(), is(false));
	assertThat(status1.isFromInstrumentation(), is(false));
	assertThat(status1.isValid(), is(false));
	assertThat(status1.isJustCreated(), is(false));

	link1.merge(link3);
	// merge happened
	assertThat(status1.isFromAnnotation(), is(true));
	assertThat(status1.isFromInstrumentation(), is(true));
	assertThat(status1.isValid(), is(false));
	assertThat(status1.isJustCreated(), is(false));
    }

    @Test
    public void shouldCorrectlyMergeTestMetadata() {

	MetadataRepository repository1 = new MetadataRepository();
	MetadataRepository repository2 = new MetadataRepository();

	StatusMetadata status1 = new StatusMetadata(false, false, false, false);
	StatusMetadata status2 = new StatusMetadata(true, true, true, true);

	MethodMetadata method1 = new ImmutableMethodMetadata("it.javalinux.Foo", "method1", new String[] { "boolean" });
	MethodMetadata method2 = new ImmutableMethodMetadata("it.javalinux.Foo", "method2", new String[] { "boolean, int" });

	MethodLinkMetadata mlink1 = new MethodLinkMetadata(status1, "it.javalinux.Foo", method1);
	MethodLinkMetadata mlink2 = new MethodLinkMetadata(status2, "it.javalinux.Foo", method2);
	MethodLinkMetadata mlink3 = new MethodLinkMetadata(status2, "it.javalinux.Foo", method1);
	ClassLinkMetadata clink1 = new ClassLinkMetadata(status1, "it.javalinux.Foo");
	ClassLinkMetadata clink2 = new ClassLinkMetadata(status2, "it.javalinux.FooBar");
	ClassLinkMetadata clink3 = new ClassLinkMetadata(status2, "it.javalinux.Foo");

	repository1.addConnection("it.javalinux.FooTest", "testMethodOne", new String[] {}, "it.javalinx.Foo", "methodUTOne", new String[] {}, status1);
	repository1.addConnection("it.javalinux.FooTest", "testMethodTwo", new String[] {}, "it.javalinx.Foo", "methodUTOne", new String[] {}, status1);
	repository1.addConnection("it.javalinux.FooBarTest", "testMethodOne", new String[] {}, "it.javalinx.FooBar", "methodUTOne", new String[] {}, status1);
	repository2.addConnection("it.javalinux.FooTest", "testMethodOne", new String[] {}, "it.javalinx.Foo", "methodUTOne", new String[] {}, status2);
	repository2.addConnection("it.javalinux.FooTest", "testMethodThree", new String[] {}, "it.javalinx.Foo", "methodUTOne", new String[] {}, status2);
	repository2.addConnection("it.javalinux.FooBarTest", null, null, "it.javalinx.FooBar", "methodUTOne", new String[] {}, status2);

	repository1.merge(repository2);

	assertThat(repository1.getAllTestClasses().size(), is(2));
	assertThat(repository1.getAllTestedClasses().size(), is(2));
	System.out.println(repository2.getAllTestMethods());

	System.out.println(repository1.getAllTestMethods());
	assertThat(repository1.getAllTestMethods().size(), is(4));
	assertThat(repository1.getAllTestedMethods().size(), is(2));
	// TODO: provide better assertion controls
    }
}
