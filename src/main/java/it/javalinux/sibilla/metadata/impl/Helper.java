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
package it.javalinux.sibilla.metadata.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author alessio.soldano@javalinux.it
 * @since 20-Sep-2009
 *
 */
public class Helper {
    
    private static String[] jvmPackages = new String[]{"sun.", "java.", "javax." };
    private static String[] restrictedPackages = new String[] { "it.javalinux.sibilla.instrumentation.",
							       "it.javalinux.sibilla.metadata.",
							       "it.javalinux.sibilla.builder.",
							       "it.javalinux.sibilla.annotation.",
							       "it.javalinux.sibilla.runner.",
    							       "junit.",
    							       "org.hamcrest.",
    							       "org.junit." };
    
    public static String[] getParameterTypesAsStringArray(Method method)
    {
	List<String> l = new LinkedList<String>();
	for (Class<?> c : method.getParameterTypes()) {
	    l.add(c.getCanonicalName());
	}
	return listToString(l);
    }
    
    public static String[] getParameterTypesAsStringArray(Constructor<?> constructor)
    {
	List<String> l = new LinkedList<String>();
	for (Class<?> c : constructor.getParameterTypes()) {
	    l.add(c.getCanonicalName());
	}
	return listToString(l);
    }
    
    public static String getCanonicalNameFromJavaAssistName(String className)
    {
	return className != null ? className.replaceAll("/", ".") : null;
    }
    
    public static String getMethodNameFromJavaAssistLongName(String methodLongName)
    {
	String method = methodLongName.substring(0, methodLongName.indexOf("("));
	return method.substring(method.lastIndexOf(".") + 1);
    }

    public static String[] getMethodParametersFromJavaAssistLongName(String methodLongName)
    {
	String parameters = methodLongName.substring(methodLongName.indexOf("(") + 1, methodLongName.indexOf(")"));
	StringTokenizer st = new StringTokenizer(parameters, ", ", false);
	List<String> list = new LinkedList<String>();
	while (st.hasMoreTokens())
	{
	    String s = st.nextToken();
	    list.add(s);
	}
	return listToString(list);
    }
    
    private static String[] listToString(List<String> list)
    {
	if (list == null)
	{
	    return null;
	}
	if (list.isEmpty()) {
	    return new String[0];
	}
	return list.toArray(new String[list.size()]);
    }
    
    public static boolean isInJVMPackage(Class<?> clazz) {
	return (clazz != null && isInJVMPackage(clazz.getName()));
    }
    
    public static boolean isInJVMPackage(String className) {
	if (className == null) {
	    return false;
	}
	String modified = className.replaceFirst("/", ".");
	for (String s : jvmPackages)
	{
	    if (modified.startsWith(s)) {
		return true;
	    }
	}
	return false;
    }
    
    public static boolean isInRestrictedPackage(Class<?> clazz) {
	return (clazz != null && isInRestrictedPackage(clazz.getName()));
    }
    
    public static boolean isInRestrictedPackage(String className) {
	if (className == null) {
	    return false;
	}
	String modified = className.replaceAll("/", ".");
	for (String s : restrictedPackages)
	{
	    if (modified.startsWith(s)) {
		return true;
	    }
	}
	return false;
    }
}
