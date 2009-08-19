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

import it.javalinux.testedby.metadata.builder.MetaDataBuilder;

import java.util.Collection;
import java.util.Map;

/**
 * It represent metadatas for ClassUnderTest.
 * It contains both its own metadata (accessed by {@link #getTestClasses()} and {@link #getTestMethods()} )
 * and a Colection of {@link MethodUnderTestMetadata} (accessed by {@link #getMethodsSpecificMetaDatas()})
 * 
 * It aims to collect metadata to make possible navigation of relation between class under test and test classes/methods
 * using class under test as starting point.
 * 
 * Metadata will be created by a {@link MetaDataBuilder} with a specific strategy
 * 
 * @author stefano.maestri@javalinux.it
 *
 */
public interface ClassUnderTestMetadata extends CodeUnderTestMetadata {
	
	
	/**
	 * 
	 * @return the {@link Class} under test
	 */
	public Class<?> getClassUnderTest();
	
	/**
	 * 
	 * @return a {@link Map} of {@link MethodUnderTestMetadata} representing metadata specific of methods of the class under test.
	 *  The key of thios map is the method name
	 */
	public Map<String, MethodUnderTestMetadata> getMethodsSpecificMetaDatas(); 

}
