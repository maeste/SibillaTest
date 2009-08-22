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
package it.javalinux.testedby.metadata.merger;

import it.javalinux.testedby.exceptions.MergeException;
import it.javalinux.testedby.metadata.TestClassMetadata;
import it.javalinux.testedby.metadata.TestMethodMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestClassMetadata;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public final class Merger {

    public static <T extends TestClassMetadata> T merge(T left, T right) {
	TestClassMetadata testClassMetaData = null;
	if (! canBeMerged(left, right)) {
	    return left;
	}
	for (TestMethodMetadata rightM : right.getMethodsSpecificMetaDatas()) {
	    testClassMetaData = left;
	    if (canBeMerged(left, rightM)) {
		testClassMetaData = Merger.merge(left, rightM);
	    } else {
		//keep left
	    }
	}
	return (T) testClassMetaData;
    }

    public static < T extends TestClassMetadata> boolean canBeMerged(T left, T right) {
	if(left == null || right == null) {
	    return false;
	}
	return left.getTestClassName() == right.getTestClassName();
    }

    public static <T extends TestClassMetadata, R extends TestMethodMetadata> T merge(T left, R rightM) {
	Collection<TestMethodMetadata> testMethodCol = new LinkedList<TestMethodMetadata>();
	if (! canBeMerged(left, rightM)) {
	    return left;
	}
	boolean merged = false;
	for (TestMethodMetadata leftM : left.getMethodsSpecificMetaDatas()) {
	    if (canBeMerged(leftM, rightM)) {
		testMethodCol.add(Merger.merge(leftM, rightM));
		merged = true;
	    } else {
		testMethodCol.add(leftM);
	    }
	}
	if (!merged) {
	    testMethodCol.add(rightM);
	}
	return (T) new ImmutableTestClassMetadata(left.getTestClassName(), testMethodCol);
    }

    public static < T extends TestClassMetadata, R extends TestMethodMetadata> boolean canBeMerged(T left, R right) {
	if(left == null || right == null) {
	    return false;
	}
	if (right.getTestClassMetadata() == null) {
	    return false;
	}
	return left.getTestClassName() == right.getTestClassMetadata().getTestClassName();
    }

    public static <R extends TestMethodMetadata> R merge(R left, R right) {
	return left;
    }

    public static <R extends TestMethodMetadata> boolean canBeMerged(R left, R right) {
	if(left == null || right == null) {
	    return false;
	}
	return ((left.getMethodName() == right.getMethodName()) && canBeMerged(left.getTestClassMetadata(), right.getTestClassMetadata()));
    }

}
