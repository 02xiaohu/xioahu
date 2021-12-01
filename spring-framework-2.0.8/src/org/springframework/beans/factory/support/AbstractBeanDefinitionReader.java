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

package org.springframework.beans.factory.support;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

/**
 * Abstract base class for bean definition readers which implement
 * the {@link BeanDefinitionReader} interface.
 *
 * <p>Provides common properties like the bean factory to work on
 * and the class loader to use for loading bean classes.
 *
 * @author Juergen Hoeller
 * @since 11.12.2003
 * @see BeanDefinitionReaderUtils
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	protected final Log logger = LogFactory.getLog(getClass());

	private final BeanDefinitionRegistry beanFactory;

	private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	private ResourceLoader resourceLoader;

	private ClassLoader beanClassLoader;


	/**
	 * Create a new AbstractBeanDefinitionReader for the given bean factory.
	 * <p>If the passed-in bean factory does not only implement the BeanDefinitionRegistry
	 * interface but also the ResourceLoader interface, it will be used as default
	 * ResourceLoader as well. This will usually be the case for
	 * {@link org.springframework.context.ApplicationContext} implementations.
	 * <p>If given a plain BeanDefinitionRegistry, the default ResourceLoader will be a
	 * {@link org.springframework.core.io.support.PathMatchingResourcePatternResolver}.
	 * @param beanFactory the BeanFactory to load bean definitions into,
	 * in the form of a BeanDefinitionRegistry
	 * @see #setResourceLoader
	 */
	protected AbstractBeanDefinitionReader(BeanDefinitionRegistry beanFactory) {
		Assert.notNull(beanFactory, "Bean factory must not be null");
		this.beanFactory = beanFactory;

		// Determine ResourceLoader to use.
		if (this.beanFactory instanceof ResourceLoader) {
			this.resourceLoader = (ResourceLoader) this.beanFactory;
		}
		else {
			this.resourceLoader = new PathMatchingResourcePatternResolver();
		}
	}

	public BeanDefinitionRegistry getBeanFactory() {
		return this.beanFactory;
	}

	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.beanNameGenerator = (beanNameGenerator != null ? beanNameGenerator : new DefaultBeanNameGenerator());
	}

	public BeanNameGenerator getBeanNameGenerator() {
		return this.beanNameGenerator;
	}

	/**
	 * Set the ResourceLoader to use for resource locations.
	 * If specifying a ResourcePatternResolver, the bean definition reader
	 * will be capable of resolving resource patterns to Resource arrays.
	 * <p>Default is PathMatchingResourcePatternResolver, also capable of
	 * resource pattern resolving through the ResourcePatternResolver interface.
	 * <p>Setting this to <code>null</code> suggests that absolute resource loading
	 * is not available for this bean definition reader.
	 * @see org.springframework.core.io.support.ResourcePatternResolver
	 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
	 */
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}

	/**
	 * Set the ClassLoader to use for bean classes.
	 * <p>Default is <code>null</code>, which suggests to not load bean classes
	 * eagerly but rather to just register bean definitions with class names,
	 * with the corresponding Classes to be resolved later (or never).
	 * @see java.lang.Thread#getContextClassLoader()
	 */
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

	public ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}


	public int loadBeanDefinitions(Resource[] resources) throws BeanDefinitionStoreException {
		Assert.notNull(resources, "Resource array must not be null");
		int counter = 0;
		for (int i = 0; i < resources.length; i++) {
			counter += loadBeanDefinitions(resources[i]);
		}
		return counter;
	}

	public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
		ResourceLoader resourceLoader = getResourceLoader();
		if (resourceLoader == null) {
			throw new BeanDefinitionStoreException(
					"Cannot import bean definitions from location [" + location + "]: no ResourceLoader available");
		}

		if (resourceLoader instanceof ResourcePatternResolver) {
			// Resource pattern matching available.
			try {
				Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
				int loadCount = loadBeanDefinitions(resources);
				if (logger.isDebugEnabled()) {
					logger.debug("Loaded " + loadCount + " bean definitions from location pattern [" + location + "]");
				}
				return loadCount;
			}
			catch (IOException ex) {
				throw new BeanDefinitionStoreException(
						"Could not resolve bean definition resource pattern [" + location + "]", ex);
			}
		}
		else {
			// Can only load single resources by absolute URL.
			Resource resource = resourceLoader.getResource(location);
			int loadCount = loadBeanDefinitions(resource);
			if (logger.isDebugEnabled()) {
				logger.debug("Loaded " + loadCount + " bean definitions from location [" + location + "]");
			}
			return loadCount;
		}
	}

	public int loadBeanDefinitions(String[] locations) throws BeanDefinitionStoreException {
		Assert.notNull(locations, "Location array must not be null");
		int counter = 0;
		for (int i = 0; i < locations.length; i++) {
			counter += loadBeanDefinitions(locations[i]);
		}
		return counter;
	}

}
