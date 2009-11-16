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
package it.javalinux.testedby.factories;

import it.javalinux.testedby.annotations.TestedByFactory;
import it.javalinux.testedby.metadata.builder.instrumentation.InvocationTracker;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public class ClassUnderTestFactory {
    @SuppressWarnings("unchecked")
    public static <T> T instanceClassUnderTest(Class<T> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	
	    InvocationTracker tracker = InvocationTracker.getInstance();
	    Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(tracker.getCurrentClassUnderTest());
	    if (clazz.isAssignableFrom(cls)) {
		if (cls.getAnnotation(TestedByFactory.class)!= null) {
		    return cls.getAnnotation(TestedByFactory.class).value().newInstance().createInstance((Class<T>) cls);
		} else {
		    return DefaultClassUnderTestInstanceFactory.class.newInstance().createInstance((Class<T>) cls);
		}
	    } else {
		throw new IllegalAccessException("This test try to instantiate a generic type not assignable from current class under test");
	    }
	
    }
}
