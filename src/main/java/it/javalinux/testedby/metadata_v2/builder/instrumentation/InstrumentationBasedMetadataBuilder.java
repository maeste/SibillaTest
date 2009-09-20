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
package it.javalinux.testedby.metadata_v2.builder.instrumentation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import it.javalinux.testedby.metadata_v2.StatusMetadata;
import it.javalinux.testedby.metadata_v2.TestsMetadata;
import it.javalinux.testedby.metadata_v2.impl.Helper;
import it.javalinux.testedby.metadata_v2.impl.MetadataRepository;

/**
 * A metadata builder based on the data collected through instrumentation
 * 
 * @author alessio.soldano@javalinux.it
 * @since 20-Sep-2009
 *
 */
public class InstrumentationBasedMetadataBuilder {
    
    private MetadataRepository metadata;
    
    public InstrumentationBasedMetadataBuilder()
    {
	reset();
    }
    
    public void reset()
    {
	this.metadata = new MetadataRepository();
    }
    
    public void performBuildStep(Class<?> testClass, Method testMethod)
    {
	performBuildStep(InvocationTracker.getInstance().getInvokedMethodMap(), testClass, testMethod);
    }
    
    public void performBuildStep(String testClass, String testMethod, String[] parameterTypes)
    {
	performBuildStep(InvocationTracker.getInstance().getInvokedMethodMap(), testClass, testMethod, parameterTypes);
    }
    
    void performBuildStep(Map<String, Set<String>> invocationsMap, Class<?> testClass, Method testMethod)
    {
	performBuildStep(invocationsMap, testClass.getName(), testMethod.getName(), Helper.getParameterTypesAsStringArray(testMethod));
    }
    
    void performBuildStep(Map<String, Set<String>> invocationsMap, String testClass, String testMethod, String[] parameterTypes)
    {
	for (String testedClass : invocationsMap.keySet())
	{
	    for (String testedMethod : invocationsMap.get(testedClass))
	    {
		StatusMetadata status = new StatusMetadata();
		status.setFromInstrumentation(true);
		status.setValid(true);
		metadata.addConnection(testClass, testMethod, parameterTypes, testedClass, Helper.getMethodNameFromJavaAssistLongName(testedMethod),
			Helper.getMethodParametersFromJavaAssistLongName(testedMethod), status);
	    }
	}
    }
    
    public TestsMetadata getMetadata()
    {
	return this.metadata;
    }
    
}
