/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package it.javalinux.testedby.junit;

import it.javalinux.testedby.TestedBy;
import java.lang.reflect.Method;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

/**
 * @author oracle
 */
public class TestedByRunner {

    public void run( Class<?> classUnderTest ) throws Exception {
        JUnitCore core = new JUnitCore();
        for (Method method : classUnderTest.getMethods()) {
            TestedBy testedBy = method.getAnnotation(TestedBy.class);
            if (testedBy != null) {
                Class<?> testClass = Thread.currentThread().getContextClassLoader().loadClass(testedBy.testClass());
                Request request = Request.method(testClass, testedBy.testMethod());
                core.run(request);
            }
        }

    }

    public static void main( String[] args ) throws Exception {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(args[0]);
        TestedByRunner runner = new TestedByRunner();
        runner.run(clazz);
    }

}
