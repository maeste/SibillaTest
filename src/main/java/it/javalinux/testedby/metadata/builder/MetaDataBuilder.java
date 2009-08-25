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
package it.javalinux.testedby.metadata.builder;

import it.javalinux.testedby.metadata.ClassUnderTestMetadata;

import java.util.Collection;
import java.util.Map;

/**
 * This is a Builder interface. Implementors provide their own metadata
 * collection algorithm
 * 
 * @author Stefano Maestri
 * 
 */
public interface MetaDataBuilder {

    /**
     * Build application metadatas fo
     * 
     * @param classesUnderTest
     * @param testClasses
     * @return application metadata
     * @throws IllegalStateException
     */
    public Map<String, ClassUnderTestMetadata> build(Collection<Class<?>> classesUnderTest, Collection<Class<?>> testClasses);

    /**
     * Build application metadatas fo
     * 
     * @param classesUnderTest
     * @param testClasses
     * @param onlyValid
     * @return application metadata
     * @throws IllegalStateException
     */
    public Map<String, ClassUnderTestMetadata> build(Collection<Class<?>> classesUnderTest, Collection<Class<?>> testClasses, boolean onlyValid);

}
