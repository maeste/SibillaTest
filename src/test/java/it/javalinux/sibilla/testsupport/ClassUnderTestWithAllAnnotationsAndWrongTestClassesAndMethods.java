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
package it.javalinux.sibilla.testsupport;

import it.javalinux.sibilla.annotations.TestedBy;
import it.javalinux.sibilla.annotations.TestedByList;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */

@TestedBy(testClass = "it.javalinux.sibilla.testsupport.TestClassNonExisting")
@TestedByList( { @TestedBy(testClass = "it.javalinux.sibilla.testsupport.TestClassNonExisting", testMethod = "testMethodTwo"),
		@TestedBy(testClass = "it.javalinux.sibilla.testsupport.TestClassTwo", testMethod = "notExist") })
public class ClassUnderTestWithAllAnnotationsAndWrongTestClassesAndMethods {

    @TestedBy(testClass = "it.javalinux.sibilla.testsupport.TestClassOne", testMethod = "notExist")
    public void testMethodOne() {

    }

    @TestedBy(testClass = "it.javalinux.sibilla.testsupport.TestClassTwo", testMethod = "testMethodTwo")
    public void testMethodTwo() {

    }
}