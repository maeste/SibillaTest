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

import it.javalinux.testedby.metadata.builder.instrumentation.InvocationTracker;
import it.javalinux.testedby.metadata.impl.Helper;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.Modifier;

/**
 * The agent class that modifies the bytecode of
 * classes being loaded adding a call to the utility
 * collecting metadata.
 * 
 * See the specified attributes in the manifest added
 * through the maven-jar-plugin in pom.xml.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 23-Aug-2009
 *
 */
public class Agent implements ClassFileTransformer {

    private static Logger LOG = Logger.getLogger(Agent.class.getName());
    
    static String[] includes;
    static String[] excludes;
    static String[] classesExcludes;
    
    static {
	resetConfiguration();
    }
    
    /**
     * The premain method required for this class to be called when
     * providing the JVM arg -javaagent:testedby.jar
     * Adds a new instance of Agent to the specified instrumentation instance.
     * 
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation) {
	LOG.info("Performing class instrumentation...");
	parseArguments(args);
	instrumentation.addTransformer(new Agent());
    }
    
    static void parseArguments(String args) {
	if (args != null) {
	    StringTokenizer st = new StringTokenizer(args, "#", false);
	    while (st.hasMoreTokens()) {
		String token = st.nextToken();
		int j = token.indexOf("=");
		if (j > 0) {
		    String key = token.substring(0, j);
		    if ("include".equalsIgnoreCase(key)) {
			includes = token.substring(j+1).split(",");
			for (int i = 0; i < includes.length; i++) {
			    String s = includes[i];
			    if (!s.endsWith("/")) {
				includes[i] = s + "/";
			    }
			}
		    } else if ("exclude".equalsIgnoreCase(key)) {
			excludes = token.substring(j+1).split(",");
			for (int i = 0; i < excludes.length; i++) {
			    String s = excludes[i];
			    if (!s.endsWith("/")) {
				excludes[i] = s + "/";
			    }
			}
		    } else if ("classesExclude".equalsIgnoreCase(key)) {
			classesExcludes = token.substring(j+1).split(",");
		    }
		}
	    }
	} else {
	    resetConfiguration();
	}
    }

    private static void resetConfiguration() {
	includes = null;
	excludes = null;
	classesExcludes = new String[] { "$Proxy" };
    }
    
    @SuppressWarnings("unused")
    public byte[] transform(ClassLoader loader,
	     		    String className,
	     		    Class<?> classBeingRedefined,
	     		    ProtectionDomain protectionDomain,
	     		    byte[] classfileBuffer) throws IllegalClassFormatException {
	if (isInstrumentationRequired(className)) {
	    try {
		return modifyClass(className, classBeingRedefined, classfileBuffer);
	    } catch (Throwable t) {
		LOG.throwing(Agent.class.getName(), "transform", t);
		t.printStackTrace();
		return null;
	    }
	}
	return classfileBuffer;
    }
    
    static boolean isInstrumentationRequired(String className) {
	if (Helper.isInJVMPackage(className)) {
	    return false;
	}
	if (Helper.isInRestrictedPackage(className)) {
	    return false;
	}
	if (classesExcludes != null) {
	    for (String ce : classesExcludes) {
		// TODO improve this
		if (className.contains(ce)) {
		    return false;
		}
	    }
	}
	String matchedInclude = null;
	String matchedExclude = null;
	if (includes != null) {
	    for (String in : includes) {
		if (className.startsWith(in)) {
		    if (matchedInclude == null || in.length() > matchedInclude.length()) {
			matchedInclude = in;
		    }
		}
	    }
	}
	if (excludes != null) {
	    for (String ex : excludes) {
		if (className.startsWith(ex)) {
		    if (matchedExclude == null || ex.length() > matchedExclude.length()) {
			matchedExclude = ex;
		    }
		}
	    }
	}
	if (matchedExclude == null && matchedInclude != null) {
	    return true;
	} else if (matchedExclude != null && matchedInclude == null) {
	    return false;
	} else if (matchedExclude != null && matchedInclude != null) {
	    return matchedInclude.length() > matchedExclude.length();
	}
	return (includes == null);
    }
    
    private byte[] modifyClass(String className, Class<?> clazz, byte[] bytes) {
	ClassPool pool = ClassPool.getDefault();
	CtClass cl = null;
	try {
	    cl = pool.makeClass(new ByteArrayInputStream(bytes));
	    if (!cl.isInterface()) {
		CtBehavior[] methods = cl.getDeclaredBehaviors();
		for (CtBehavior m : methods) {
		    if (!Modifier.isAbstract(m.getModifiers())) {
//			  System.out.println("** Instrumenting: " + className + " -> " + m.getLongName());
		        //InvocationTracker.getInstance().addInvokedMethod(className, m.getLongName());
			StringBuilder code = new StringBuilder();
			code.append(InvocationTracker.class.getName());
			code.append(".getInstance().addInvokedMethod(\"");
			code.append(Helper.getCanonicalNameFromJavaAssistName(className));
			code.append("\", \"");
			code.append(m.getLongName());
			code.append("\");");
			m.insertBefore(code.toString());
		    }
		}
	    }
	    bytes = cl.toBytecode();
	} catch (Exception e) {
	    LOG.severe("Instrumentation failed of class '" + className + "': " + e.getMessage());
	    throw new RuntimeException(e); //TODO implement proper exception handling
	} finally {
	    if (cl != null) {
		cl.detach();
	    }
	}
	return bytes;
    }

}
