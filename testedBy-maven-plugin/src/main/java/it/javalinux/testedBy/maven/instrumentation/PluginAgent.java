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
package it.javalinux.testedBy.maven.instrumentation;

import it.javalinux.testedBy.maven.metadata.CompiledClassTracker;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
/**
 * @author "Luca Perfetti"
 * 
 */
public class PluginAgent implements ClassFileTransformer {

    private static String[] ignore = new String[]{"sun/", "java/", "javax/" };
    private static String[] includes = null;
   
    /**
     * The premain method required for this class to be called when
     * providing the JVM arg -javaagent:testedby.jar
     * Adds a new instance of Agent to the specified instrumentation instance.
     * 
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation) {
	   Logger.getLogger(PluginAgent.class.getName()).info("Performing class instrumentation...");
	if (args != null) {
	    includes = args.split(",");
	    for (int i=0; i < includes.length; i++) {
		String s = includes[i];
		if (!s.endsWith("/"))
		{
		    includes[i] = s + "/";
		}
	    }
	}
	instrumentation.addTransformer(new PluginAgent());
    }
    

    /**
     * {@inheritDoc}
     *
     * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader, java.lang.String, java.lang.Class, java.security.ProtectionDomain, byte[])
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
	if (isInstrumentationRequired(className)) {
	    return modifyClass(className, classBeingRedefined, classfileBuffer);
	}
	return classfileBuffer;
    }

    private static boolean isInstrumentationRequired(String className){
	for (String s : ignore) {
	    if (className.startsWith(s)) {
		return false;
	    }
	}
	if (StringUtils.equalsIgnoreCase(className, "org.codehaus.plexus.compiler.javac.JavacCompiler")) {
	    System.out.println("find compilerClass");
	    return true;
	}
	return false;
    }
    

    public byte[] modifyClass( String className, Class<?> classBeingRedefined, byte[] bytes) {
	ClassPool pool = ClassPool.getDefault();
	CtClass cl = null;
	try {
	    cl = pool.makeClass(new ByteArrayInputStream(bytes));
	   	CtMethod m = cl.getDeclaredMethod("compile");
		StringBuilder code = new StringBuilder();
		code.append(CompiledClassTracker.class.getName());
		code.append(".getInstance().getCompiledClass(getSourceFiles(config));");
		m.insertBefore(code.toString());
	   
	    bytes = cl.toBytecode();
	} catch (Exception e) {
	    Logger.getLogger(PluginAgent.class.getName()).severe("Instrumentation failed of class '" + className +  "': " + e.getMessage());
	    throw new RuntimeException(e); // TODO implement proper exception
					   // handling
	} finally {
	    if (cl != null) {
		cl.detach();
	    }
	}
	return bytes;
    }


}
