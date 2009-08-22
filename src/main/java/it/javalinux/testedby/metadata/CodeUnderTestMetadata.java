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

import java.util.Collection;

/**
 * It represent metadatas for a unit of code under test (i.e class under test
 * and method under test) It contains {@link TestClassMetadata} representing
 * test classes and their methods stressing current unit of code.
 * 
 * 
 * @author stefano.maestri@javalinux.it
 * 
 */
public interface CodeUnderTestMetadata extends Metadata {

    /**
     * 
     * @return a Colection of {@link TestClassMetadata} of test classes
     *         stressing the unit of code represented. Note that the purpose of
     *         this method is to return metadatas of test stressing specifically the
     *         represented unit of code. IOW it have to return tests directly
     *         linked to this unit of code AND NOT ones linking their components. I.E
     *         if the unit of code is a class under test this method have to
     *         return {@link TestClassMetadata} collected from the class itself
     *         and not from its methods.
     */
    public Collection<TestClassMetadata> getTestClassesMetadatas();

}