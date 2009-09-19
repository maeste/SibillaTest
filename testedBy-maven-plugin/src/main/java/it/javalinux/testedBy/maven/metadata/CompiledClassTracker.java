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
package it.javalinux.testedBy.maven.metadata;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author "Luca Perfetti"
 * 
 */
public class CompiledClassTracker {
    private static CompiledClassTracker me = null;

    private List<String> sourceFiles = new LinkedList<String>();

    public static CompiledClassTracker getInstance() {
	if (me == null) {
	    me = new CompiledClassTracker();
	}
	return me;
    }

    private CompiledClassTracker() {
    }

    public void addCompiledClass(String[] sourceClasses) {
	if (sourceClasses != null && sourceClasses.length > 0) {
	    sourceFiles.addAll(Arrays.asList(sourceClasses));
	}

    }

    /**
     * @return sourceFiles
     */
    public List<String> getSourceFiles() {
	return sourceFiles;
    }

}
