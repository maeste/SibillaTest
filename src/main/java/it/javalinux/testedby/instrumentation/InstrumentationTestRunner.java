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
package it.javalinux.testedby.instrumentation;

import java.util.List;

import it.javalinux.testedby.metadata.ClassLinkMetadata;
import it.javalinux.testedby.metadata.TestsMergeableMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;

/**
 * Interface for test runner leveraging instrumentation for getting the TestedBy
 * metadata.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 10-Oct-2009
 * 
 */
public interface InstrumentationTestRunner {

    /**
     * Run the specified tests and return the metadata obtained through tested
     * code instrumentation.
     * 
     * @param testClass
     * @param methodName
     * @param classesUnderTest
     *            optional parameter specifying classesUnderTest previous
     *            collected in metadata. For logging purpose
     * 
     * @return Metadata obtained using instrumentation
     * @throws Exception
     */
    public TestsMergeableMetadata instrumentAndRunTest(String testClass, String methodName, ClassLinkMetadata... classesUnderTest) throws Exception;
}
