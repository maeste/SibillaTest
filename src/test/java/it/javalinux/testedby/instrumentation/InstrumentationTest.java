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

import static org.junit.matchers.JUnitMatchers.hasItem;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import it.javalinux.testedby.metadata.ClassLinkMetadata;
import it.javalinux.testedby.metadata.MethodLinkMetadata;
import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.testedby.runner.impl.JunitTestRunner;
import it.javalinux.testedby.testsupport.instrumentation.Foo;
import it.javalinux.testedby.testsupport.instrumentation.SampleTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import javassist.ClassPool;

import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * Instrumentation test
 * 
 * Please note this need to run at integration-test phase (Maven) as it requires
 * the project package jar for running the instrumentation.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 11-Oct-2009
 * 
 */
public class InstrumentationTest {
    
    private static boolean verbose = "true".equalsIgnoreCase(System.getProperty("verbose"));

    @Test
    public void testNoInstrumentation() throws Exception {
	InstrumentationTestRunner runner = new JunitTestRunner();
	List<Class<?>> tests = new LinkedList<Class<?>>();
	tests.add(SampleTest.class);
	TestsMetadata metadata = runner.run(tests);
	assertThat(metadata.getAllTestClasses().size(), is(0));
	assertThat(metadata.getAllTestedClasses().size(), is(0));
    }

    @Test
    public void testInstrumentation() throws Exception {

	String command = "java -Xbootclasspath/a:" + getOwnJarPath() + ":" + getJUnitJarPath() + ":" + getJavassistJarPath() + " -javaagent:" + getOwnJarPath() + " -cp " + getTestClassesDir().getPath() + " " + InstrumentationTest.class.getCanonicalName();

	Process p = Runtime.getRuntime().exec(command);
	assertThat(p.waitFor(), is(0));

	BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

	if (verbose) {
	    String s = null;
	    System.out.println("Here is the standard output of the command:\n");
	    while ((s = stdInput.readLine()) != null) {
		System.out.println(s);
	    }

	    // read any errors from the attempted command
	    System.out.println("Here is the standard error of the command (if any):\n");
	    while ((s = stdError.readLine()) != null) {
		System.out.println(s);
	    }
	}
    }

    /**
     * An helper main that is called through the testInstrumentation() method
     * 
     * @param args
     */
    public static void main(String[] args) {
	try {
	    InstrumentationTestRunner runner = new JunitTestRunner();
	    List<Class<?>> tests = new LinkedList<Class<?>>();
	    tests.add(SampleTest.class);
	    TestsMetadata metadata = runner.run(tests);

	    if (verbose) {
		System.out.println("** Tests:");
		for (ClassLinkMetadata test : metadata.getAllTestClasses()) {
		    System.out.println(test.getClazz());
		}
		System.out.println("** Tested classes:");
		for (ClassLinkMetadata tested : metadata.getAllTestedClasses()) {
		    System.out.println(tested.getClazz());
		}
		System.out.println("** Tests (method):");
		for (MethodLinkMetadata test : metadata.getAllTestMethods()) {
		    System.out.println(test.getMethod() + " constructor = " + test.getMethod().isConstructor());
		}
		System.out.println("** Tested methods:");
		for (MethodLinkMetadata tested : metadata.getAllTestedMethods()) {
		    System.out.println(tested.getMethod() + " constructor = " + tested.getMethod().isConstructor());
		}
	    }
	    
	    StatusMetadata status = new StatusMetadata(true, true, false, true);
	    status.setJustCreated(true);
	    status.setPassedOnLastRun(true);
	    
	    assertThat(metadata.getAllTestedClasses().size(), is(1));
	    assertThat(metadata.getAllTestClasses().size(), is(1));
	    assertThat("Cannot find expected test link", metadata.getAllTestClasses(), hasItem(new ClassLinkMetadata(status, SampleTest.class.getName())));
	    assertThat("Wrong expected test link status", metadata.getAllTestClasses().iterator().next().getStatus(), is(status));
	    assertThat("Cannot find expected tested link", metadata.getAllTestedClasses(), hasItem(new ClassLinkMetadata(status, Foo.class.getName())));
	    assertThat("Wrong expected tested link status", metadata.getAllTestedClasses().iterator().next().getStatus(), is(status));
	    
	    assertThat(metadata.getAllTestedClasses().size(), is(1));
	    assertThat(metadata.getAllTestClasses().size(), is(1));
	    assertThat(metadata.getAllTestedMethods().size(), is(2));
	    assertThat(metadata.getAllTestMethods().size(), is(1));
	    assertThat("Cannot find expected test link (method)", metadata.getAllTestMethods(), hasItem(new MethodLinkMetadata(status, SampleTest.class.getName(), new ImmutableMethodMetadata(SampleTest.class.getName(), "testFoo", null))));
	    assertThat("Wrong expected test link status (method)", metadata.getAllTestMethods().iterator().next().getStatus(), is(status));
	    assertThat("Cannot find expected tested link (method)", metadata.getAllTestedMethods(), hasItem(new MethodLinkMetadata(status, Foo.class.getName(), new ImmutableMethodMetadata(Foo.class.getName(), "bar", null))));
	    assertThat("Cannot find expected tested link (constructor)", metadata.getAllTestedMethods(), hasItem(new MethodLinkMetadata(status, Foo.class.getName(), new ImmutableMethodMetadata(Foo.class.getName(), "Foo", null))));
	    assertThat("Wrong expected tested link status (method)", metadata.getAllTestedMethods().get(0).getStatus(), is(status));
	    assertThat("Wrong expected tested link status (method)", metadata.getAllTestedMethods().get(1).getStatus(), is(status));
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}
    }

    private static String getJUnitJarPath() throws Exception {
	return new File(JUnitCore.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
    }

    private static String getJavassistJarPath() throws Exception {
	return new File(ClassPool.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
    }

    private static File getTestClassesDir() throws Exception {
	return new File(Thread.currentThread().getContextClassLoader().getResource(".").toURI());
    }

    private static String getOwnJarPath() throws Exception {
	File testClassesDir = getTestClassesDir();
	File[] files = testClassesDir.getParentFile().listFiles(new FilenameFilter() {
	    public boolean accept(File dir, String name) {
		return (name.startsWith("TestedBy") && name.endsWith(".jar"));
	    }
	});
	return files[0].getPath();
    }

}
