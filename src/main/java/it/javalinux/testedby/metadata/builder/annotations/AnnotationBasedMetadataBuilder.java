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
import it.javalinux.testedby.metadata.TestClassMetadata;
import it.javalinux.testedby.metadata.TestMetadataMergingList;
import it.javalinux.testedby.metadata.MethodUnderTestMetadata;
import it.javalinux.testedby.metadata.builder.MetaDataBuilder;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableClassUnderTestMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableMethodMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableMethodUnderTestMetadata;
import it.javalinux.testedby.metadata.impl.immutable.ImmutableTestClassMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

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
	return this.build(classesUnderTest, testClasses, false);
    }

    public Map<String, ClassUnderTestMetadata> build(Collection<Class<?>> classesUnderTest, Collection<Class<?>> testClasses, boolean onlyValid) {
	Map<String, Class<?>> testClassesMap = new HashMap<String, Class<?>>();
	for (Class<?> clazz : testClasses) {
	    testClassesMap.put(clazz.getCanonicalName(), clazz);
	}
	return buildFromClassUnderTest(classesUnderTest, testClassesMap, onlyValid);

    }

    /**
     * internal method. Keeping it package protected for test purpose
     * 
     * @param classesUnderTest
     * @param testClasses
     * @param onlyValid
     * @return application metadata collected only from class under test
     */
    private Map<String, ClassUnderTestMetadata> buildFromClassUnderTest(Collection<Class<?>> classesUnderTest, Map<String, Class<?>> testClasses, boolean onlyValid) {
	final Map<String, ClassUnderTestMetadata> classUnderTestMetadatas = new HashMap<String, ClassUnderTestMetadata>();
	TestMetadataMergingList<ImmutableTestClassMetadata> testClassesMetadatas = new TestMetadataMergingList<ImmutableTestClassMetadata>(onlyValid);
	TestMetadataMergingList<ImmutableTestClassMetadata> testClassesMetadatasForMethods = new TestMetadataMergingList<ImmutableTestClassMetadata>(onlyValid);
	Map<String, MethodUnderTestMetadata> methodSpecicMetadatas = new HashMap<String, MethodUnderTestMetadata>();

	for (Class<?> clazzUnderTest : classesUnderTest) {
	    testClassesMetadatas.clear();
	    methodSpecicMetadatas.clear();
	    testClassesMetadatas.addAll(createTestClassMetadatas(testClasses, clazzUnderTest));
	    ClassUnderTestMetadata classUnderTestMetadata = new ImmutableClassUnderTestMetadata(clazzUnderTest.getCanonicalName(), testClassesMetadatas, methodSpecicMetadatas);

	    for (Method methodUnderTest : clazzUnderTest.getMethods()) {
		testClassesMetadatasForMethods.clear();
		testClassesMetadatasForMethods.addAll(createTestClassMetadatasForMethod(testClasses, clazzUnderTest, methodUnderTest));
		methodSpecicMetadatas.put(methodUnderTest.getName(), new ImmutableMethodUnderTestMetadata(new ImmutableMethodMetadata(methodUnderTest), classUnderTestMetadata, testClassesMetadatasForMethods));
	    }
	    classUnderTestMetadata = new ImmutableClassUnderTestMetadata(clazzUnderTest.getCanonicalName(), testClassesMetadatas, methodSpecicMetadatas);

	    classUnderTestMetadatas.put(classUnderTestMetadata.getClassUnderTestName(), classUnderTestMetadata);
	}

	return classUnderTestMetadatas;
    }

    /**
     * @param testClasses
     * @param clazzUnderTest
     * @param methodUnderTest
     * @return {@link TestMetadataMergingList} for this method defined in this
     *         class or in its super classes and interfaces
     */
    private TestMetadataMergingList<ImmutableTestClassMetadata> createTestClassMetadatasForMethod(Map<String, Class<?>> testClasses, Class<?> clazzUnderTest, Method methodUnderTest) {
	TestMetadataMergingList<ImmutableTestClassMetadata> testClassesMetadatasForMethods = new TestMetadataMergingList<ImmutableTestClassMetadata>();
	List<TestedBy> listOfTestedByOnMethod = createListOfTestedBy(methodUnderTest);
	for (TestedBy testedByOnMethod : listOfTestedByOnMethod) {
	    testClassesMetadatasForMethods.add(createImmutableTestClassMetadata(testClasses, clazzUnderTest, testedByOnMethod));
	}
	for (Class<?> interfaceUnderTest : clazzUnderTest.getInterfaces()) {
	    try {
		Method methodOnInterfcace = interfaceUnderTest.getMethod(methodUnderTest.getName(), methodUnderTest.getParameterTypes());
		testClassesMetadatasForMethods.addAll(createTestClassMetadatasForMethod(testClasses, interfaceUnderTest, methodOnInterfcace));
	    } catch (NoSuchMethodException e) {

	    }
	}
	if (clazzUnderTest.getSuperclass() != null && clazzUnderTest.getSuperclass().getPackage() != null && !clazzUnderTest.getSuperclass().getPackage().toString().startsWith("java")) {
	    try {
		Method methodOnSuperClass = clazzUnderTest.getSuperclass().getMethod(methodUnderTest.getName(), methodUnderTest.getParameterTypes());
		testClassesMetadatasForMethods.addAll(createTestClassMetadatasForMethod(testClasses, clazzUnderTest.getSuperclass(), methodOnSuperClass));
	    } catch (NoSuchMethodException e) {

	    }
	}

	return testClassesMetadatasForMethods;
    }

    /**
     * @param testClasses
     * @param clazzUnderTest
     * @return {@link TestMetadataMergingList} for this class and its super
     *         classes and interfaces
     */
    private TestMetadataMergingList<ImmutableTestClassMetadata> createTestClassMetadatas(Map<String, Class<?>> testClasses, Class<?> clazzUnderTest) {
	TestMetadataMergingList<ImmutableTestClassMetadata> testClassesMetadatas = new TestMetadataMergingList<ImmutableTestClassMetadata>();
	List<TestedBy> listOfTestedBy = createListOfTestedBy(clazzUnderTest);
	for (TestedBy testedBy : listOfTestedBy) {
	    testClassesMetadatas.add(createImmutableTestClassMetadata(testClasses, clazzUnderTest, testedBy));
	}
	for (Class<?> interfaceUnderTest : clazzUnderTest.getInterfaces()) {
	    testClassesMetadatas.addAll(createTestClassMetadatas(testClasses, interfaceUnderTest));
	}
	if (clazzUnderTest.getSuperclass() != null && clazzUnderTest.getSuperclass().getPackage() != null && !clazzUnderTest.getSuperclass().getPackage().toString().startsWith("java")) {
	    testClassesMetadatas.addAll(createTestClassMetadatas(testClasses, clazzUnderTest.getSuperclass()));
	}

	return testClassesMetadatas;
    }

    /**
     * @param testClasses
     * @param clazz
     * @param testedBy
     * @return generated testClassMetadata
     */
    private ImmutableTestClassMetadata createImmutableTestClassMetadata(Map<String, Class<?>> testClasses, Class<?> clazz, TestedBy testedBy) {
	String testClassName = fixPackageTestClass(testedBy.testClass(), clazz.getPackage());
	boolean valid = validateTestedByAnnotation(testClasses, testClassName, testedBy.testMethod());
	String[] testMethodsNames = createTestMethodsNameList(testClasses, testClassName, testedBy.testMethod());
	return new ImmutableTestClassMetadata(valid, testClassName, testMethodsNames);
    }

    /**
     * @param annotatedElement
     * @return the list of all testedby annotations put on this portion of code
     */
    private List<TestedBy> createListOfTestedBy(AnnotatedElement annotatedElement) {
	List<TestedBy> listOfTestedBy = new LinkedList<TestedBy>();

	for (Annotation annotation : annotatedElement.getAnnotations()) {
	    if (annotation instanceof TestedByList) {
		TestedByList tbList = annotatedElement.getAnnotation(TestedByList.class);
		if (tbList != null) {
		    listOfTestedBy.addAll(Arrays.asList(tbList.value()));
		}
	    }
	    if (annotation instanceof TestedBy) {
		listOfTestedBy.add(annotatedElement.getAnnotation(TestedBy.class));
	    }
	}

	return listOfTestedBy;
    }

    /**
     * @param testClasses
     * @param testedByTestClassName
     * @param testedByMethodName
     * @return //TODO
     */

    private boolean validateTestedByAnnotation(Map<String, Class<?>> testClasses, String testedByTestClassName, String testedByMethodName) {
	Class<?> testClazz;
	if ((testClazz = testClasses.get(testedByTestClassName)) != null) {
	    if (testedByMethodName == null || testedByMethodName.trim().length() == 0) {
		return true;
	    } else {
		try {
		    Method testMethod = testClazz.getMethod(testedByMethodName);
		    if (testMethod.getAnnotation(Test.class) != null) {
			return true;
		    } else {
			return false;
		    }

		} catch (NoSuchMethodException e) {
		    return false;
		}
	    }

	} else {
	    return false;
	}
    }

    private String[] createTestMethodsNameList(Map<String, Class<?>> testClasses, String testedByTestClassName, String testedByMethodName) {
	Class<?> testClazz;
	List<String> returnValue = new LinkedList<String>();
	if ((testClazz = testClasses.get(testedByTestClassName)) != null) {
	    if (testedByMethodName != null && testedByMethodName.trim().length() != 0) {
		returnValue.add(testedByMethodName);
	    } else {
		for (Method method : testClazz.getMethods()) {
		    if (method.getAnnotation(Test.class) != null) {
			returnValue.add(method.getName());
		    }
		}
	    }

	}
	return returnValue.toArray(new String[returnValue.size()]);
    }

    private String fixPackageTestClass(String className, Package packageName) {
	if (className == null || className.trim().length() == 0 || className.contains(".") || packageName == null) {
	    return className;
	}
	return packageName.getName() + "." + className;
    }

    /* package */Map<String, ClassUnderTestMetadata> buildFromTestClasses(Collection<Class<?>> testClasses) {
	// not yet implemented
	return null;
    }

}
