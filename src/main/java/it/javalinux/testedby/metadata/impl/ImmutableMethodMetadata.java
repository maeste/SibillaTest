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
package it.javalinux.testedby.metadata.impl;

import it.javalinux.testedby.metadata.MethodMetadata;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;

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
    
    private final boolean isConstructor;
    
    private final String[] parameterTypes;
    
    /**
     * Constructor for empty ImmutableMethodMetadata
     * 
     */
    public ImmutableMethodMetadata() {
	this(null, null, null);
    }

    /**
     * Standard constructor
     * 
     * @param className		The full name of the class the specified method belongs to
     * @param name		The method name
     * @param parameterTypes	The parameter types
     */
    public ImmutableMethodMetadata(String className, String name, String[] parameterTypes) {
	this.name = name;
	this.isConstructor = (className != null && (className.equals(name) || className.endsWith("." + name)));
	if (parameterTypes == null) {
	    this.parameterTypes = new String[0];
	} else {
	    this.parameterTypes = parameterTypes;
	}
    }

    /**
     * A convenience constructor using informations coming from either a java.lang.reflect.Method or a java.lang.reflect.Constructor
     * 
     * @param member	The member instance
     */
    ImmutableMethodMetadata(Member member) {
	this.name = member.getName();
	if (member instanceof Constructor<?>) {
	    this.isConstructor = true;
	    this.parameterTypes = Helper.getParameterTypesAsStringArray((Constructor<?>)member);
	} else if (member instanceof Method) {
	    this.isConstructor = false;
	    this.parameterTypes = Helper.getParameterTypesAsStringArray((Method)member);
	} else {
	    throw new IllegalArgumentException("Fields not supported");
	}
    }
    
    /**
     * A constructor using informations coming from a java.lang.reflect.Constructor
     * 
     * @param constructor	The constructor instance
     */
    public ImmutableMethodMetadata(Constructor<?> constructor) {
	this.name = constructor.getName();
	this.isConstructor = true;
	this.parameterTypes = Helper.getParameterTypesAsStringArray(constructor);
    }
    
    /**
     * A constructor using informations coming from a java.lang.reflect.Method
     * 
     * @param method	The method instance
     */
    public ImmutableMethodMetadata(Method method) {
	this.name = method.getName();
	this.isConstructor = false;
	this.parameterTypes = Helper.getParameterTypesAsStringArray(method);
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
    
    @Override
	public String toString() {
		return "ImmutableMethodMetadata [name=" + name + ", isConstructor="
				+ isConstructor + ", parameterTypes="
				+ parameterTypes == null ? "" : Arrays.toString(parameterTypes) + "]";
	}

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata.MethodMetadata#isConstructor()
     */
    public boolean isConstructor() {
	return isConstructor;
    }
}