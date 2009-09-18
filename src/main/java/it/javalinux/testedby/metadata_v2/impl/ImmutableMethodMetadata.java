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
package it.javalinux.testedby.metadata_v2.impl;

import it.javalinux.testedby.metadata_v2.MethodMetadata;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * An immutable method metadata that can be built from both plain strings and a
 * java.lang.reflect.Method instance.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 23-Aug-2009
 * 
 */
public class ImmutableMethodMetadata implements MethodMetadata {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final String[] parameterTypes;

    public ImmutableMethodMetadata(String name, String[] parameterTypes) {
	this.name = name;
	if (parameterTypes == null) {
	    this.parameterTypes = new String[0];
	} else {
	    this.parameterTypes = parameterTypes;
	}
    }

    public ImmutableMethodMetadata(Method method) {
	this.name = method.getName();
	List<String> l = new LinkedList<String>();
	for (Class<?> c : method.getParameterTypes()) {
	    l.add(c.getName());
	}
	if (l.isEmpty()) {
	    this.parameterTypes = new String[0];
	} else {
	    this.parameterTypes = l.toArray(new String[l.size()]);
	}
    }

    public String getName() {
	return name;
    }

    public String[] getParameterTypes() {
	return parameterTypes;
    }

    @Override
    public boolean equals(Object o) {
	if (o == null || !(o instanceof MethodMetadata)) {
	    return false;
	}
	MethodMetadata obj = (MethodMetadata) o;
	// name check
	boolean nameCheck;
	if (name == null) {
	    if (obj.getName() != null) {
		nameCheck = false;
	    } else {
		nameCheck = true;
	    }
	} else {
	    nameCheck = name.equals(obj.getName());
	}
	return nameCheck && Arrays.deepEquals(parameterTypes, obj.getParameterTypes());
    }

    @Override
    public int hashCode() {
	if (name == null) {
	    return 0;
	}
	return 31 * (name.hashCode() + Arrays.deepHashCode(parameterTypes));
    }
}