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

import it.javalinux.testedby.metadata.builder.MetadataBuilder;

import java.util.Collection;

/**
 * It represent metadatas for a Test class.
 * It contains both its own metadata (accessed by {@link #getClassesUnderTest()} and {@link #getMethodsUnderTest()} )
 * and a Colection of {@link TestMethodMetadata} (accessed by {@link #getMethodsSpecificMetaDatas()})
 * 
 * It aims to collect metadata to make possible navigation of relation between class under test and test classes/methods
 * using class test class  as starting point.
 * 
 * Metadata will be created by a {@link MetadataBuilder} with a specific strategy
 * 
 * @author stefano.maestri@javalinux.it
 *
 */
public interface TestClassMetadata extends TestCodeMetadata {
	
	/**
	 * 
	 * @return the test {@link Class} 
	 */
	public Class<?> getClassUnderTest();
	
	/**
	 * 
	 * @return a Colection of {@link TestMethodMetadata} representing metadata specific of methods of test class
	 */
	public Collection<TestMethodMetadata> getMethodsSpecificMetaDatas(); 

}
