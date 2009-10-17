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

import java.lang.reflect.Method;
import java.util.List;

/**
 * This metadata interface defines what can be known about the tests and the
 * code under test
 * 
 * @author alessio.soldano@javalinux.it
 * @since 27-Aug-2009
 * 
 */
public interface TestsMetadata extends Metadata, MergeableMetadata {

    /**
     * Returns the classes tested (classes under test) by the provided class and
     * method
     * 
     * @param clazz
     * @param method
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getClassesTestedBy(Class<?> clazz, Method method);

    /**
     * Returns the classes tested (classes under test) by the provided class and
     * method
     * 
     * @param className
     * @param methodMetadata
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getClassesTestedBy(String className, MethodMetadata methodMetadata);

    /**
     * Returns the classes tested (classes under test) by the provided class
     * 
     * @param clazz
     * @param includeMethods
     *            True to return classes tested by even a single method of the
     *            specified test class
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getClassesTestedBy(Class<?> clazz, boolean includeMethods);

    /**
     * Returns the methods (methods under test) tested by the provided class and
     * method
     * 
     * @param clazz
     * @param method
     * @return a map whose keys are the class names and whose values are sets of
     *         methods of the corresponding class.
     */
    public List<MethodLinkMetadata> getMethodsTestedBy(Class<?> clazz, Method method);

    /**
     * Returns the methods (methods under test) tested by the provided class
     * 
     * @param clazz
     * @param includeMethods
     *            True to return methods tested by even a single method of the
     *            specified test class
     * @return a map whose keys are the class names and whose values are sets of
     *         methods of the corresponding class.
     */
    public List<MethodLinkMetadata> getMethodsTestedBy(Class<?> clazz, boolean includeMethods);

    /**
     * Returns the test classes that test the provided class and method
     * 
     * @param clazz
     * @param method
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getTestClassesFor(Class<?> clazz, Method method);

    /**
     * Returns the test classes that test the provided class
     * 
     * @param clazz
     * @param includeMethods
     *            True to return test classes testing even a single method of
     *            the specified class
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getTestClassesFor(Class<?> clazz, boolean includeMethods);

    /**
     * Returns the test classes that test the provided class, recursing over
     * superclasses / interfaces of the provided class.
     * 
     * @param clazz
     * @param includeMethods
     *            True to return test classes testing even a single method of
     *            the specified class
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getTestClassesForRecursive(Class<?> clazz, boolean includeMethods);

    /**
     * Returns the test methods that test the provided class and method
     * 
     * @param clazz
     * @param method
     * @return a map whose keys are the class names and whose values are sets of
     *         methods of the corresponding class.
     */
    public List<MethodLinkMetadata> getTestMethodsFor(Class<?> clazz, Method method);

    /**
     * Returns the test methods that test the provided class
     * 
     * @param clazz
     * @param includeMethods
     *            True to return test methods testing even a single method of
     *            the specified class
     * @return a map whose keys are the class names and whose values are sets of
     *         methods of the corresponding class.
     */
    public List<MethodLinkMetadata> getTestMethodsFor(Class<?> clazz, boolean includeMethods);

    /**
     * Returns the test methods that test the provided class, recursing over
     * superclasses / interfaces of the provided class.
     * 
     * @param clazz
     * @param includeMethods
     *            True to return test methods testing even a single method of
     *            the specified class
     * @return a map whose keys are the class names and whose values are sets of
     *         methods of the corresponding class.
     */
    public List<MethodLinkMetadata> getTestMethodsForRecursive(Class<?> clazz, boolean includeMethods);

    /**
     * Returns all tested classes currently hold in the metadata model
     * 
     * @return all tested classes currently hold in the metadata model
     */
    public List<ClassLinkMetadata> getAllTestedClasses();

    /**
     * Returns all tested methods currently hold in the metadata model
     * 
     * @return all tested methods currently hold in the metadata model
     */
    public List<MethodLinkMetadata> getAllTestedMethods();

    /**
     * Returns all test classes currently hold in the metadata model
     * 
     * @return all test classes currently hold in the metadata model
     */
    public List<ClassLinkMetadata> getAllTestClasses();

    /**
     * Returns all test methods currently hold in the metadata model
     * 
     * @return all test methods currently hold in the metadata model
     */
    public List<MethodLinkMetadata> getAllTestMethods();

}
