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

import it.javalinux.testedby.annotations.TestedBy;
import it.javalinux.testedby.annotations.TestedByList;
import it.javalinux.testedby.metadata.ClassUnderTestMetadata;
import it.javalinux.testedby.metadata.TestMetadataMergingList;
import it.javalinux.testedby.metadata.MethodUnderTestMetadata;
import it.javalinux.testedby.metadata.builder.MetaDataBuilder;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableClassUnderTestMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableMethodMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableMethodUnderTestMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestClassMetadata;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stefano Maestri
 * 
 */
public class AnnotationBasedMetadataBuilder implements MetaDataBuilder {

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.builder.MetaDataBuilder#build(Collection,
     *      Collection)
     */
    public Map<String, ClassUnderTestMetadata> build(Collection<Class<?>> classesUnderTest, Collection<Class<?>> testClasses) {
	Map<String, Class<?>> testClassesMap = new HashMap<String, Class<?>>();
	for (Class<?> clazz : testClasses) {
	    testClassesMap.put(clazz.getCanonicalName(), clazz);
	}
	buildFromClassUnderTest(classesUnderTest, testClassesMap);
	return null;
    }

    /**
     * internal method. Keeping it package protected for test purpose
     * 
     * @param classesUnderTest
     * @param testClasses
     * @return application metadata collected only from class under test
     */
    /* package */Map<String, ClassUnderTestMetadata> buildFromClassUnderTest(Collection<Class<?>> classesUnderTest, Map<String, Class<?>> testClasses) {
	final Map<String, ClassUnderTestMetadata> classUnderTestMetadatas = new HashMap<String, ClassUnderTestMetadata>();
	TestMetadataMergingList<ImmutableTestClassMetadata> testClassesMetadatas = new TestMetadataMergingList<ImmutableTestClassMetadata>();
	TestMetadataMergingList<ImmutableTestClassMetadata> testClassesMetadatasForMethods = new TestMetadataMergingList<ImmutableTestClassMetadata>();
	Map<String, MethodUnderTestMetadata> methodSpecicMetadatas = new HashMap<String, MethodUnderTestMetadata>();

	for (Class<?> clazz : classesUnderTest) {
	    testClassesMetadatas.clear();
	    methodSpecicMetadatas.clear();
	    TestedByList tbList = clazz.getAnnotation(TestedByList.class);
	    List<TestedBy> listOfTestedBy = Arrays.asList(tbList.value());
	    listOfTestedBy.add(clazz.getAnnotation(TestedBy.class));
	    for (TestedBy testedBy : listOfTestedBy) {
		boolean valid = validateTestedByAnnotation(testClasses, testedBy);
		testClassesMetadatas.add(new ImmutableTestClassMetadata(valid, testedBy.testClass(), testedBy.testMethod()));
	    }
	    ClassUnderTestMetadata classUnderTestMetadata = new ImmutableClassUnderTestMetadata(clazz.getCanonicalName(), testClassesMetadatas, methodSpecicMetadatas);

	    for (Method methodUnderTest : classesUnderTest.getClass().getMethods()) {
		testClassesMetadatasForMethods.clear();
		TestedByList tbOnMethodList = methodUnderTest.getAnnotation(TestedByList.class);
		List<TestedBy> listOfTestedByOnMethod = Arrays.asList(tbOnMethodList.value());
		listOfTestedByOnMethod.add(methodUnderTest.getAnnotation(TestedBy.class));
		for (TestedBy testedByOnMethod : listOfTestedByOnMethod) {
		    boolean valid = validateTestedByAnnotation(testClasses, testedByOnMethod);
		    testClassesMetadatasForMethods.add(new ImmutableTestClassMetadata(valid, testedByOnMethod.testClass(), testedByOnMethod.testMethod()));
		}
		methodSpecicMetadatas.put(methodUnderTest.getName(), new ImmutableMethodUnderTestMetadata(new ImmutableMethodMetadata(methodUnderTest), classUnderTestMetadata, testClassesMetadatasForMethods));
	    }
	    classUnderTestMetadatas.put(classUnderTestMetadata.getClassUnderTestName(), classUnderTestMetadata);
	}

	return classUnderTestMetadatas;
    }

    /**
     * @param testClasses
     * @param testedBy
     * @return //TODO
     */

    boolean validateTestedByAnnotation(Map<String, Class<?>> testClasses, TestedBy testedBy) {
	Class<?> testClazz;
	if ((testClazz = testClasses.get(testedBy.testClass())) != null) {
	    if (testedBy.testMethod() == null) {
		return true;
	    } else {
		try {
		    testClazz.getMethod(testedBy.testMethod());
		    return true;
		} catch (NoSuchMethodException e) {
		    return false;
		}
	    }

	} else {
	    return false;
	}
    }

    /* package */Map<String, ClassUnderTestMetadata> buildFromTestClasses(Collection<Class<?>> testClasses) {
	// not yet implemented
	return null;
    }

}
