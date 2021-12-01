/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Helper for resolving synthetic {@link Method#isBridge bridge Methods} to the
 * {@link Method} being bridged.
 *
 * <p>Given a synthetic {@link Method#isBridge bridge Method} returns the {@link Method}
 * being bridged. A bridge method may be created by the compiler when extending a
 * parameterized type whose methods have parameterized arguments. During runtime
 * invocation the bridge {@link Method} may be invoked and/or used via reflection.
 * When attempting to locate annotations on {@link Method Methods}, it is wise to check
 * for bridge {@link Method Methods} as appropriate and find the bridged {@link Method}.
 *
 * <p>See <a href="http://java.sun.com/docs/books/jls/third_edition/html/expressions.html#15.12.4.5">
 * The Java Language Specification</a> for more details on the use of bridge methods.
 *
 * <p>Only usable on JDK 1.5 and higher. Use an appropriate {@link JdkVersion}
 * check before calling this class, if a fallback for JDK 1.3/1.4 is desirable.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see JdkVersion
 */
public abstract class BridgeMethodResolver {

	/**
	 * Find the original method for the supplied {@link Method bridge Method}.
	 * <p>It is safe to call this method passing in a non-bridge {@link Method} instance.
	 * In such a case, the supplied {@link Method} instance is returned directly to the caller.
	 * Callers are <strong>not</strong> required to check for bridging before calling this method.
	 * @throws IllegalStateException if no bridged {@link Method} can be found
	 */
	public static Method findBridgedMethod(Method bridgeMethod) {
		Assert.notNull(bridgeMethod, "Method must not be null");

		if (!bridgeMethod.isBridge()) {
			return bridgeMethod;
		}

		// Gather all methods with matching name and parameter size.
		List candidateMethods = new ArrayList();
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
		for (int i = 0; i < methods.length; i++) {
			Method candidateMethod = methods[i];
			if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
				candidateMethods.add(candidateMethod);
			}
		}

		Method result;
		// Now perform simple quick checks.
		if (candidateMethods.size() == 1) {
			result = (Method) candidateMethods.get(0);
		}
		else {
			result = searchCandidates(candidateMethods, bridgeMethod);
		}

		if (result == null) {
			throw new IllegalStateException(
					"Unable to locate bridged method for bridge method '" + bridgeMethod + "'");
		}

		return result;
	}

	/**
	 * Search for the bridged method in the given candidates.
	 * @param candidateMethods the List of candidate Methods
	 * @param bridgeMethod the bridge method
	 * @return the bridged method, or <code>null</code> if none found
	 */
	private static Method searchCandidates(List candidateMethods, Method bridgeMethod) {
		Map typeParameterMap = createTypeVariableMap(bridgeMethod.getDeclaringClass());
		for (int i = 0; i < candidateMethods.size(); i++) {
			Method candidateMethod = (Method) candidateMethods.get(i);
			if (isBridgeMethodFor(bridgeMethod, candidateMethod, typeParameterMap)) {
				return candidateMethod;
			}
		}
		return null;
	}

	/**
	 * Return <code>true</code> if the supplied '<code>candidateMethod</code>' can be
	 * consider a validate candidate for the {@link Method} that is {@link Method#isBridge() bridged}
	 * by the supplied {@link Method bridge Method}. This method performs inexpensive
	 * checks and can be used quickly filter for a set of possible matches.
	 */
	private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
		return (!candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) &&
				candidateMethod.getName().equals(bridgeMethod.getName()) &&
				candidateMethod.getParameterTypes().length == bridgeMethod.getParameterTypes().length);
	}

	/**
	 * Determine whether or not the bridge {@link Method} is the bridge for the
	 * supplied candidate {@link Method}.
	 */
	static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Map typeVariableMap) {
		if (isResolvedTypeMatch(candidateMethod, bridgeMethod, typeVariableMap)) {
			return true;
		}
		Method method = findGenericDeclaration(bridgeMethod);
		return (method != null ? isResolvedTypeMatch(method, candidateMethod, typeVariableMap) : false);
	}

	/**
	 * Search for the generic {@link Method} declaration whose erased signature
	 * matches that of the supplied bridge method.
	 * @throws IllegalStateException if the generic declaration cannot be found
	 */
	private static Method findGenericDeclaration(Method bridgeMethod) {
		// Search parent types for method that has same signature as bridge.
		Class superclass = bridgeMethod.getDeclaringClass().getSuperclass();
		while (!Object.class.equals(superclass)) {
			Method method = searchForMatch(superclass, bridgeMethod);
			if (method != null && !method.isBridge()) {
				return method;
			}
			superclass = superclass.getSuperclass();
		}

		// Search interfaces.
		Class[] interfaces = ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
		for (int i = 0; i < interfaces.length; i++) {
			Class anInterface = interfaces[i];
			Method method = searchForMatch(anInterface, bridgeMethod);
			if (method != null  && !method.isBridge()) {
				return method;
			}
		}

		return null;
	}

	/**
	 * Return <code>true</code> if the {@link Type} signature of both the supplied
	 * {@link Method#getGenericParameterTypes() generic Method} and concrete {@link Method}
	 * are equal after resolving all {@link TypeVariable TypeVariables} using the supplied
	 * {@link #createTypeVariableMap TypeVariable Map}, otherwise returns <code>false</code>.
	 */
	private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Map typeVariableMap) {
		Type[] genericParameters = genericMethod.getGenericParameterTypes();
		Class[] candidateParameters = candidateMethod.getParameterTypes();
		if (genericParameters.length != candidateParameters.length) {
			return false;
		}
		for (int i = 0; i < genericParameters.length; i++) {
			Type genericParameter = genericParameters[i];
			Class candidateParameter = candidateParameters[i];
			if (candidateParameter.isArray()) {
				// An array type: compare the component type.
				Type rawType = getRawType(genericParameter, typeVariableMap);
				if (rawType instanceof GenericArrayType) {
					if (!candidateParameter.getComponentType().equals(
							getRawType(((GenericArrayType) rawType).getGenericComponentType(), typeVariableMap))) {
						return false;
					}
					break;
				}
			}
			// A non-array type: compare the type itself.
			if (!candidateParameter.equals(getRawType(genericParameter, typeVariableMap))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determine the raw type for the given generic parameter type.
	 */
	private static Type getRawType(Type genericType, Map typeVariableMap) {
		if (genericType instanceof TypeVariable) {
			TypeVariable tv = (TypeVariable) genericType;
			Type result = (Type) typeVariableMap.get(tv);
			return (result != null ? result : Object.class);
		}
		else if (genericType instanceof ParameterizedType) {
			return ((ParameterizedType) genericType).getRawType();
		}
		else {
			return genericType;
		}
	}

	/**
	 * If the supplied {@link Class} has a declared {@link Method} whose signature matches
	 * that of the supplied {@link Method}, then this matching {@link Method} is returned,
	 * otherwise <code>null</code> is returned.
	 */
	private static Method searchForMatch(Class type, Method bridgeMethod) {
		return ReflectionUtils.findMethod(type, bridgeMethod.getName(), bridgeMethod.getParameterTypes());
	}

	/**
	 * Build a mapping of {@link TypeVariable#getName TypeVariable names} to concrete
	 * {@link Class} for the specified {@link Class}. Searches all super types,
	 * enclosing types and interfaces.
	 */
	static Map createTypeVariableMap(Class cls) {
		Map typeVariableMap = new HashMap();

		// interfaces
		extractTypeVariablesFromGenericInterfaces(cls.getGenericInterfaces(), typeVariableMap);

		// super class
		Type genericType = cls.getGenericSuperclass();
		Class type = cls.getSuperclass();
		while (!Object.class.equals(type)) {
			if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;
				populateTypeMapFromParameterizedType(pt, typeVariableMap);
			}
			extractTypeVariablesFromGenericInterfaces(type.getGenericInterfaces(), typeVariableMap);
			genericType = type.getGenericSuperclass();
			type = type.getSuperclass();
		}

		// enclosing class
		type = cls;
		while (type.isMemberClass()) {
			genericType = type.getGenericSuperclass();
			if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;
				populateTypeMapFromParameterizedType(pt, typeVariableMap);
			}
			type = type.getEnclosingClass();
		}

		return typeVariableMap;
	}

	private static void extractTypeVariablesFromGenericInterfaces(Type[] genericInterfaces, Map typeVariableMap) {
		for (int i = 0; i < genericInterfaces.length; i++) {
			Type genericInterface = genericInterfaces[i];
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericInterface;
				populateTypeMapFromParameterizedType(pt, typeVariableMap);
				if (pt.getRawType() instanceof Class) {
					extractTypeVariablesFromGenericInterfaces(
							((Class) pt.getRawType()).getGenericInterfaces(), typeVariableMap);
				}
			}
			else if (genericInterface instanceof Class) {
				extractTypeVariablesFromGenericInterfaces(
						((Class) genericInterface).getGenericInterfaces(), typeVariableMap);
			}
		}
	}


	/**
	 * Read the {@link TypeVariable TypeVariables} from the supplied {@link ParameterizedType}
	 * and add mappings corresponding to the {@link TypeVariable#getName TypeVariable name} ->
	 * concrete type to the supplied {@link Map}.
	 * <p>Consider this case:
	 * <pre class="code>
	 * public interface Foo<S, T> {
	 *  ..
	 * }
	 *
	 * public class FooImpl implements Foo<String, Integer> {
	 *  ..
	 * }</pre>
	 * For '<code>FooImpl</code>' the following mappings would be added to the {@link Map}:
	 * {S=java.lang.String, T=java.lang.Integer}.
	 */
	private static void populateTypeMapFromParameterizedType(ParameterizedType type, Map typeVariableMap) {
		if (type.getRawType() instanceof Class) {
			Type[] actualTypeArguments = type.getActualTypeArguments();
			TypeVariable[] typeVariables = ((Class) type.getRawType()).getTypeParameters();
			for (int i = 0; i < actualTypeArguments.length; i++) {
				Type actualTypeArgument = actualTypeArguments[i];
				TypeVariable variable = typeVariables[i];
				if (actualTypeArgument instanceof Class) {
					typeVariableMap.put(variable, actualTypeArgument);
				}
				else if (actualTypeArgument instanceof GenericArrayType) {
					typeVariableMap.put(variable, actualTypeArgument);
				}
				else if (actualTypeArgument instanceof ParameterizedType) {
					typeVariableMap.put(variable, ((ParameterizedType) actualTypeArgument).getRawType());
				}
				else if (actualTypeArgument instanceof TypeVariable) {
					// We have a type that is parameterized at instantiation time
					// the nearest match on the bridge method will be the bounded type.
					TypeVariable typeVariableArgument = (TypeVariable) actualTypeArgument;
					Type resolvedType = (Type) typeVariableMap.get(typeVariableArgument);
					if (resolvedType == null) {
						resolvedType = extractClassForTypeVariable(typeVariableArgument);
					}
					if (resolvedType != null) {
						typeVariableMap.put(variable, resolvedType);
					}
				}
			}
		}
	}

	/**
	 * Extracts the bound '<code>Class</code>' for a give {@link TypeVariable}.
	 */
	private static Class extractClassForTypeVariable(TypeVariable typeVariable) {
		Type[] bounds = typeVariable.getBounds();
		Type result = null;
		if (bounds.length > 0) {
			Type bound = bounds[0];
			if (bound instanceof ParameterizedType) {
				result = ((ParameterizedType) bound).getRawType();
			}
			else if (bound instanceof Class) {
				result = bound;
			}
			else if (bound instanceof TypeVariable) {
				result = extractClassForTypeVariable((TypeVariable) bound);
			}
		}
		return (result instanceof Class ? (Class) result : null);
	}

}
