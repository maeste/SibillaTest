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
import it.javalinux.testedby.metadata.ClassLinkMetadata;
import it.javalinux.testedby.metadata.Metadata;
import it.javalinux.testedby.metadata.StatusMetadata;
import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.builder.MetaDataBuilder;
import it.javalinux.testedby.metadata.impl.Helper;
import it.javalinux.testedby.metadata.impl.MetadataRepository;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
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

    private boolean onlyValidLink;

    public TestsMetadata build(Collection<Class<?>> classesUnderTest, Collection<ClassLinkMetadata> testClassesMetadata, Collection<Class<?>> testClasses) {
	if (testClasses == null) {
	    testClasses = new LinkedList<Class<?>>();
	}
	if (testClassesMetadata != null) {
	    for (ClassLinkMetadata classLink : testClassesMetadata) {
		try {
		    testClasses.add(Thread.currentThread().getContextClassLoader().loadClass(classLink.getClazz()));
		} catch (Exception e) {
		    e.printStackTrace();
		    // ignore
		}
	    }
	}

	return this.build(classesUnderTest, testClasses);
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.builder.MetaDataBuilder#build(Collection,
     *      Collection)
     */
    public TestsMetadata build(Collection<Class<?>> classesUnderTest, Collection<Class<?>> testClasses) {
	return this.build(classesUnderTest, testClasses, false);
    }

    public TestsMetadata build(Collection<Class<?>> classesUnderTest, Collection<Class<?>> testClasses, boolean onlyValid) {
	this.considerOnlyValidLink(onlyValid);
	Map<String, Class<?>> testClassesMap = new HashMap<String, Class<?>>();
	for (Class<?> clazz : testClasses) {
	    testClassesMap.put(clazz.getCanonicalName(), clazz);
	}
	return buildFromClassUnderTest(classesUnderTest, testClassesMap);

    }

    /**
     * internal method. Keeping it package protected for test purpose
     * 
     * @param classesUnderTest
     * @param testClasses
     * @return application metadata collected only from class under test
     */
    private TestsMetadata buildFromClassUnderTest(Collection<Class<?>> classesUnderTest, Map<String, Class<?>> testClasses) {
	MetadataRepository repository = new MetadataRepository();

	if (classesUnderTest != null) {
	    for (Class<?> clazzUnderTest : classesUnderTest) {
		repository = createTestClassMetadatas(testClasses, clazzUnderTest, repository, null);

		for (Method methodUnderTest : clazzUnderTest.getMethods()) {
		    repository = createTestClassMetadatasForMethod(testClasses, clazzUnderTest, methodUnderTest, repository, null);
		}
	    }
	}

	return repository;
    }

    /**
     * @param testClasses
     * @param clazzUnderTest
     * @param methodUnderTest
     * @param repository
     * @param originalClassUnderTest
     * @return {@link Metadata} for this method defined in this class or in its
     *         super classes and interfaces
     */
    private MetadataRepository createTestClassMetadatasForMethod(Map<String, Class<?>> testClasses, Class<?> clazzUnderTest, Method methodUnderTest, MetadataRepository repository, Class<?> originalClassUnderTest) {
	List<TestedBy> listOfTestedByOnMethod = createListOfTestedBy(methodUnderTest);
	for (TestedBy testedByOnMethod : listOfTestedByOnMethod) {
	    if (originalClassUnderTest == null) {
		repository = createTestClassMetadata(testClasses, clazzUnderTest, testedByOnMethod, methodUnderTest, repository, null, clazzUnderTest.isInterface() || Modifier.isAbstract(clazzUnderTest.getModifiers()));
	    } else {
		repository = createTestClassMetadata(testClasses, originalClassUnderTest, testedByOnMethod, methodUnderTest, repository, clazzUnderTest, originalClassUnderTest.isInterface() || Modifier.isAbstract(originalClassUnderTest.getModifiers()));
	    }

	}
	for (Class<?> interfaceUnderTest : clazzUnderTest.getInterfaces()) {
	    try {
		Method methodOnInterfcace = interfaceUnderTest.getMethod(methodUnderTest.getName(), methodUnderTest.getParameterTypes());
		repository = createTestClassMetadatasForMethod(testClasses, interfaceUnderTest, methodOnInterfcace, repository, clazzUnderTest);
	    } catch (NoSuchMethodException e) {

	    }
	}
	if (clazzUnderTest.getSuperclass() != null && !Helper.isInJVMPackage(clazzUnderTest.getSuperclass())) {
	    try {
		Method methodOnSuperClass = clazzUnderTest.getSuperclass().getMethod(methodUnderTest.getName(), methodUnderTest.getParameterTypes());
		repository = createTestClassMetadatasForMethod(testClasses, clazzUnderTest.getSuperclass(), methodOnSuperClass, repository, clazzUnderTest);
	    } catch (NoSuchMethodException e) {

	    }
	}

	return repository;
    }

    /**
     * @param testClasses
     * @param clazzUnderTest
     * @param repository
     * @param originalClassUnderTest
     * @return {@link Metadata} for this class and its super classes and
     *         interfaces
     */
    private MetadataRepository createTestClassMetadatas(Map<String, Class<?>> testClasses, Class<?> clazzUnderTest, MetadataRepository repository, Class<?> originalClassUnderTest) {
	List<TestedBy> listOfTestedBy = createListOfTestedBy(clazzUnderTest);
	for (TestedBy testedBy : listOfTestedBy) {
	    if (originalClassUnderTest == null) {
		repository = createTestClassMetadata(testClasses, clazzUnderTest, testedBy, null, repository, null, clazzUnderTest.isInterface() || Modifier.isAbstract(clazzUnderTest.getModifiers()) );
	    } else {
		repository = createTestClassMetadata(testClasses, originalClassUnderTest, testedBy, null, repository, clazzUnderTest, originalClassUnderTest.isInterface() || Modifier.isAbstract(originalClassUnderTest.getModifiers()));
	    }
	}
	for (Class<?> interfaceUnderTest : clazzUnderTest.getInterfaces()) {
	    repository = createTestClassMetadatas(testClasses, interfaceUnderTest, repository, originalClassUnderTest != null ? originalClassUnderTest : clazzUnderTest);
	}
	if (clazzUnderTest.getSuperclass() != null && !Helper.isInJVMPackage(clazzUnderTest.getSuperclass())) {
	    repository = createTestClassMetadatas(testClasses, clazzUnderTest.getSuperclass(), repository, originalClassUnderTest != null ? originalClassUnderTest : clazzUnderTest);
	}

	return repository;
    }

    /**
     * @param testClasses
     * @param clazz
     * @param testedBy
     * @param methodUnderTest
     * @param repository
     * @param upperMostClassInHierarchyDefiningThisMetadata
     * @param onAbstract
     * @return generated testClassMetadata
     */
    private MetadataRepository createTestClassMetadata(Map<String, Class<?>> testClasses, Class<?> clazz, TestedBy testedBy, Method methodUnderTest, MetadataRepository repository, Class<?> upperMostClassInHierarchyDefiningThisMetadata, boolean onAbstract) {
	String methodUTName;
	String[] methodUTParameters;

	if (methodUnderTest == null) {
	    methodUTName = null;
	    methodUTParameters = new String[0];
	} else {
	    methodUTName = methodUnderTest.getName();
	    List<String> l = new LinkedList<String>();
	    for (Class<?> c : methodUnderTest.getParameterTypes()) {
		l.add(c.getName());
	    }
	    if (l.isEmpty()) {
		methodUTParameters = new String[0];
	    } else {
		methodUTParameters = l.toArray(new String[l.size()]);
	    }
	}
	String testClassName = fixPackageTestClass(testedBy.testClass(), upperMostClassInHierarchyDefiningThisMetadata != null ? upperMostClassInHierarchyDefiningThisMetadata.getPackage() : clazz.getPackage());
	StatusMetadata status = new StatusMetadata();
	status.setValid(validateTestedByAnnotation(testClasses, testClassName, testedBy.testMethod()));
	status.setFromAnnotation(true);
	status.setUpperMostClassInHierarchyDefiningThisMetadata(upperMostClassInHierarchyDefiningThisMetadata);
	status.setOnAbstract(onAbstract);
	String[] testMethodsNames = createTestMethodsNameList(testClasses, testClassName, testedBy.testMethod());
	if (testMethodsNames.length == 0 && !isOnlyValidLinkConsidered()) {
	    repository.addConnection(testClassName, null, null, clazz.getCanonicalName(), methodUTName, methodUTParameters, status);
	} else {
	    for (String testMethodName : testMethodsNames) {
		if (isOnlyValidLinkConsidered() && !status.isValid()) {
		    // skip
		} else {
		    repository.addConnection(testClassName, testMethodName, null, clazz.getCanonicalName(), methodUTName, methodUTParameters, status);
		}

	    }
	}
	return repository;
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
     * @return true if testedby annotation point an existing method decorated
     *         with Test annotation. If no method name specified it veify only
     *         the testedby class name is present in testClass Map passed
     */

    private boolean validateTestedByAnnotation(Map<String, Class<?>> testClasses, String testedByTestClassName, String testedByMethodName) {
	Class<?> testClazz;
	if ((testClazz = testClasses.get(testedByTestClassName)) != null) {
	    if (testedByMethodName == null || testedByMethodName.trim().length() == 0) {
		return true;
	    }
	    try {
		Method testMethod = testClazz.getMethod(testedByMethodName);
		return (testMethod.getAnnotation(Test.class) != null);
	    } catch (NoSuchMethodException e) {
		return false;
	    }
	}
	return false;
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

    /**
     * @return onlyValidLink
     */
    private synchronized boolean isOnlyValidLinkConsidered() {
	return onlyValidLink;
    }

    /**
     * @param onlyValidLink
     *            Sets onlyValidLink to the specified value.
     */
    private synchronized void considerOnlyValidLink(boolean onlyValidLink) {
	this.onlyValidLink = onlyValidLink;
    }

}
