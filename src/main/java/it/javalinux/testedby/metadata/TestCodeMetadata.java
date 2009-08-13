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
import java.util.Collection;

/**
 * It represent metadatas for a unit of test code (i.e test class or test method)
 * It contains generic metadata for any level (accessed by {@link #getClassesUnderTest()} and {@link #getMethodsUnderTest()} )
 *
 * 
 * @author stefano.maestri@javalinux.it
 *
 */
public interface TestCodeMetadata {

	/**
	 * 
	 * @return a Colection of {@link Class} that are the class under test stressed by the metadata's unit of code
	 */
	public Collection<Class<?>> getClassesUnderTest();

	/**
	 * 
	 * @return a Colection of {@link Method} that are the method of class under test stressed by the metadata's unit of code
	 */
	public Collection<Method> getMethodsUnderTest();
}
