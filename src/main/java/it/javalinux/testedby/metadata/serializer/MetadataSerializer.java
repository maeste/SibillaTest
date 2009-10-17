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
package it.javalinux.testedby.metadata.serializer;

import it.javalinux.testedby.metadata.Metadata;
import it.javalinux.testedby.metadata.TestsMergeableMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;

/**
 * @author oracle
 * 
 */
public interface MetadataSerializer {

    /**
     * 
     * @param metadata
     * @return true if serialisations is possible and went well. False if
     *         serialisation fails. It puts them in a file on current directory
     *         named testedbyMetadata.bin
     */
    public boolean serialize(TestsMergeableMetadata metadata);

    /**
     * 
     * @return deserialised TestsMetadata It gets them from a file on current
     *         directory named testedbyMetadata.bin
     */
    public TestsMergeableMetadata deserialize();

}
