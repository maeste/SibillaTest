/**
 *  WISE Invokes Services Easily - Stefano Maestri / Alessio Soldano
 *  
 *  http://www.javalinuxlabs.org - http://www.javalinux.it 
 *
 *  Wise is free software; you can redistribute it and/or modify it under the 
 *  terms of the GNU Lesser General Public License as published by the Free Software Foundation; 
 *  either version 2.1 of the License, or (at your option) any later version.
 *
 *  Wise is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */
package it.javalinux.testedby.junit;

import it.javalinux.testedby.TestedBy;
import it.javalinux.testedby.TestedByList;
import java.lang.reflect.Method;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

/**
 * @author stefano.maestri@javalinux.it
 */
public class TestedByRunner {

    public void run( Class<?> classUnderTest ) throws Exception {
        JUnitCore core = new JUnitCore();
        RunListener listener = new TextListener(new RealSystem());
        core.addListener(listener);

        for (Method method : classUnderTest.getMethods()) {
            TestedBy testedBy = method.getAnnotation(TestedBy.class);
            if (testedBy != null) {
                runTestedByElement(classUnderTest, core, listener, testedBy);
            }
            TestedByList list = method.getAnnotation(TestedByList.class);
            if (list != null) {
                for (TestedBy testedByElement : list.value()) {
                    runTestedByElement(classUnderTest, core, listener, testedByElement);
                }

            }

        }

    }

    /**
     * @param classUnderTest
     * @param core
     * @param listener
     * @param testedBy
     * @throws Exception
     * @throws ClassNotFoundException
     */
    private void runTestedByElement( Class<?> classUnderTest,
                                     JUnitCore core,
                                     RunListener listener,
                                     TestedBy testedBy ) throws Exception, ClassNotFoundException {
        listener.testRunStarted(Description.createTestDescription(classUnderTest, "Going to test method of classUnderTest named:"
                                                                                  + testedBy.testMethod()));
        Class<?> testClass = Thread.currentThread().getContextClassLoader().loadClass(testedBy.testClass());
        Request request = Request.method(testClass, testedBy.testMethod());
        Result result = core.run(request);
        System.out.println(result.wasSuccessful());
    }

    public static void main( String[] args ) throws Exception {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(args[0]);
        TestedByRunner runner = new TestedByRunner();
        runner.run(clazz);
    }

}
