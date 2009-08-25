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

import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestClassMetadata;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * @param <E>
 * 
 */
public class TestMetadataMergingList<E extends TestClassMetadata> extends AbstractCollection<E> {

    private final List<E> underlyingList = Collections.synchronizedList(new LinkedList<E>());

    private final boolean onlyValid;

    public TestMetadataMergingList() {
	super();
	this.onlyValid = false;
    }

    /**
     * @param onlyValid
     */
    public TestMetadataMergingList(boolean onlyValid) {
	super();
	this.onlyValid = onlyValid;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.AbstractCollection#add(java.lang.Object)
     */
    @Override
    public synchronized boolean add(E e) {
	int i;
	if (onlyValid && !e.isValid()) {
	    return false;
	}
	if ((i = underlyingList.indexOf(e)) != -1) {
	    E element = underlyingList.get(i);
	    if (canBeMerged(element, e)) {
		E newElement = merge(element, e);
		underlyingList.remove(e);
		return underlyingList.add(newElement);
	    } else {
		return false;
	    }
	} else {
	    return underlyingList.add(e);
	}
    }

    public synchronized boolean removeInvalid() {
	boolean returnValue = false;
	for (E element : underlyingList) {
	    if (!element.isValid()) {
		underlyingList.remove(element);
		returnValue = true;
	    }
	}
	return returnValue;

    }

    /* package */boolean canBeMerged(E left, E right) {
	if (left == null || right == null || left.getTestClassName() == null) {
	    return false;
	}
	return left.getTestClassName().equals(right.getTestClassName());
    }

    /* package */<R extends TestMethodMetadata> boolean canBeMerged(E left, R right) {
	if (left == null || right == null) {
	    return false;
	}
	if (right.getTestClassMetadata() == null) {
	    return false;
	}
	return left.getTestClassName().equals(right.getTestClassMetadata().getTestClassName());
    }

    /* package */<R extends TestMethodMetadata> boolean canBeMerged(R left, R right) {
	if (left == null || right == null) {
	    return false;
	}
	return ((left.getMethodName().equals(right.getMethodName())) && canBeMerged((E) left.getTestClassMetadata(), (E) right.getTestClassMetadata()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.AbstractCollection#iterator()
     */
    @Override
    public synchronized Iterator<E> iterator() {
	return underlyingList.iterator();
    }

    /* package */E merge(E left, E right) {
	TestClassMetadata testClassMetaData = null;
	if (!canBeMerged(left, right)) {
	    return left;
	}
	for (TestMethodMetadata rightM : right.getMethodsSpecificMetaDatas()) {
	    testClassMetaData = left;
	    if (canBeMerged(left, rightM)) {
		testClassMetaData = merge(left, rightM);
	    } else {
		// keep left
	    }
	}
	return (E) testClassMetaData;
    }

    /* package */<R extends TestMethodMetadata> E merge(E left, R rightM) {
	Collection<TestMethodMetadata> testMethodCol = new LinkedList<TestMethodMetadata>();
	if (!canBeMerged(left, rightM)) {
	    return left;
	}
	boolean merged = false;
	for (TestMethodMetadata leftM : left.getMethodsSpecificMetaDatas()) {
	    if (canBeMerged(leftM, rightM)) {
		testMethodCol.add(merge(leftM, rightM));
		merged = true;
	    } else {
		testMethodCol.add(leftM);
	    }
	}
	if (!merged) {
	    testMethodCol.add(rightM);
	}
	return (E) new ImmutableTestClassMetadata(left.isValid(), left.getTestClassName(), testMethodCol);
    }

    /* package */<R extends TestMethodMetadata> R merge(R left, R right) {
	return left;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
	return underlyingList.size();
    }

}
