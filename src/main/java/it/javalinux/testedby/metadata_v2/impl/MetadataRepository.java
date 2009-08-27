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
package it.javalinux.testedby.metadata_v2.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.javalinux.testedby.metadata_v2.MethodMetadata;
import it.javalinux.testedby.metadata_v2.TestsMetadata;

/**
 * A repository containing the whole tests<->classesUnderTests relationships.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 27-Aug-2009
 *
 */
public class MetadataRepository implements TestsMetadata {

    private static final long serialVersionUID = 1L;
    
    private Map<MethodInfo, Set<MethodInfo>> connectionsByTests = new HashMap<MethodInfo, Set<MethodInfo>>();
    private Map<MethodInfo, Set<MethodInfo>> connectionsByTested = new HashMap<MethodInfo, Set<MethodInfo>>();

    public void addConnection(String testClass, String testMethod, String[] testMethodParameters, String testedClass, String testedMethod, String[] testedMethodParameters) {
	MethodInfo invoked = new MethodInfo(testedClass, new ImmutableMethodMetadata(testedMethod, testedMethodParameters));
	MethodInfo test = new MethodInfo(testClass, new ImmutableMethodMetadata(testMethod, testMethodParameters));
	//add to connectionsByTests
	if (connectionsByTests.containsKey(test)) {
	    connectionsByTests.get(test).add(invoked);
	} else {
	    Set<MethodInfo> set = new HashSet<MethodInfo>();
	    set.add(invoked);
	    connectionsByTests.put(test, set);
	}
	//add to the connectionsByTested
	if (connectionsByTested.containsKey(invoked)) {
	    connectionsByTested.get(invoked).add(test);
	} else {
	    Set<MethodInfo> set = new HashSet<MethodInfo>();
	    set.add(test);
	    connectionsByTested.put(invoked, set);
	}
    }
    
    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getClassesTestedBy(java.lang.Class, java.lang.reflect.Method)
     */
    public Set<String> getClassesTestedBy(Class<?> clazz, Method method) {
	MethodInfo test = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(method));
	return setToSet(connectionsByTests.get(test));
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getClassesTestedBy(java.lang.Class)
     */
    public Set<String> getClassesTestedBy(Class<?> clazz) {
	MethodInfo test = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(null, null));
	return setToSet(connectionsByTests.get(test));
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getMethodsTestedBy(java.lang.Class, java.lang.reflect.Method)
     */
    public Map<String, Set<MethodMetadata>> getMethodsTestedBy(Class<?> clazz, Method method) {
	MethodInfo test = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(method));
	return setToMap(connectionsByTests.get(test));
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getMethodsTestedBy(java.lang.Class)
     */
    public Map<String, Set<MethodMetadata>> getMethodsTestedBy(Class<?> clazz) {
	MethodInfo test = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(null, null));
	return setToMap(connectionsByTests.get(test));
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getTestClassesFor(java.lang.Class, java.lang.reflect.Method)
     */
    public Set<String> getTestClassesFor(Class<?> clazz, Method method) {
	MethodInfo tested = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(method));
	return setToSet(connectionsByTested.get(tested));
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getTestClassesFor(java.lang.Class)
     */
    public Set<String> getTestClassesFor(Class<?> clazz) {
	MethodInfo tested = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(null, null));
	return setToSet(connectionsByTested.get(tested));
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getTestMethodsFor(java.lang.Class, java.lang.reflect.Method)
     */
    public Map<String, Set<MethodMetadata>> getTestMethodsFor(Class<?> clazz, Method method) {
	MethodInfo tested = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(method));
	return setToMap(connectionsByTested.get(tested));
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getTestMethodsFor(java.lang.Class)
     */
    public Map<String, Set<MethodMetadata>> getTestMethodsFor(Class<?> clazz) {
	MethodInfo tested = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(null, null));
	return setToMap(connectionsByTested.get(tested));
    }

    /**
     * Converts a Set<MethodInfo> to a Map<String, Set<MethodMetadata>>
     * basically grouping the tests by classes.
     * 
     * @param set of class-method couples
     * @return a map class name -> set of methods (metadata) of the class
     */
    private static Map<String, Set<MethodMetadata>> setToMap(Set<MethodInfo> set) {
	Map<String, Set<MethodMetadata>> result = new HashMap<String, Set<MethodMetadata>>();
	if (set != null) {
	    for (MethodInfo mi : set) {
		if (result.containsKey(mi.getClassRef())) {
		    result.get(mi.getClassRef()).add(mi.getMethodRef());
		} else {
		    Set<MethodMetadata> newSet = new HashSet<MethodMetadata>();
		    newSet.add(mi.getMethodRef());
		    result.put(mi.getClassRef(), newSet);
		}
	    }
	}
	return result;
    }
    
    /**
     * Converts a Set<MethodInfo> to a Set<String> containing the classRef only.
     * 
     * @param set
     * @return a Set<String> containing the classRef only
     */
    private static Set<String> setToSet(Set<MethodInfo> set) {
	Set<String> result = new HashSet<String>();
	if (set != null) {
	    for (MethodInfo mi : set) {
		result.add(mi.getClassRef());
	    }
	}
	return result;
    }
    
    /**
     * An inner class for class-method couples
     *
     */
    private class MethodInfo {
	private String classRef;
	private MethodMetadata methodRef;

	/**
	 * @param classRef
	 * @param methodRef
	 */
	public MethodInfo(String classRef, MethodMetadata methodRef) {
	    super();
	    this.classRef = classRef;
	    this.methodRef = methodRef;
	}

	/**
	 * @return classRef
	 */
	public String getClassRef() {
	    return classRef;
	}

	/**
	 * @return methodRef
	 */
	public MethodMetadata getMethodRef() {
	    return methodRef;
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj == null || !(obj instanceof MethodInfo)) {
		return false;
	    }
	    MethodInfo mi = (MethodInfo) obj;
	    // testClass check
	    boolean testClassCheck = (classRef == null) ? (mi.getClassRef() == null) : classRef.equals(mi.getClassRef());
	    // testMethod check
	    boolean testMethodCheck = (methodRef == null) ? (mi.getMethodRef() == null) : methodRef.equals(mi.getMethodRef());
	    return testClassCheck && testMethodCheck;
	}

	@Override
	public int hashCode() {
	    return 31 * (classRef.hashCode() + methodRef.hashCode());
	}
    }

}
