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

/**
 * It represent metadatas for a Test method. It contains both its own metadata
 * (accessed by {@link #getMethodName()} and
 * {@link #getMethodParameterTypes()} )
 * 
 * 
 * It is part of {@link TestClassMetadata}. See its javadoc for further
 * explanation
 * 
 * Metadata will be created during {@link ClassUnderTestMetadata} or 
 * {@link MethodUnderTestMetadata} creation by a {@link MetaDataBuilder} with a
 * specific strategy
 * 
 * @author stefano.maestri@javalinux.it
 * 
 */
public interface TestMethodMetadata  {
    /**
     * 
     * @return the test method name
     */
    public String getMethodName();

    /**
     * 
     * @return the test method parameter types
     */
    public Class<?>[] getMethodParameterTypes();

}
