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

import it.javalinux.testedby.metadata.merger.Merger;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * @param <E>
 * 
 */
public class MergingList<E extends TestClassMetadata> extends AbstractCollection<E> {

    private LinkedList<E> underlyingList = new LinkedList<E>();

    /**
     * {@inheritDoc}
     * 
     * @see java.util.AbstractCollection#iterator()
     */
    @Override
    public Iterator<E> iterator() {
	return underlyingList.iterator();
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

    /**
     * {@inheritDoc}
     * 
     * @see java.util.AbstractCollection#add(java.lang.Object)
     */
    @Override
    public boolean add(E e) {
	int i;
	if ((i = underlyingList.indexOf(e)) != -1) {
	    E element = underlyingList.get(i);
	    if (Merger.canBeMerged((TestClassMetadata) element, (TestClassMetadata) e)) {
		E newElement = Merger.merge(element, e);
		underlyingList.remove(e);
		return underlyingList.add(newElement);
	    } else {
		return false;
	    }
	} else {
	    return underlyingList.add(e);
	}
    }

}
