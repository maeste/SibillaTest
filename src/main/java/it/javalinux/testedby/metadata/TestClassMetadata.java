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
package it.javalinux.testedby.metadata;

import it.javalinux.testedby.metadata.builder.MetaDataBuilder;

import java.util.Collection;

/**
 * It represent metadatas for a Test class. It contains both its own metadata
 * (accessed by {@link #getTestClassName()} ) and a Colection of
 * {@link TestMethodMetadata} (accessed by
 * {@link #getMethodsSpecificMetaDatas()})
 * 
 * Note that this metadata doesn't represent necessarily the test class
 * completely, but only methods that insist on a class under test. In fact this
 * metadata are created during class (and methods) under test metadata
 * collection. In other word we can have multiple {@link TestClassMetadata} for
 * a single real test class, containing different method metadatas for method
 * stressing different class under test.
 * 
 * i.e
 * 
 * <pre>
 *  Class firstClass {
 *  	&#064;TestedBy(class=&quot;testClass&quot; method=&quot;testMethodOne&quot;)
 *  	public doSMomething() {
 *  		[...]
 *  	}
 *  }
 *  
 *  Class secondClass {
 *  	&#064;TestedBy(class=&quot;testClass&quot; method=&quot;testMethodTwo&quot;)
 *  	public doSMomething() {
 *  		[...]
 *  	}
 *  }
 *  
 *  Class testClass {
 *  	public testMethodOne() {
 *  		[...]
 *  	}
 *  	public testMethodTwo() {
 *  		[...]
 *  	}
 *  }
 * </pre>
 * 
 * In this case we will have 2 {@link TestClassMetadata}: one containing only
 * testMethodOne {@link TestMethodMetadata} pointed by
 * {@link ClassUnderTestMetadata} representing firstClass; and one containing
 * only testMethodTwo {@link TestMethodMetadata} pointed by
 * {@link ClassUnderTestMetadata} representing secondClass. Of course client
 * could decide to merge them using its own policy. It's out of the scope of
 * this interface to define merging policy.
 * 
 * 
 * Metadata will be created during {@link ClassUnderTestMetadata} or
 * {@link MethodUnderTestMetadata} creation by a {@link MetaDataBuilder} with a
 * specific strategy
 * 
 * @author stefano.maestri@javalinux.it
 * 
 */
public abstract class TestClassMetadata implements Metadata {

    private static final long serialVersionUID = 1L;

    /**
     * t
     * 
     * @return the test full qualified name
     */
    public abstract String getTestClassName();

    /**
     * 
     * @return a Colection of {@link TestMethodMetadata} representing metadata
     *         specific of methods of test class
     */
    public abstract Collection<TestMethodMetadata> getMethodsSpecificMetaDatas();

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (!(TestClassMetadata.class.isAssignableFrom(obj.getClass()))) {
	    return false;
	}
	if (this.getTestClassName() == null) {
	    return super.equals(obj);
	}
	return this.getTestClassName().equals(((TestClassMetadata) obj).getTestClassName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	if (this.getTestClassName() == null) {
	    return super.hashCode();
	}
	return this.getTestClassName().hashCode();
    }

    /**
     * @return valid
     */
    public abstract boolean isValid();
}
