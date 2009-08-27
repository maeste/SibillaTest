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
package it.javalinux.testedby.metadata_v2;

import java.lang.reflect.Method;
import java.util.List;

/**
 * This metadata interface defines what can be known about the tests and the code under test
 * 
 * @author alessio.soldano@javalinux.it
 * @since 27-Aug-2009
 *
 */
public interface TestsMetadata extends Metadata {

    /**
     * Returns the classes tested (classes under test) by the provided class and method
     * 
     * @param clazz
     * @param method
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getClassesTestedBy(Class<?> clazz, Method method);
    
    /**
     * Returns the classes tested (classes under test) by the provided class
     * 
     * @param clazz
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getClassesTestedBy(Class<?> clazz);
    
    /**
     * Returns the methods (methods under test) tested by the provided class and method
     * 
     * @param clazz
     * @param method
     * @return a map whose keys are the class names and whose values are sets of methods of
     * 	       the corresponding class.
     */
    public List<MethodLinkMetadata> getMethodsTestedBy(Class<?> clazz, Method method);

    /**
     * Returns the methods (methods under test) tested by the provided class 
     * 
     * @param clazz
     * @return a map whose keys are the class names and whose values are sets of methods of
     * 	       the corresponding class.
     */
    public List<MethodLinkMetadata> getMethodsTestedBy(Class<?> clazz);
    
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
     * @return a set with the full qualified name of classes
     */
    public List<ClassLinkMetadata> getTestClassesFor(Class<?> clazz);
    
    /**
     * Returns the test methods that test the provided class and method
     * 
     * @param clazz
     * @param method
     * @return a map whose keys are the class names and whose values are sets of methods of
     * 	       the corresponding class.
     */
    public List<MethodLinkMetadata> getTestMethodsFor(Class<?> clazz, Method method);
    
    /**
     * Returns the test methods that test the provided class
     * 
     * @param clazz
     * @return a map whose keys are the class names and whose values are sets of methods of
     * 	       the corresponding class.
     */
    public List<MethodLinkMetadata> getTestMethodsFor(Class<?> clazz);
}
