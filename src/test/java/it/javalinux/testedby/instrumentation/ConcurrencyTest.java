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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.javalinux.testedby.metadata_v2.builder.instrumentation.InvocationTracker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.junit.Test;

/**
 * Concurrency tests for the instrumentation based metadata retrieval
 * 
 * @author alessio.soldano@javalinux.it
 * @since 01-Sep-2009
 * 
 */
public class ConcurrencyTest {

    private static final int THREAD_NUM = 10;

    private static final int CALLABLE_NUM = 5;

    private static final int ITERATIONS = 50;

    private static final long INTERNAL_SLEEP = 50;

    @Test
    public void testInvocationTrakerCorrectlyHandlesMultithreading() {
	performTest(null);
    }

    @Test
    public void testInvocationTrakerCorrectlyHandlesMultithreadingDefaultThreadFactory() {
	performTest(Executors.defaultThreadFactory());
    }

    @Test
    public void testInvocationTrakerCorrectlyHandlesMultithreadingWithSimpleThreadFactory() {
	class SimpleThreadFactory implements ThreadFactory {
	    public Thread newThread(Runnable r) {
		return new Thread(r);
	    }
	}
	performTest(new SimpleThreadFactory());
    }

    @Test
    public void testInvocationTrakerCorrectlyHandlesMultithreadingWithComplexThreadFactory() {
	/**
	 * A thread factory that creates each thread in a new thread group
	 */
	class ComplexThreadFactory implements ThreadFactory {
	    private int count = 0;

	    public Thread newThread(Runnable r) {
		ThreadGroup tg = new ThreadGroup("ThreadGroup" + count++);
		return new Thread(tg, r);
	    }
	}
	performTest(new ComplexThreadFactory());
    }

    public void performTest(ThreadFactory factory) {
	List<Thread> threads = new LinkedList<Thread>();
	List<MyRunnable> runnables = new LinkedList<MyRunnable>();

	for (int i = 0; i < THREAD_NUM; i++) {
	    MyRunnable r = new MyRunnable("Thread-" + i, factory);
	    Thread t = new Thread(r);
	    threads.add(t);
	    runnables.add(r);
	}

	for (Thread t : threads) {
	    t.start();
	}

	try {
	    Thread.sleep(2 * ITERATIONS * INTERNAL_SLEEP); // standard and thread calls
	} catch (InterruptedException ie) {
	    // ignore
	}

	boolean done = false;
	while (!done) {
	    try {
		Thread.sleep(INTERNAL_SLEEP);
	    } catch (InterruptedException ie) {
		// ignore
	    }
	    int alive = 0;
	    for (Thread t : threads) {
		if (t.isAlive()) {
		    alive++;
		}
	    }
	    if (alive == 0) {
		done = true;
	    }
	}
	for (MyRunnable r : runnables) {
	    if (r.getError() != null) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		r.getError().printStackTrace(ps);
		fail("Error detected on thread " + r.getName() + ":\n" + baos.toString());
	    }
	}
    }

    /**
     * A Runnable that simulates user code run by a given test and tests the
     * invocations to the tracker go to the right instance.
     * 
     * @author alessio.soldano@javalinux.it
     * 
     */
    private class MyRunnable extends TrackerCaller implements Runnable {

	private String name;

	private ThreadFactory factory;

	private Throwable error = null;

	public MyRunnable(String name, ThreadFactory factory) {
	    super();
	    this.name = name;
	    this.factory = factory;
	}

	/**
	 * @return error
	 */
	public Throwable getError() {
	    return error;
	}

	/**
	 * @return name
	 */
	public String getName() {
	    return name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
	    try {
		Map<String, Set<String>> map = InvocationTracker.getInstance().getInvokedMethodMap();
		assertEquals(0, map.size());
		this.callTracker(ITERATIONS, INTERNAL_SLEEP, name);
		map = InvocationTracker.getInstance().getInvokedMethodMap();
		assertEquals(ITERATIONS, map.size());
		this.threadCalls();
		map = InvocationTracker.getInstance().getInvokedMethodMap();
		assertEquals((ITERATIONS) + (CALLABLE_NUM * ITERATIONS), map.size());
		for (String clazz : map.keySet()) {
		    assertTrue(clazz.startsWith(name));
		    for (String method : map.get(clazz)) {
			assertTrue(method.startsWith(name));
		    }
		}
	    } catch (Throwable e) {
		error = e;
	    }
	}

	private void threadCalls() throws Exception {
	    ExecutorService es = factory == null ? Executors.newFixedThreadPool(THREAD_NUM) : Executors.newFixedThreadPool(THREAD_NUM, factory);
	    Collection<Callable<String>> callables = new LinkedList<Callable<String>>();
	    List<Future<String>> futures = new LinkedList<Future<String>>();
	    for (int i = 0; i < CALLABLE_NUM; i++) {
		callables.add(new MyCallable(name + "(callable" + i + ")"));
	    }
	    futures = es.invokeAll(callables);
	    for (Future<String> f : futures) {
		assertTrue(f.get().startsWith(name)); // loose check
	    }
	}
    }

    private class MyCallable extends TrackerCaller implements Callable<String> {

	private String name;

	public MyCallable(String name) {
	    super();
	    this.name = name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	public String call() throws Exception {
	    callTracker(ITERATIONS, INTERNAL_SLEEP, name);
	    return name;
	}
    }

    private class TrackerCaller {

	public TrackerCaller() {
	    super();
	}

	public void callTracker(int iterations, long sleepBetweenIterations, String prefix) {
	    for (int i = 0; i < iterations; i++) {
		InvocationTracker.getInstance().addInvokedMethod(prefix + "-class-" + i, prefix + "-method-" + (i - 1));
		InvocationTracker.getInstance().addInvokedMethod(prefix + "-class-" + i, prefix + "-method-" + i);
		InvocationTracker.getInstance().addInvokedMethod(prefix + "-class-" + i, prefix + "-method-" + (i + 1));
		try {
		    Thread.sleep(sleepBetweenIterations);
		} catch (InterruptedException ie) {
		    // ignore
		}
	    }
	}
    }

}
