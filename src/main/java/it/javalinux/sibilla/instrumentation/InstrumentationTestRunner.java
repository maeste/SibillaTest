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
package it.javalinux.sibilla.instrumentation;

import it.javalinux.sibilla.metadata.TestsMetadata;

import java.util.List;

/**
 * Interface for test runner leveraging instrumentation for getting the Sibilla
 * metadata.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 10-Oct-2009
 * 
 */
public interface InstrumentationTestRunner {

    /**
     * Run all the specified tests and return the metadata obtained
     * through tested code instrumentation.
     *
     * @param tests     The test classes to run
     * @return          Metadata obtained using instrumentation
     * @throws Exception
     */
    public TestsMetadata run(List<Class<?>> tests) throws Exception;
}
