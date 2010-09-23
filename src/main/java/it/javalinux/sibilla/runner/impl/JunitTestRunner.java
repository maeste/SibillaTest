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
package it.javalinux.sibilla.runner.impl;

import it.javalinux.sibilla.metadata.ClassLinkMetadata;
import it.javalinux.sibilla.metadata.builder.instrumentation.InvocationTracker;
import it.javalinux.sibilla.runner.AbstractUnitRunner;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public class JunitTestRunner extends AbstractUnitRunner {

	private final JUnitCore core;

	private final RunListener listener;

	/**
	 * @param _core
	 * @param _listener
	 */
	public JunitTestRunner(JUnitCore _core, RunListener _listener) {
		super();
		this.core = _core;
		this.listener = _listener;
		core.addListener(listener);
	}

	/**
	 * @param _listener
	 */
	public JunitTestRunner(RunListener _listener) {
		this(new JUnitCore(), _listener);
	}

	public JunitTestRunner(JUnitCore _core) {
		this(_core, new TextListener(new RealSystem()));
	}

	public JunitTestRunner() {
		this(new JUnitCore());
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see it.javalinux.sibilla.runner.AbstractUnitRunner#runTest(String,
	 *      String, ClassLinkMetadata...)
	 */
	@Override
	public boolean runTest(String testClass, String methodName,
			ClassLinkMetadata... unfilteredClassesUnderTest) {
		try {
			List<ClassLinkMetadata> classesUnderTest = filterOnlyFromAnnotation(unfilteredClassesUnderTest);
			Class<?> testClazz = Thread.currentThread().getContextClassLoader()
					.loadClass(testClass);
			Request request = Request.method(testClazz, methodName);
			boolean status = true;
			if (classesUnderTest.size() > 0) {
				for (ClassLinkMetadata classLinkMetadata : classesUnderTest) {
					if (!classLinkMetadata.getStatus().isOnAbstract()) {
						InvocationTracker tracker = InvocationTracker
								.getInstance();
						tracker.setCurrentClassUnderTest(classLinkMetadata
								.getClazz());
						Result result = core.run(request);
						tracker.setCurrentClassUnderTest(null);
						System.err.println(result.wasSuccessful());
						status &= result.wasSuccessful();
					}
				}
			} else {

				Result result = core.run(request);

				status = result.wasSuccessful();
			}
			return status;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see it.javalinux.sibilla.runner.AbstractUnitRunner#getTestMethods(java.lang.Class)
	 */
	@Override
	protected Collection<Method> getTestMethods(Class<?> testClass) {
		List<Method> list = new LinkedList<Method>();
		for (Method method : testClass.getMethods()) {
			if (method.getAnnotation(Test.class) != null) {
				list.add(method);
			}
		}
		return list;
	}

	List<ClassLinkMetadata> filterOnlyFromAnnotation(
			ClassLinkMetadata... classLinks) {
		LinkedList<ClassLinkMetadata> filteredCollection = new LinkedList<ClassLinkMetadata>();
		for (ClassLinkMetadata classLinkMetadata : classLinks) {
			if (classLinkMetadata.getStatus().isFromAnnotation()) {
				filteredCollection.add(classLinkMetadata);
			}
		}
		return filteredCollection;
	}

}
