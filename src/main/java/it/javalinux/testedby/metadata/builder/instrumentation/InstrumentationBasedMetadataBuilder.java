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
package it.javalinux.testedby.metadata.builder.instrumentation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.TestsMergeableMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.impl.Helper;
import it.javalinux.testedby.metadata.impl.MetadataRepository;

/**
 * A metadata builder based on the data collected through instrumentation
 * 
 * @author alessio.soldano@javalinux.it
 * @since 20-Sep-2009
 * 
 */
public class InstrumentationBasedMetadataBuilder {

    private MetadataRepository metadata;

    public InstrumentationBasedMetadataBuilder() {
	reset();
    }

    public void reset() {
	this.metadata = new MetadataRepository();
    }

    /**
     * Run a single build step linking information coming from instrumentation
     * to the specified test method run.
     * 
     * @param testClass
     *            The test class that has just been run
     * @param testMethod
     *            The test method that has just been run
     */
    public void performBuildStep(Class<?> testClass, Method testMethod) {
	performBuildStep(testClass, testMethod, null);
    }

    /**
     * Run a single build step linking information coming from instrumentation
     * to the specified test method run.
     * 
     * @param testClass
     *            The test class that has just been run
     * @param testMethod
     *            The test method that has just been run
     * @param status
     *            The status to be assigned to the link created in this step
     *            (the status is cloned)
     */
    public void performBuildStep(Class<?> testClass, Method testMethod, StatusMetadata status) {
	performBuildStep(InvocationTracker.getInstance().getInvokedMethodMap(), testClass, testMethod);
    }

    /**
     * Run a single build step linking information coming from instrumentation
     * to the specified test method run.
     * 
     * @param testClass
     *            The test class that has just been run
     * @param testMethod
     *            The test method that has just been run
     * @param parameterTypes
     *            The parameters of the test method that has just been run
     */
    public void performBuildStep(String testClass, String testMethod, String[] parameterTypes) {
	performBuildStep(testClass, testMethod, parameterTypes, null);
    }

    /**
     * Run a single build step linking information coming from instrumentation
     * to the specified test method run.
     * 
     * @param testClass
     *            The test class that has just been run
     * @param testMethod
     *            The test method that has just been run
     * @param parameterTypes
     *            The parameters of the test method that has just been run
     * @param status
     *            he status to be assigned to the link created in this step (the
     *            status is cloned)
     */
    public void performBuildStep(String testClass, String testMethod, String[] parameterTypes, StatusMetadata status) {
	performBuildStep(InvocationTracker.getInstance().getInvokedMethodMap(), testClass, testMethod, parameterTypes, status);
    }

    void performBuildStep(Map<String, Set<String>> invocationsMap, Class<?> testClass, Method testMethod) {
	performBuildStep(invocationsMap, testClass, testMethod, null);
    }

    void performBuildStep(Map<String, Set<String>> invocationsMap, Class<?> testClass, Method testMethod, StatusMetadata status) {
	performBuildStep(invocationsMap, testClass.getCanonicalName(), testMethod.getName(), Helper.getParameterTypesAsStringArray(testMethod), status);
    }

    void performBuildStep(Map<String, Set<String>> invocationsMap, String testClass, String testMethod, String[] parameterTypes, StatusMetadata status) {
	for (String testedClass : invocationsMap.keySet()) {
	    for (String testedMethod : invocationsMap.get(testedClass)) {
		if (status == null) {
		    status = new StatusMetadata();
		    status.setFromInstrumentation(true);
		    status.setValid(true);
		} else {
		    status = (StatusMetadata) status.clone();
		}
		metadata.addConnection(testClass, testMethod, parameterTypes, testedClass, Helper.getMethodNameFromJavaAssistLongName(testedMethod), Helper.getMethodParametersFromJavaAssistLongName(testedMethod), status);
	    }
	}
    }

    public TestsMergeableMetadata getMetadata() {
	return this.metadata;
    }

}
