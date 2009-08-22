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
package it.javalinux.testedby.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * @author "Luca Perfetti"
 * 
 *@goal testedBy
 *@phase test
 */
public class FirstMavenMojoImpl extends AbstractMojo {

    /**
     * Directory containing the classes and resource files that should be
     * packaged into the JAR.
     * 
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File classesDirectory;

    /**
     * Set this to 'true' to skip running tests, but still compile them. Its use
     * is NOT RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${skipTests}"
     * @since 2.4
     */
    private boolean skipTests;

    /**
     * The base directory of the project being tested. This can be obtained in
     * your unit test by System.getProperty("basedir").
     * 
     * @parameter expression="${basedir}"
     * @required
     */
    private File basedir;

    /**
     * The directory containing generated test classes of the project being
     * tested.
     * 
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     */
    private File testClassesDirectory;

    /**
     * Base directory where all reports are written to.
     * 
     * @parameter expression="${project.build.directory}/testedBy-reports"
     */
    private File reportsDirectory;

    /**
     * The test source directory containing test class sources.
     * 
     * @parameter expression="${project.build.testSourceDirectory}"
     * @required
     * @since 2.2
     */
    private File testSourceDirectory;

    /**
     * Set this to "true" to cause a failure if there are no tests to run.
     * Defaults false.
     * 
     * @parameter expression="${failIfNoTests}"
     * @since 2.4
     */
    private Boolean failIfNoTests;

    private boolean verifyParameters() throws MojoFailureException {
	if (skipTests) {
	    getLog().info("Tests are skipped.");
	    return false;
	}

	if (!testClassesDirectory.exists()) {
	    if (failIfNoTests != null && failIfNoTests.booleanValue()) {
		throw new MojoFailureException("No tests to run!");
	    }
	    getLog().info("No tests to run.");
	    return false;
	}

	return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
	System.out.println("skip test value " + isSkipTests());
	System.out.println("classesDirectory" + getClassesDirectory());
	System.out.println("basedir " + basedir);
	if (verifyParameters()) {
	    getLog().info("start test execution with TestedBy  ");
	} else {
	    getLog().info("no test to run..");
	}

    }

    /**
     * @return classesDirectory
     */
    public File getClassesDirectory() {
	return classesDirectory;
    }

    /**
     * @param classesDirectory
     *            Sets classesDirectory to the specified value.
     */
    public void setClassesDirectory(File classesDirectory) {
	this.classesDirectory = classesDirectory;
    }

    /**
     * @return skipTests
     */
    public boolean isSkipTests() {
	return skipTests;
    }

    /**
     * @param skipTests
     *            Sets skipTests to the specified value.
     */
    public void setSkipTests(boolean skipTests) {
	this.skipTests = skipTests;
    }

    /**
     * @return basedir
     */
    public File getBasedir() {
	return basedir;
    }

    /**
     * @param basedir
     *            Sets basedir to the specified value.
     */
    public void setBasedir(File basedir) {
	this.basedir = basedir;
    }

    /**
     * @return testClassesDirectory
     */
    public File getTestClassesDirectory() {
	return testClassesDirectory;
    }

    /**
     * @param testClassesDirectory
     *            Sets testClassesDirectory to the specified value.
     */
    public void setTestClassesDirectory(File testClassesDirectory) {
	this.testClassesDirectory = testClassesDirectory;
    }

    /**
     * @return reportsDirectory
     */
    public File getReportsDirectory() {
	return reportsDirectory;
    }

    /**
     * @param reportsDirectory
     *            Sets reportsDirectory to the specified value.
     */
    public void setReportsDirectory(File reportsDirectory) {
	this.reportsDirectory = reportsDirectory;
    }

    /**
     * @return testSourceDirectory
     */
    public File getTestSourceDirectory() {
	return testSourceDirectory;
    }

    /**
     * @param testSourceDirectory
     *            Sets testSourceDirectory to the specified value.
     */
    public void setTestSourceDirectory(File testSourceDirectory) {
	this.testSourceDirectory = testSourceDirectory;
    }

    /**
     * @return failIfNoTests
     */
    public Boolean getFailIfNoTests() {
	return failIfNoTests;
    }

    /**
     * @param failIfNoTests
     *            Sets failIfNoTests to the specified value.
     */
    public void setFailIfNoTests(Boolean failIfNoTests) {
	this.failIfNoTests = failIfNoTests;
    }

}
