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
package it.javalinux.testedby.metadata.impl.immutable;

import it.javalinux.testedby.metadata.TestClassMetadata;
import it.javalinux.testedby.metadata.TestMethodMetadata;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public class ImmutableTestClassMetadata extends TestClassMetadata {

    private static final long serialVersionUID = 1L;

    private final Collection<TestMethodMetadata> methodSpecificMetadatas = new LinkedList<TestMethodMetadata>();

    private final String testClassName;

    private final boolean valid;

    /**
     * 
     * @param valid
     * @param testClassName
     * @param methodSpecificMetadatas
     */
    public ImmutableTestClassMetadata(boolean valid, String testClassName, Collection<TestMethodMetadata> methodSpecificMetadatas) {
	super();
	this.testClassName = testClassName;
	this.methodSpecificMetadatas.addAll(methodSpecificMetadatas);
	this.valid = valid;
    }

    public ImmutableTestClassMetadata(boolean valid, String testClassName, String methodName) {
	super();
	this.testClassName = testClassName;
	TestMethodMetadata testMethodMetadata = new ImmutableTestMethodMetadata(methodName, this);
	this.methodSpecificMetadatas.add(testMethodMetadata);
	this.valid = valid;
    }

    public ImmutableTestClassMetadata(boolean valid, String testClassName, String... methodNames) {
	super();
	this.testClassName = testClassName;
	for (String methodName : methodNames) {
	    TestMethodMetadata testMethodMetadata = new ImmutableTestMethodMetadata(methodName, this);
	    this.methodSpecificMetadatas.add(testMethodMetadata);
	}
	this.valid = valid;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.TestClassMetadata#getMethodsSpecificMetaDatas()
     */
    @Override
    public Collection<TestMethodMetadata> getMethodsSpecificMetaDatas() {
	LinkedList<TestMethodMetadata> list = new LinkedList<TestMethodMetadata>();
	list.addAll(methodSpecificMetadatas);
	return list;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.TestClassMetadata#getTestClassName()
     */
    @Override
    public String getTestClassName() {
	return testClassName;
    }

    /**
     * @return valid
     */
    @Override
    public boolean isValid() {
	return valid;
    }

}
