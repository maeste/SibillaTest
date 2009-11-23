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

import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.mock;

import static org.junit.Assert.fail;

import static org.mockito.Mockito.times;

import static org.mockito.Mockito.verify;

import static org.mockito.Matchers.anyObject;

import static org.hamcrest.core.IsAnything.any;

import static org.hamcrest.core.IsAnything.anything;

import static org.mockito.Matchers.anyCollection;

import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.when;

import static org.mockito.Mockito.spy;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import it.javalinux.testedby.instrumentation.InstrumentationTest;
import it.javalinux.testedby.instrumentation.InstrumentationTestRunner;
import it.javalinux.testedby.metadata.ClassLinkMetadata;
import it.javalinux.testedby.metadata.MethodLinkMetadata;
import it.javalinux.testedby.metadata.MethodMetadata;
import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.builder.annotations.AnnotationBasedMetadataBuilder;
import it.javalinux.testedby.metadata.builder.instrumentation.InstrumentationBasedMetadataBuilder;
import it.javalinux.testedby.metadata.impl.ImmutableMethodMetadata;
import it.javalinux.testedby.metadata.impl.MetadataRepository;
import it.javalinux.testedby.runner.impl.JunitTestRunner;
import it.javalinux.testedby.testsupport.ClassExtendingAbstractClass;
import it.javalinux.testedby.testsupport.TestClassOne;
import it.javalinux.testedby.testsupport.TestClassTwo;
import it.javalinux.testedby.testsupport.instrumentation.Foo;
import it.javalinux.testedby.testsupport.instrumentation.SampleTest;
import it.javalinux.testedby.testsupport.interfaces.AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations;
import it.javalinux.testedby.testsupport.interfaces.InterfaceUnderTestOne;
import it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceOne;
import it.javalinux.testedby.testsupport.interfaces.TestClassOnInterfaceTwo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javassist.ClassPool;

import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * An integration test for the TestRunner
 * 
 * @author alessio.soldano@javalinux.it
 * @author stefano.maestri@javalinux.it
 * 
 * @since 18-Oct-2009
 * 
 */
public class TestRunnerTest {

    private static Logger log = Logger.getLogger(TestRunnerTest.class.getName());

    private static boolean verbose = "true".equalsIgnoreCase(System.getProperty("verbose"));

    private final static StatusMetadata status = new StatusMetadata().setFromAnnotation(true).setValid(true).setJustCreated(true);

    private final static ClassLinkMetadata ClassExtendingAbstractClass_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.ClassExtendingAbstractClass");

    private final static ClassLinkMetadata AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.AbstractClassClassExtendingInterfaceUnderTestWithAddedAnnotations");

    private final static ClassLinkMetadata InterfaceUnderTestOne_METADATA = new ClassLinkMetadata(status, "it.javalinux.testedby.testsupport.InterfaceUnderTestOne");

    @Test
    public void runningWithoutInstrumentationAndWithoutAnnotationShouldResultInNoMetadata() throws Exception {
	InstrumentationTestRunner runner = new JunitTestRunner();
	List<Class<?>> tests = new LinkedList<Class<?>>();
	tests.add(SampleTest.class);
	TestsMetadata metadata = runner.run(tests);
	assertThat(metadata.getAllTestClasses().size(), is(0));
	assertThat(metadata.getAllTestedClasses().size(), is(0));
    }

    @Test
    public void shouldInstrumentClassesAndMethods() throws Exception {

	String command = "java -Xbootclasspath/a:" + getOwnJarPath() + ":" + getJUnitJarPath() + ":" + getJavassistJarPath() + " -javaagent:" + getOwnJarPath() + " -cp " + getTestClassesDir().getPath() + " " + InstrumentationTest.class.getCanonicalName();

	Process p = Runtime.getRuntime().exec(command);
	int res = p.waitFor();

	if (verbose) {
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String s = null;
	    log.info("Here is the standard output of the command:\n");
	    while ((s = stdInput.readLine()) != null) {
		System.out.println(s);
	    }
	}

	BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	// read any errors from the attempted command
	String t = null;
	StringBuilder sb = new StringBuilder();
	while ((t = stdError.readLine()) != null) {
	    sb.append(t);
	    sb.append("\n");
	}
	log.severe(sb.toString());

	assertThat("The application run by this test did not exit as expected", res, is(0));
    }

    /**
     * An helper main that is called through the testInstrumentation() method
     * 
     * @param args
     */
    public static void main(String[] args) {
	try {
	    JunitTestRunner runner = new JunitTestRunner();
	    List<Class<?>> tests = new LinkedList<Class<?>>();
	    tests.add(SampleTest.class);
	    TestsMetadata metadata = new MetadataRepository();

	    runner.run(Collections.EMPTY_LIST, tests);

	    System.out.println("** Tests:");
	    for (ClassLinkMetadata test : metadata.getAllTestClasses()) {
		System.out.println(test.getClazz());
	    }
	    System.out.println("** Tested classes:");
	    for (ClassLinkMetadata tested : metadata.getAllTestedClasses()) {
		System.out.println(tested.getClazz());
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
	    assertThat("Cannot find expected test link (method)", metadata.getAllTestMethods(), hasItem(new MethodLinkMetadata(status, SampleTest.class.getName(), new ImmutableMethodMetadata(SampleTest.class.getName(), "testFoo", null))));
	    assertThat("Wrong expected test link status (method)", metadata.getAllTestMethods().iterator().next().getStatus(), is(status));
	    assertThat("Cannot find expected tested link (method)", metadata.getAllTestedMethods(), hasItem(new MethodLinkMetadata(status, Foo.class.getName(), new ImmutableMethodMetadata(Foo.class.getName(), "bar", null))));
	    assertThat("Wrong expected tested link status (method)", metadata.getAllTestedMethods().iterator().next().getStatus(), is(status));
	} catch (Throwable t) {
	    throw new RuntimeException(t);
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
