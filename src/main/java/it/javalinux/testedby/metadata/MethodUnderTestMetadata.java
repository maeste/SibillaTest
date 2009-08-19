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
 * It represent metadatas for method under test. It contains its own metadata
 * (accessed by {@link #getTestClassesMetadatas()}  )
 * 
 * It aims to collect metadata to make possible navigation of relation between
 * method under test and test classes/methods using method under test as
 * starting point.
 * 
 * Metadata will be created by a {@link MetaDataBuilder} with a specific
 * strategy
 * 
 * @author stefano.maestri@javalinux.it
 * 
 */
public interface MethodUnderTestMetadata extends CodeUnderTestMetadata {

	/**
	 * 
	 * @return the full qualified name of method under test
	 */
	public String getMethodUnderTestName();

	/**
	 * 
	 * @return a {@link MethodLineNumber} representing starting and ending
	 *         linenumber for method under test. It is useful to test runner to
	 *         identify which are method changed in a class under test and so
	 *         which are test method to run
	 */
	public MethodLineNumber getLineNumbers();

	public class MethodLineNumber {
		private final int startLine;

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

		private final int endLine;

	}

}
