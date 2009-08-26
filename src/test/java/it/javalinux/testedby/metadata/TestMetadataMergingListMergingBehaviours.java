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

import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.matchers.JUnitMatchers.hasItems;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.core.Is.is;

import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestClassMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestMethodMetadata;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public class TestMetadataMergingListMergingBehaviours {

    private TestMetadataMergingList<ImmutableTestClassMetadata> list = new TestMetadataMergingList<ImmutableTestClassMetadata>();

    private TestMetadataMergingList<ImmutableTestClassMetadata> listOnlyValid = new TestMetadataMergingList<ImmutableTestClassMetadata>(true);

    @Before
    public void before() {
	list.clear();
	listOnlyValid.clear();
    }

    @Test
    public void shouldNotBeMergeableBothNullTestClassMetadata() {
	assertThat(list.canBeMerged((ImmutableTestClassMetadata) null, (ImmutableTestClassMetadata) null), is(false));
    }

    @Test
    public void shouldNotBeMergeableLeftTestClassMetadataIsNull() {
	assertThat(list.canBeMerged((ImmutableTestClassMetadata) null, mock(ImmutableTestClassMetadata.class)), is(false));
    }

    @Test
    public void shouldBeMergeableTestClassMetadataWithSameName() {
	ImmutableTestClassMetadata m1 = mock(ImmutableTestClassMetadata.class);
	ImmutableTestClassMetadata m2 = mock(ImmutableTestClassMetadata.class);
	when(m1.getTestClassName()).thenReturn("name");
	when(m2.getTestClassName()).thenReturn("name");
	assertThat(list.canBeMerged(m1, m2), is(true));
    }

    @Test
    public void shouldNotBeMergeableTestClassMetadataWithNotSameName() {
	ImmutableTestClassMetadata m1 = mock(ImmutableTestClassMetadata.class);
	ImmutableTestClassMetadata m2 = mock(ImmutableTestClassMetadata.class);
	when(m1.getTestClassName()).thenReturn("name");
	when(m2.getTestClassName()).thenReturn("name2");
	assertThat(list.canBeMerged(m1, m2), is(false));
    }

    @Test
    public void shouldNotBeMergeableBothNullTestClassMetadataAndTestMethod() {
	assertThat(list.canBeMerged((ImmutableTestClassMetadata) null, (ImmutableTestMethodMetadata) null), is(false));
    }

    @Test
    public void shouldNotBeMergeableTestClassMetadataNameIsNull() {
	ImmutableTestClassMetadata m1 = mock(ImmutableTestClassMetadata.class);
	ImmutableTestMethodMetadata m2 = mock(ImmutableTestMethodMetadata.class);
	when(m1.getTestClassName()).thenReturn("");
	assertThat(list.canBeMerged(m1, m2), is(false));
    }

    @Test
    public void shouldBeMergeableTestClassMetadataHasSameNameOfOnePointedByRightMethod() {
	ImmutableTestClassMetadata m1 = mock(ImmutableTestClassMetadata.class);
	ImmutableTestClassMetadata m2 = mock(ImmutableTestClassMetadata.class);
	ImmutableTestMethodMetadata m3 = mock(ImmutableTestMethodMetadata.class);
	when(m1.getTestClassName()).thenReturn("name");
	when(m2.getTestClassName()).thenReturn("name");
	when(m3.getTestClassMetadata()).thenReturn(m2);
	assertThat(list.canBeMerged(m1, m3), is(true));
    }

    @Test
    public void shouldNotBeMergeableBothNullTestMethod() {
	assertThat(list.canBeMerged((ImmutableTestMethodMetadata) null, (ImmutableTestMethodMetadata) null), is(false));
    }

    @Test
    public void shouldMergeSameTestClassesMetadata() throws Exception {
	ImmutableTestClassMetadata c1 = new ImmutableTestClassMetadata(true, "name", "methodName");
	ImmutableTestClassMetadata c2 = new ImmutableTestClassMetadata(true, "name", "methodName2");
	list.add(c1);
	list.add(c2);
	assertThat(list.size(), is(1));
	assertThat(list.iterator().next().getTestClassName(), is("name"));
	Iterator<TestMethodMetadata> iter = list.iterator().next().getMethodsSpecificMetadata().iterator();
	assertThat(iter.next().getMethodName(), is("methodName"));
	assertThat(iter.next().getMethodName(), is("methodName2"));

    }

    @Test
    public void shouldMergeSameTestClassesMetadataWithMergingAndNonMergingMethod() throws Exception {
	ImmutableTestClassMetadata c1 = new ImmutableTestClassMetadata(true, "name", "methodName");
	ImmutableTestClassMetadata c2 = new ImmutableTestClassMetadata(true, "name", "methodName", "methodName2");

	list.add(c1);
	list.add(c2);

	assertThat(list.size(), is(1));
	assertThat(list.iterator().next().getTestClassName(), is("name"));
	Iterator<TestMethodMetadata> iter = list.iterator().next().getMethodsSpecificMetadata().iterator();
	assertThat(iter.next().getMethodName(), is("methodName"));
	assertThat(iter.next().getMethodName(), is("methodName2"));

    }

    @Test
    public void shouldAddNonExistingTestClasses() throws Exception {
	ImmutableTestClassMetadata c1 = new ImmutableTestClassMetadata(true, "name", "methodName");
	ImmutableTestClassMetadata c2 = new ImmutableTestClassMetadata(true, "name2", "methodName2");
	list.add(c1);
	list.add(c2);
	assertThat(list.size(), is(2));

    }

    @Test
    public void shouldMergeSameTestClassesMetadataUsingAddAll() throws Exception {
	ImmutableTestClassMetadata c1 = new ImmutableTestClassMetadata(true, "name", "methodName");
	ImmutableTestClassMetadata c2 = new ImmutableTestClassMetadata(true, "name", "methodName2");
	LinkedList<ImmutableTestClassMetadata> clientList = new LinkedList<ImmutableTestClassMetadata>();
	clientList.add(c1);
	clientList.add(c2);
	list.addAll(clientList);
	assertThat(list.size(), is(1));
	assertThat(list.iterator().next().getTestClassName(), is("name"));
	Iterator<TestMethodMetadata> iter = list.iterator().next().getMethodsSpecificMetadata().iterator();
	assertThat(iter.next().getMethodName(), is("methodName"));
	assertThat(iter.next().getMethodName(), is("methodName2"));

    }

    @Test
    public void shouldAddNonExistingTestClassesUsingAddAll() throws Exception {
	ImmutableTestClassMetadata c1 = new ImmutableTestClassMetadata(true, "name", "methodName");
	ImmutableTestClassMetadata c2 = new ImmutableTestClassMetadata(true, "name2", "methodName2");
	LinkedList<ImmutableTestClassMetadata> clientList = new LinkedList<ImmutableTestClassMetadata>();
	clientList.add(c1);
	clientList.add(c2);
	list.addAll(clientList);
	assertThat(list.size(), is(2));

    }

    @Test
    public void shouldNotAddNonExistingTestClassesUsingAddAllIfInvalidAndOnlyValidIsSetToTrue() throws Exception {
	ImmutableTestClassMetadata c1 = new ImmutableTestClassMetadata(false, "name", "methodName");
	ImmutableTestClassMetadata c2 = new ImmutableTestClassMetadata(true, "name2", "methodName2");
	LinkedList<ImmutableTestClassMetadata> clientList = new LinkedList<ImmutableTestClassMetadata>();
	clientList.add(c1);
	clientList.add(c2);
	listOnlyValid.addAll(clientList);
	assertThat(listOnlyValid.size(), is(1));

    }

    @Test
    public void shouldNotMergeSameTestClassesMetadataIfOneIsNotValidAndOnlyValidIsSetToTrue() throws Exception {
	ImmutableTestClassMetadata c1 = new ImmutableTestClassMetadata(true, "name", "methodName");
	ImmutableTestClassMetadata c2 = new ImmutableTestClassMetadata(false, "name", "methodName2");
	listOnlyValid.add(c1);
	listOnlyValid.add(c2);
	assertThat(listOnlyValid.size(), is(1));
	assertThat(listOnlyValid.iterator().next().getTestClassName(), is("name"));
	assertThat(listOnlyValid.iterator().next().getMethodsSpecificMetadata().size(), is(1));
	Iterator<TestMethodMetadata> iter = listOnlyValid.iterator().next().getMethodsSpecificMetadata().iterator();
	assertThat(iter.next().getMethodName(), is("methodName"));

    }

    @Test
    public void shouldRemoveInvalid() throws Exception {
	ImmutableTestClassMetadata c1 = new ImmutableTestClassMetadata(false, "name", "methodName");
	ImmutableTestClassMetadata c2 = new ImmutableTestClassMetadata(true, "name2", "methodName2");
	LinkedList<ImmutableTestClassMetadata> clientList = new LinkedList<ImmutableTestClassMetadata>();
	clientList.add(c1);
	clientList.add(c2);
	list.addAll(clientList);
	assertThat(list.size(), is(2));
	list.removeInvalid();
	assertThat(list.size(), is(1));

    }

}
