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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Agent test
 * 
 * @author alessio.soldano@javalinux.it
 * @since 11-Oct-2009
 * 
 */
public class AgentTest {
    
    @Test
    public void shouldParseArgumentsCorrectly() throws Exception {
	Agent.parseArguments(null);
	assertNull(Agent.includes);
	assertNull(Agent.excludes);
	assertArrayEquals(Agent.classesExcludes, new String[]{"$Proxy"});
	Agent.parseArguments("include=it/javalinux");
	assertArrayEquals(Agent.includes, new String[]{"it/javalinux/"});
	assertNull(Agent.excludes);
	assertArrayEquals(Agent.classesExcludes, new String[]{"$Proxy"});
	Agent.parseArguments("include=it/javalinux,it/javalinux/testedby");
	assertArrayEquals(Agent.includes, new String[]{"it/javalinux/","it/javalinux/testedby/"});
	assertNull(Agent.excludes);
	assertArrayEquals(Agent.classesExcludes, new String[]{"$Proxy"});
	Agent.parseArguments("include=it/javalinux,it/javalinux/testedby#exclude=it/javalinux/testedby/metadata");
	assertArrayEquals(Agent.includes, new String[]{"it/javalinux/","it/javalinux/testedby/"});
	assertArrayEquals(Agent.excludes, new String[]{"it/javalinux/testedby/metadata/"});
	assertArrayEquals(Agent.classesExcludes, new String[]{"$Proxy"});
	Agent.parseArguments("include=it/javalinux,it/javalinux/testedby#exclude=it/javalinux/testedby/metadata#classesExclude=Foo");
	assertArrayEquals(Agent.includes, new String[]{"it/javalinux/","it/javalinux/testedby/"});
	assertArrayEquals(Agent.excludes, new String[]{"it/javalinux/testedby/metadata/"});
	assertArrayEquals(Agent.classesExcludes, new String[]{"Foo"});
    }
    
    @Test
    public void shouldCorrectlyDealWithExclusionsAndInclusions() throws Exception {
	Agent.parseArguments(null);
	assertTrue(Agent.isInstrumentationRequired("foo/javalinux/MyClass"));
	assertTrue(Agent.isInstrumentationRequired("foo/javalinux/testedby/MyClass"));
	assertTrue(Agent.isInstrumentationRequired("foo/javalinux/testedby/metadata/MyClass"));
	assertFalse(Agent.isInstrumentationRequired("it/javalinux/testedby/metadata/MyClass")); //restricted
	assertTrue(Agent.isInstrumentationRequired("foo/javalinux/Foo"));
	
	Agent.parseArguments("include=foo/javalinux,foo/javalinux/testedby#exclude=foo/javalinux/testedby/metadata#classesExclude=Foo");
	assertTrue(Agent.isInstrumentationRequired("foo/javalinux/MyClass"));
	assertTrue(Agent.isInstrumentationRequired("foo/javalinux/testedby/MyClass"));
	assertFalse(Agent.isInstrumentationRequired("foo/javalinux/testedby/metadata/MyClass"));
	assertFalse(Agent.isInstrumentationRequired("it/javalinux/testedby/metadata/MyClass")); //restricted
	assertFalse(Agent.isInstrumentationRequired("foo/javalinux/Foo"));
    }
    
    
}
