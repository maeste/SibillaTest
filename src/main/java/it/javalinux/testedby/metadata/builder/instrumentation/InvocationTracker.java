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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Collects raw metadata tracking method invocations
 * happening while running a given test. 
 * 
 * @author alessio.soldano@javalinux.it
 * @since 23-Aug-2009
 *
 */
public class InvocationTracker {

    private static ThreadLocal<InvocationTracker> metadataCollector = new InheritableThreadLocal<InvocationTracker>() {
	@Override
	protected InvocationTracker initialValue() {
	    return new InvocationTracker();
	}
    };

    private String testClass;
    private String testMethod;
    // a map className->methodNames of the invoked classes-methods
    private Map<String, Set<String>> invoked = new HashMap<String, Set<String>>();
    
    public synchronized static InvocationTracker getInstance() {
	return metadataCollector.get();
    }
    
    public synchronized static void cleanUp() {
	metadataCollector.remove();
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }
    
    public synchronized void addInvokedMethod(String clazz, String method) {
	if (invoked.keySet().contains(clazz)) {
	    Set<String> methods = invoked.get(clazz);
	    methods.add(method);
	} else {
	    Set<String> s = new HashSet<String>();
	    s.add(method);
	    invoked.put(clazz, s);
	}
    }
    
    public synchronized Map<String, Set<String>> getInvokedMethodMap() {
	Map<String, Set<String>> map = new HashMap<String, Set<String>>();
	for (String s : invoked.keySet()) {
	    Set<String> set = new HashSet<String>();
	    for (String v : invoked.get(s)) {
		set.add(v);
	    }
	    map.put(s, set);
	}
	return map;
    }
}
