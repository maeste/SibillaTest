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
package it.javalinux.testedby.runner;

import java.util.List;

import it.javalinux.testedby.metadata.TestsMergeableMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.serializer.MetadataSerializer;
import it.javalinux.testedby.metadata.serializer.impl.SimpleMetadataSerializer;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public interface TestRunner {

    /**
     * deserialize metadata using default serializer implementation and use
     * 
     * it to run tests it have to run test collected by annotations of modified
     * classes it have to consider previous failed tests
     * 
     * it have to enrich previous metadata with new one (merging of metadata
     * needed); it have to run instrumented tests to collect new metadata
     * 
     * it have to run all new tests classes even if they arent connected by
     * annotation to class under tests to collect new metadata via
     * instrumentation
     * 
     * After run
     * 
     * 1. update methods successfully run as notToRun for next time
     * 
     * 2. Leave failed methods as ToRun for next time
     * 
     * 
     * @param changedClassesUnderTest
     * @param changedTestClasses
     * @return modified TestsMetadata
     * @throws Exception
     */
    public TestsMetadata run(List<Class<?>> changedClassesUnderTest, List<Class<?>> changedTestClasses) throws Exception;

    /**
     * deserialize metadata using passed serializer implementation and use it to
     * run tests it to run tests
     * 
     * it have to run test collected by annotations of modified classes it have
     * to consider previous failed tests
     * 
     * it have to enrich previous metadata with new one (merging of metadata
     * needed); it have to run instrumented tests to collect new metadata
     * 
     * it have to run all new tests classes even if they arent connected by
     * annotation to class under tests to collect new metadata via
     * instrumentation
     * 
     * After run
     * 
     * 1. update methods successfully run as notToRun for next time
     * 
     * 2. Leave failed methods as ToRun for next time
     * 
     * @param changedClassesUnderTest
     * @param changedTestClasses
     * @param serializer
     * @return modified TestsMetadata
     * @throws Exception
     */
    public TestsMetadata run(List<Class<?>> changedClassesUnderTest, List<Class<?>> changedTestClasses, MetadataSerializer serializer) throws Exception;

    /**
     * run all tests which is linked to classUndertest on passed metadata. It
     * return modified metadatas it to run tests
     * 
     * it have to run test collected by annotations of modified classes it have
     * to consider previous failed tests
     * 
     * it have to enrich previous metadata with new one (merging of metadata
     * needed); it have to run instrumented tests to collect new metadata
     * 
     * it have to run all new tests classes even if they arent connected by
     * annotation to class under tests to collect new metadata via
     * instrumentation
     * 
     * After run
     * 
     * 1. update methods successfully run as notToRun for next time
     * 
     * 2. Leave failed methods as ToRun for next time
     * 
     * @param changedClassesUnderTest
     * @param changedTestClasses
     * @param metadata
     * @return modified TestsMetadata
     * @throws Exception
     */
    public TestsMetadata run(List<Class<?>> changedClassesUnderTest, List<Class<?>> changedTestClasses, TestsMergeableMetadata metadata) throws Exception;
}
