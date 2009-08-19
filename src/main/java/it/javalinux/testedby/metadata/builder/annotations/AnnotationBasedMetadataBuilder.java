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
package it.javalinux.testedby.metadata.builder.annotations;

import it.javalinux.testedby.annotations.Stressing;
import it.javalinux.testedby.annotations.StressingList;
import it.javalinux.testedby.annotations.TestedBy;
import it.javalinux.testedby.annotations.TestedByList;
import it.javalinux.testedby.metadata.ClassUnderTestMetadata;
import it.javalinux.testedby.metadata.builder.MetaDataBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stefano Maestri
 *
 */
public class AnnotationBasedMetadataBuilder  implements MetaDataBuilder {

    /**
     * {@inheritDoc}
     *
     * @see it.javalinux.testedby.metadata.builder.MetaDataBuilder#build(Collection, Collection)
     */
    public Map<String, ClassUnderTestMetadata> build(Collection<Class<?>> classesUnderTest, Collection<Class<?>> testClasses) {
	
	buildFromClassUnderTest(classesUnderTest);
	return null;
    }

    /**
     * internal method. Keeping it package protected for test purpose
     * @param classesUnderTest
     * @return application metadata collected only from class under test
     */
    /*package*/ Map<String, ClassUnderTestMetadata> buildFromClassUnderTest(Collection<Class<?>> classesUnderTest) {
	final Map<String, ClassUnderTestMetadata> classUnderTestMetadatas= new HashMap<String, ClassUnderTestMetadata>();
	
	for (Class<?> clazz : classesUnderTest) {
	    TestedByList tbList= clazz.getAnnotation(TestedByList.class);
	    List<TestedBy> listOfTestedBy = Arrays.asList(tbList.value());
	    listOfTestedBy.add(clazz.getAnnotation(TestedBy.class));
	    //ClassUnderTestMetadata classUnderTestMetadata = 
	}
	
	return classUnderTestMetadatas;
    }
    
    
    /*package*/ Map<String, ClassUnderTestMetadata> buildFromTestClasses(Collection<Class<?>> testClasses) {
	final Map<String, ClassUnderTestMetadata> classUnderTestMetadatas= new HashMap<String, ClassUnderTestMetadata>();
	
	for (Class<?> clazz : testClasses) {
	    StressingList stList= clazz.getAnnotation(StressingList.class);
	    List<Stressing> listOfStressing = Arrays.asList(stList.value());
	    listOfStressing.add(clazz.getAnnotation(Stressing.class));
	    //ClassUnderTestMetadata classUnderTestMetadata = 
	}
	
	return classUnderTestMetadatas;
    }


    
    
}
