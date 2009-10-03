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
package it.javalinux.testedby.runner.impl;

import java.util.Collection;

import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import it.javalinux.testedby.legacy.annotations.TestedBy;
import it.javalinux.testedby.metadata.Metadata;
import it.javalinux.testedby.metadata.MethodLinkMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.runner.AbstractUnitRunner;
import it.javalinux.testedby.runner.TestRunner;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public class JunitTestRunner extends AbstractUnitRunner {

    private final JUnitCore core = new JUnitCore();

    private final RunListener listener;

    /**
     * @param _listener
     */
    public JunitTestRunner(RunListener _listener) {
	super();
	this.listener = _listener;
	core.addListener(listener);
    }

    public JunitTestRunner() {
	super();
	this.listener = new TextListener(new RealSystem());
	core.addListener(listener);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.runner.AbstractUnitRunner#runTestedByElement(java.lang.Class,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void runTestedByElement(Class<?> classUnderTest, String testClass, String methodName) throws Exception, ClassNotFoundException {
	listener.testRunStarted(Description.createTestDescription(classUnderTest, "Going to test classUnderTest named:" + testClass));
	Request request = Request.method(Thread.currentThread().getContextClassLoader().loadClass(testClass), methodName);
	Result result = core.run(request);
	System.out.println(result.wasSuccessful());
    }

}
