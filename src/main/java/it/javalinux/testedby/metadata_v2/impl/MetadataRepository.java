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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import it.javalinux.testedby.metadata_v2.ClassLinkMetadata;
import it.javalinux.testedby.metadata_v2.LinkMetadata;
import it.javalinux.testedby.metadata_v2.MethodLinkMetadata;
import it.javalinux.testedby.metadata_v2.MethodMetadata;
import it.javalinux.testedby.metadata_v2.StatusMetadata;
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

    // what a given class-method tests
    private Map<MethodInfo, Set<LinkMetadata>> testsLinks = new HashMap<MethodInfo, Set<LinkMetadata>>();

    // what a given class-method is tested by
    private Map<MethodInfo, Set<LinkMetadata>> isTestedByLinks = new HashMap<MethodInfo, Set<LinkMetadata>>(); // map

    /**
     * Adds a connection from a test method/class to a tested method/class
     *  
     * @param testClass			The test class
     * @param testMethod		The test method, if any
     * @param testMethodParameters	A String array of the test method parameter types
     * @param testedClass		The tested class
     * @param testedMethod		The tested method, if any
     * @param testedMethodParameters	A String array of the tested method parameter types
     * @param status			The link status
     */
    public void addConnection(String testClass, String testMethod, String[] testMethodParameters, String testedClass, String testedMethod, String[] testedMethodParameters, StatusMetadata status) {
	// TODO!! Clone status
	MethodMetadata invokedMethodMetadata = new ImmutableMethodMetadata(testedMethod, testedMethodParameters);
	MethodInfo invoked = new MethodInfo(testedClass, invokedMethodMetadata);
	MethodMetadata testMethodMetadata = new ImmutableMethodMetadata(testMethod, testMethodParameters);
	MethodInfo test = new MethodInfo(testClass, testMethodMetadata);

	// create links
	LinkMetadata invokedLink = (testedMethod == null || testedMethod.trim() == "") ? new ClassLinkMetadata(status, testedClass) : new MethodLinkMetadata(status, testedClass, invokedMethodMetadata);
	LinkMetadata testLink = (testMethod == null || testMethod.trim() == "") ? new ClassLinkMetadata(status, testClass) : new MethodLinkMetadata(status, testClass, testMethodMetadata);

	// add to testsLink
	if (testsLinks.containsKey(test)) {
	    testsLinks.get(test).add(invokedLink);
	} else {
	    Set<LinkMetadata> set = new HashSet<LinkMetadata>();
	    set.add(invokedLink);
	    testsLinks.put(test, set);
	}
	// add to the isTestedByLinks
	if (isTestedByLinks.containsKey(invoked)) {
	    isTestedByLinks.get(invoked).add(testLink);
	} else {
	    Set<LinkMetadata> set = new HashSet<LinkMetadata>();
	    set.add(testLink);
	    isTestedByLinks.put(invoked, set);
	}
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getClassesTestedBy(java.lang.Class,
     *      java.lang.reflect.Method)
     */
    public List<ClassLinkMetadata> getClassesTestedBy(Class<?> clazz, Method method) {
	MethodInfo test = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(method));
	return getClassLinks(testsLinks.get(test));
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getClassesTestedBy(java.lang.Class, boolean)
     */
    public List<ClassLinkMetadata> getClassesTestedBy(Class<?> clazz, boolean includeMethods) {
	MethodInfo test = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(null, null));
	Set<LinkMetadata> set = testsLinks.get(test);
	if (includeMethods) {
	    set = enrichUsingClassMethods(set, clazz, testsLinks);
	}
	return getClassLinks(set);
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getMethodsTestedBy(java.lang.Class,
     *      java.lang.reflect.Method)
     */
    public List<MethodLinkMetadata> getMethodsTestedBy(Class<?> clazz, Method method) {
	MethodInfo test = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(method));
	return getMethodLinks(testsLinks.get(test));
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getMethodsTestedBy(java.lang.Class, boolean)
     */
    public List<MethodLinkMetadata> getMethodsTestedBy(Class<?> clazz, boolean includeMethods) {
	MethodInfo test = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(null, null));
	Set<LinkMetadata> set = testsLinks.get(test);
	if (includeMethods) {
	    set = enrichUsingClassMethods(set, clazz, testsLinks);
	}
	return getMethodLinks(set);
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getTestClassesFor(java.lang.Class,
     *      java.lang.reflect.Method)
     */
    public List<ClassLinkMetadata> getTestClassesFor(Class<?> clazz, Method method) {
	MethodInfo tested = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(method));
	return getClassLinks(isTestedByLinks.get(tested));
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getTestClassesFor(java.lang.Class, boolean)
     */
    public List<ClassLinkMetadata> getTestClassesFor(Class<?> clazz, boolean includeMethods) {
	MethodInfo tested = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(null, null));
	Set<LinkMetadata> set = isTestedByLinks.get(tested);
	if (includeMethods) {
	    set = enrichUsingClassMethods(set, clazz, isTestedByLinks);
	}
	return getClassLinks(set);
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getTestMethodsFor(java.lang.Class,
     *      java.lang.reflect.Method)
     */
    public List<MethodLinkMetadata> getTestMethodsFor(Class<?> clazz, Method method) {
	MethodInfo tested = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(method));
	return getMethodLinks(isTestedByLinks.get(tested));
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getTestMethodsFor(java.lang.Class, boolean)
     */
    public List<MethodLinkMetadata> getTestMethodsFor(Class<?> clazz, boolean includeMethods) {
	MethodInfo tested = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(null, null));
	Set<LinkMetadata> set = isTestedByLinks.get(tested);
	if (includeMethods) {
	    set = enrichUsingClassMethods(set, clazz, isTestedByLinks);
	}
	return getMethodLinks(set);
    }

    private static List<ClassLinkMetadata> getClassLinks(Collection<LinkMetadata> links) {
	Set<ClassLinkMetadata> result = new HashSet<ClassLinkMetadata>();
	if (links != null) {
	    for (LinkMetadata l : links) {
		if (l instanceof ClassLinkMetadata) {
		    result.add((ClassLinkMetadata) l); // TODO!! Clone l
		}
		if (l instanceof MethodLinkMetadata) {
		    result.add(new ClassLinkMetadata(l.getStatus(), ((MethodLinkMetadata) l).getClazz()));
		}
	    }
	}
	return Arrays.asList(result.toArray(new ClassLinkMetadata[result.size()]));
    }

    private static List<MethodLinkMetadata> getMethodLinks(Collection<LinkMetadata> links) {
	List<MethodLinkMetadata> result = new LinkedList<MethodLinkMetadata>();
	if (links != null) {
	    for (LinkMetadata l : links) {
		if (l instanceof MethodLinkMetadata) {
		    result.add((MethodLinkMetadata) l); // TODO!! Clone l
		}
	    }
	}
	return result;
    }
    
    private static Set<LinkMetadata> enrichUsingClassMethods(Set<LinkMetadata> set, Class<?> clazz, Map<MethodInfo, Set<LinkMetadata>> source) {
	if (set == null) {
	    set = new HashSet<LinkMetadata>();
	}
	for (Method m : clazz.getMethods()) {
	    MethodInfo mi = new MethodInfo(clazz.getName(), new ImmutableMethodMetadata(m));
	    Set<LinkMetadata> s = source.get(mi);
	    if (s != null) {
		set.addAll(s);
	    }
	}
	return set;
    }

    /**
     * An inner class for class-method couples
     * 
     */
    private static class MethodInfo {
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

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getAllTestClasses()
     */
    public List<ClassLinkMetadata> getAllTestClasses() {
	Set<LinkMetadata> links = new HashSet<LinkMetadata>(testsLinks.size());
	for (Entry<MethodInfo, Set<LinkMetadata>> entry: testsLinks.entrySet())
	{
	    links.addAll(entry.getValue());
	}
	return getClassLinks(links);
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getAllTestMethods()
     */
    public List<MethodLinkMetadata> getAllTestMethods() {
	Set<LinkMetadata> links = new HashSet<LinkMetadata>(testsLinks.size());
	for (Entry<MethodInfo, Set<LinkMetadata>> entry: testsLinks.entrySet())
	{
	    links.addAll(entry.getValue());
	}
	return getMethodLinks(links);
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getAllTestedClasses()
     */
    public List<ClassLinkMetadata> getAllTestedClasses() {
	Set<LinkMetadata> links = new HashSet<LinkMetadata>(isTestedByLinks.size());
	for (Entry<MethodInfo, Set<LinkMetadata>> entry: isTestedByLinks.entrySet())
	{
	    links.addAll(entry.getValue());
	}
	return getClassLinks(links);
    }

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata_v2.TestsMetadata#getAllTestedMethods()
     */
    public List<MethodLinkMetadata> getAllTestedMethods() {
	Set<LinkMetadata> links = new HashSet<LinkMetadata>(isTestedByLinks.size());
	for (Entry<MethodInfo, Set<LinkMetadata>> entry: isTestedByLinks.entrySet())
	{
	    links.addAll(entry.getValue());
	}
	return getMethodLinks(links);
    }

}
