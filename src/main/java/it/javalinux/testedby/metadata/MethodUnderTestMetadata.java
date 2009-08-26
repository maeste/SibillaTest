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

/**
 * It represents metadata for a method under test. It contains its own metadata
 * (accessed by {@link #getTestClassesMetadata()} )
 * 
 * It aims at collecting metadata to allow navigation of relationships between
 * method under test and test classes/methods using method under test as
 * starting point.
 * 
 * Metadata have to be created by a {@link MetaDataBuilder} with a specific
 * strategy.
 * 
 * @author stefano.maestri@javalinux.it
 * 
 */
public abstract class MethodUnderTestMetadata implements CodeUnderTestMetadata {

    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return the full qualified name of method under test
     */
    public abstract MethodMetadata getMethodUnderTest();

    /**
     * 
     * @return a {@link MethodLineNumber} representing starting and ending
     *         linenumber for method under test. It is useful to test runner to
     *         identify which are method changed in a class under test and so
     *         which are test method to run. In case line numbers aren't
     *         available it returns null (i.e if metadatas have been collected
     *         by annotations)
     */
    public abstract MethodLineNumber getLineNumbers();

    public abstract ClassUnderTestMetadata getClassUnderTestMetadata();

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof MethodUnderTestMetadata)) {
	    return false;
	}
	if (this.getMethodUnderTest() == null || this.getClassUnderTestMetadata() == null) {
	    return super.equals(obj);
	}
	return (this.getMethodUnderTest().equals(((MethodUnderTestMetadata) obj).getMethodUnderTest()) && this.getClassUnderTestMetadata().equals(((MethodUnderTestMetadata) obj).getClassUnderTestMetadata()));
    }

    @Override
    public String toString() {
	return "<[methodName=" + this.getMethodUnderTest().getName() + ", className=" + this.getClassUnderTestMetadata().getClassUnderTestName() + "]>";
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	if (this.getMethodUnderTest() == null || this.getClassUnderTestMetadata() == null) {
	    return super.hashCode();
	}
	return 31 * (this.getMethodUnderTest().hashCode() + this.getClassUnderTestMetadata().hashCode());
    }

    public class MethodLineNumber {
	private final int startLine;

	private final int endLine;

	/**
	 * @param startLine
	 * @param endLine
	 */
	public MethodLineNumber(int startLine, int endLine) {
	    super();
	    this.startLine = startLine;
	    this.endLine = endLine;
	}

	/**
	 * @return startLine
	 */
	public int getStartLine() {
	    return startLine;
	}

	/**
	 * @return endLine
	 */
	public int getEndLine() {
	    return endLine;
	}

    }

}
