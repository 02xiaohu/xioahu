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

package org.springframework.web.servlet.support;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.EscapedErrors;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

/**
 * Context holder for request-specific state, like current web application
 * context, current locale, current theme, and potential binding errors.
 * Provides easy access to localized messages and Errors instances.
 *
 * <p>Suitable for exposition to views, and usage within JSP's "useBean" tag,
 * JSP scriptlets, JSTL EL, Velocity templates, etc. Necessary for views
 * that do not have access to the servlet request, like Velocity templates.
 *
 * <p>Can be instantiated manually, or automatically exposed to views as
 * model attribute via AbstractView's "requestContextAttribute" property.
 *
 * <p>Will also work outside of DispatcherServlet requests, accessing the root
 * WebApplicationContext and using an appropriate fallback for the locale
 * (the JSTL locale if available, or the HttpServletRequest locale else).
 *
 * @author Juergen Hoeller
 * @since 03.03.2003
 * @see org.springframework.web.servlet.DispatcherServlet
 * @see org.springframework.web.servlet.view.AbstractView#setRequestContextAttribute
 * @see org.springframework.web.servlet.view.UrlBasedViewResolver#setRequestContextAttribute
 * @see #getFallbackLocale
 */
public class RequestContext {

	/**
	 * Default theme name used if the RequestContext cannot find a ThemeResolver.
	 * Only applies to non-DispatcherServlet requests.
	 * <p>Same as AbstractThemeResolver's default, but not linked in here to
	 * avoid package interdependencies.
	 * @see org.springframework.web.servlet.theme.AbstractThemeResolver#ORIGINAL_DEFAULT_THEME_NAME
	 */
	public final static String DEFAULT_THEME_NAME = "theme";

	/**
	 * JSTL locale attribute, as used by JSTL implementations to expose their
	 * current locale. Used as fallback in non-DispatcherServlet requests; if not
	 * available, the accept-header locale is used (<code>request.getLocale</code).
	 * <p>Same as the FMT_LOCALE constant in JSTL's Config class, but not linked
	 * in here to avoid a hard-coded dependency on JSTL. RequestContext does not
	 * depend on JSTL except for this fallback check of JSTL's locale attribute.
	 * @see javax.servlet.jsp.jstl.core.Config#FMT_LOCALE
	 * @see javax.servlet.http.HttpServletRequest#getLocale
	 */
	public final static String JSTL_LOCALE_ATTRIBUTE = "javax.servlet.jsp.jstl.fmt.locale";


	/** JSTL suffix for request-scoped attributes */
	protected static final String REQUEST_SCOPE_SUFFIX = ".request";

	/** JSTL suffix for session-scoped attributes */
	protected static final String SESSION_SCOPE_SUFFIX = ".session";

	/** JSTL suffix for application-scoped attributes */
	protected static final String APPLICATION_SCOPE_SUFFIX = ".application";


	private HttpServletRequest request;

	private Map model;

	private WebApplicationContext webApplicationContext;

	private Locale locale;

	private Theme theme;

	private boolean defaultHtmlEscape;

	private UrlPathHelper urlPathHelper;

	private Map errorsMap;


	/**
	 * Create a new RequestContext for the given request,
	 * using the request attributes for Errors retrieval.
	 * <p>This only works with InternalResourceViews, as Errors instances
	 * are part of the model and not normally exposed as request attributes.
	 * It will typically be used within JSPs or custom tags.
	 * <p><b>Will only work within a DispatcherServlet request.</b> Pass in a
	 * ServletContext to be able to fallback to the root WebApplicationContext.
	 * @param request current HTTP request
	 * @see org.springframework.web.servlet.DispatcherServlet
	 * @see #RequestContext(javax.servlet.http.HttpServletRequest, javax.servlet.ServletContext)
	 */
	public RequestContext(HttpServletRequest request) {
		initContext(request, null, null);
	}

	/**
	 * Create a new RequestContext for the given request,
	 * using the request attributes for Errors retrieval.
	 * <p>This only works with InternalResourceViews, as Errors instances
	 * are part of the model and not normally exposed as request attributes.
	 * It will typically be used within JSPs or custom tags.
	 * <p>If a ServletContext is specified, the RequestContext will also
	 * work with the root WebApplicationContext (outside a DispatcherServlet).
	 * @param request current HTTP request
	 * @param servletContext the servlet context of the web application
	 * (can be <code>null</code>; necessary for fallback to root WebApplicationContext)
	 * @see org.springframework.web.context.WebApplicationContext
	 * @see org.springframework.web.servlet.DispatcherServlet
	 */
	public RequestContext(HttpServletRequest request, ServletContext servletContext) {
		initContext(request, servletContext, null);
	}

	/**
	 * Create a new RequestContext for the given request,
	 * using the given model attributes for Errors retrieval.
	 * <p>This works with all View implementations.
	 * It will typically be used by View implementations.
	 * <p><b>Will only work within a DispatcherServlet request.</b> Pass in a
	 * ServletContext to be able to fallback to the root WebApplicationContext.
	 * @param request current HTTP request
	 * @param model the model attributes for the current view
	 * (can be <code>null</code>, using the request attributes for Errors retrieval)
	 * @see org.springframework.web.servlet.DispatcherServlet
	 * @see #RequestContext(javax.servlet.http.HttpServletRequest, javax.servlet.ServletContext, Map)
	 */
	public RequestContext(HttpServletRequest request, Map model) {
		initContext(request, null, model);
	}

	/**
	 * Create a new RequestContext for the given request,
	 * using the given model attributes for Errors retrieval.
	 * <p>This works with all View implementations.
	 * It will typically be used by View implementations.
	 * <p>If a ServletContext is specified, the RequestContext will also
	 * work with a root WebApplicationContext (outside a DispatcherServlet).
	 * @param request current HTTP request
	 * @param servletContext the servlet context of the web application
	 * (can be <code>null</code>; necessary for fallback to root WebApplicationContext)
	 * @param model the model attributes for the current view
	 * (can be <code>null</code>, using the request attributes for Errors retrieval)
	 * @see org.springframework.web.context.WebApplicationContext
	 * @see org.springframework.web.servlet.DispatcherServlet
	 */
	public RequestContext(HttpServletRequest request, ServletContext servletContext, Map model) {
		initContext(request, servletContext, model);
	}

	/**
	 * Default constructor for subclasses.
	 */
	protected RequestContext() {
	}


	/**
	 * Initialize this context with the given request,
	 * using the given model attributes for Errors retrieval.
	 * <p>Delegates to <code>getFallbackLocale</code> and <code>getFallbackTheme</code>
	 * for determining the fallback locale and theme, respectively, if no LocaleResolver
	 * and/or ThemeResolver can be found in the request.
	 * @param request current HTTP request
	 * @param servletContext the servlet context of the web application
	 * (can be <code>null</code>; necessary for fallback to root WebApplicationContext)
	 * @param model the model attributes for the current view
	 * (can be <code>null</code>, using the request attributes for Errors retrieval)
	 * @see #getFallbackLocale
	 * @see #getFallbackTheme
	 * @see org.springframework.web.servlet.DispatcherServlet#LOCALE_RESOLVER_ATTRIBUTE
	 * @see org.springframework.web.servlet.DispatcherServlet#THEME_RESOLVER_ATTRIBUTE
	 */
	protected void initContext(HttpServletRequest request, ServletContext servletContext, Map model) {
		this.request = request;
		this.model = model;

		// Fetch WebApplicationContext, either from DispatcherServlet or the root context.
		// ServletContext needs to be specified to be able to fall back to the root context!
		this.webApplicationContext = RequestContextUtils.getWebApplicationContext(request, servletContext);

		// Determine locale to use for this RequestContext.
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if (localeResolver != null) {
			// Try LocaleResolver (we're within a DispatcherServlet request).
			this.locale = localeResolver.resolveLocale(request);
		}
		else {
			// No LocaleResolver available -> try fallback.
			this.locale = getFallbackLocale();
		}

		// Determine default HTML escape setting from the "defaultHtmlEscape"
		// context-param in web.xml, if any.
		this.defaultHtmlEscape = WebUtils.isDefaultHtmlEscape(this.webApplicationContext.getServletContext());

		this.urlPathHelper = new UrlPathHelper();
	}

	/**
	 * Determine the fallback locale for this context.
	 * <p>The default implementation checks for a JSTL locale attribute
	 * in request, session or application scope; if not found,
	 * returns the <code>HttpServletRequest.getLocale()</code>.
	 * @return the fallback locale (never <code>null</code>)
	 * @see javax.servlet.http.HttpServletRequest#getLocale
	 */
	protected Locale getFallbackLocale() {
		Locale locale = (Locale) getRequest().getAttribute(JSTL_LOCALE_ATTRIBUTE);
		if (locale == null) {
			locale = (Locale) getRequest().getAttribute(JSTL_LOCALE_ATTRIBUTE + REQUEST_SCOPE_SUFFIX);
			if (locale == null) {
				HttpSession session = getRequest().getSession(false);
				if (session != null) {
					locale = (Locale) session.getAttribute(JSTL_LOCALE_ATTRIBUTE);
					if (locale == null) {
						locale = (Locale) session.getAttribute(JSTL_LOCALE_ATTRIBUTE + SESSION_SCOPE_SUFFIX);
					}
				}
				if (locale == null) {
					locale = (Locale) getServletContext().getAttribute(JSTL_LOCALE_ATTRIBUTE);
					if (locale == null) {
						locale = (Locale) getServletContext().getAttribute(JSTL_LOCALE_ATTRIBUTE + APPLICATION_SCOPE_SUFFIX);
						if (locale == null) {
							// No JSTL locale available -> fall back to accept-header locale.
							locale = getRequest().getLocale();
						}
					}
				}
			}
		}
		return locale;
	}

	/**
	 * Determine the fallback theme for this context.
	 * <p>The default implementation returns the default theme (with name "theme").
	 * @return the fallback theme (never <code>null</code>)
	 */
	protected Theme getFallbackTheme() {
		ThemeSource themeSource = RequestContextUtils.getThemeSource(getRequest());
		if (themeSource == null) {
			themeSource = new ResourceBundleThemeSource();
		}
		Theme theme = themeSource.getTheme(DEFAULT_THEME_NAME);
		if (theme == null) {
			throw new IllegalStateException("No theme defined and no fallback theme found");
		}
		return theme;
	}


	/**
	 * Return the underlying HttpServletRequest.
	 * Only intended for cooperating classes in this package.
	 */
	protected final HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * Return the current WebApplicationContext.
	 */
	public final WebApplicationContext getWebApplicationContext() {
		return this.webApplicationContext;
	}

	/**
	 * Return the underlying ServletContext.
	 * Only intended for cooperating classes in this package.
	 */
	protected final ServletContext getServletContext() {
		return this.webApplicationContext.getServletContext();
	}

	/**
	 * Return the current locale.
	 */
	public final Locale getLocale() {
		return this.locale;
	}

	/**
	 * Return the current theme (never <code>null</code>).
	 * Resolved lazily for more efficiency when theme support is not used.
	 */
	public final Theme getTheme() {
		if (this.theme == null) {
			// Lazily determine theme to use for this RequestContext.
			this.theme = RequestContextUtils.getTheme(this.request);
			if (this.theme == null) {
				// No ThemeResolver and ThemeSource available -> try fallback.
				this.theme = getFallbackTheme();
			}
		}
		return this.theme;
	}


	/**
	 * (De)activate default HTML escaping for messages and errors, for the scope
	 * of this RequestContext. The default is the application-wide setting
	 * (the "defaultHtmlEscape" context-param in web.xml).
	 * @see org.springframework.web.util.WebUtils#isDefaultHtmlEscape
	 */
	public void setDefaultHtmlEscape(boolean defaultHtmlEscape) {
		this.defaultHtmlEscape = defaultHtmlEscape;
	}

	/**
	 * Is default HTML escaping active?
	 */
	public boolean isDefaultHtmlEscape() {
		return this.defaultHtmlEscape;
	}

	/**
	 * Set the UrlPathHelper to use for context path and request URI decoding.
	 * Can be used to pass a shared UrlPathHelper instance in.
	 * <p>A default UrlPathHelper is always available.
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		this.urlPathHelper = (urlPathHelper != null ? urlPathHelper : new UrlPathHelper());
	}

	/**
	 * Return the UrlPathHelper used for context path and request URI decoding.
	 * Can be used to configure the current UrlPathHelper.
	 * <p>A default UrlPathHelper is always available.
	 */
	public UrlPathHelper getUrlPathHelper() {
		return this.urlPathHelper;
	}


	/**
	 * Return the context path of the original request,
	 * that is, the path that indicates the current web application.
	 * This is useful for building links to other resources within the application.
	 * <p>Delegates to the UrlPathHelper for decoding.
	 * @see javax.servlet.http.HttpServletRequest#getContextPath
	 * @see #getUrlPathHelper
	 */
	public String getContextPath() {
		return this.urlPathHelper.getOriginatingContextPath(this.request);
	}

	/**
	 * Return the request URI of the original request, that is, the invoked URL
	 * without parameters. This is particularly useful as HTML form action target,
	 * possibly in combination with the original query string.
	 * <p><b>Note this implementation will correctly resolve to the URI of any
	 * originating root request in the presence of a forwarded request. However, this
	 * can only work when the Servlet 2.4 'forward' request attributes are present.
	 * For use in a Servlet 2.3- environment, you can rely on
	 * {@link org.springframework.web.servlet.view.InternalResourceView}
	 * to add these prior to dispatching the request.</b>
	 * <p>Delegates to the UrlPathHelper for decoding.
	 * @see #getQueryString
	 * @see org.springframework.web.util.UrlPathHelper#getOriginatingRequestUri
	 * @see #getUrlPathHelper
	 */
	public String getRequestUri() {
		return this.urlPathHelper.getOriginatingRequestUri(this.request);
	}

	/**
	 * Return the query string of the current request, that is, the part after
	 * the request path. This is particularly useful for building an HTML form
	 * action target in combination with the original request URI.
	 * <p><b>Note this implementation will correctly resolve to the query string of any
	 * originating root request in the presence of a forwarded request. However, this
	 * can only work when the Servlet 2.4 'forward' request attributes are present.
	 * For use in a Servlet 2.3- environment, you can rely on
	 * {@link org.springframework.web.servlet.view.InternalResourceView}
	 * to add these prior to dispatching the request.</b>
	 * <p>Delegates to the UrlPathHelper for decoding.
	 * @see #getRequestUri
	 * @see org.springframework.web.util.UrlPathHelper#getOriginatingQueryString
	 * @see #getUrlPathHelper
	 */
	public String getQueryString() {
		return this.urlPathHelper.getOriginatingQueryString(this.request);
	}


	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, String defaultMessage) {
		return getMessage(code, null, defaultMessage, this.defaultHtmlEscape);
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return getMessage(code, args, defaultMessage, this.defaultHtmlEscape);
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param args arguments for the message as a List, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, List args, String defaultMessage) {
		return getMessage(code, (args != null ? args.toArray() : null), defaultMessage, this.defaultHtmlEscape);
	}

	/**
	 * Retrieve the message for the given code.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @param htmlEscape HTML escape the message?
	 * @return the message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage, boolean htmlEscape) {
		String msg = this.webApplicationContext.getMessage(code, args, defaultMessage, this.locale);
		return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getMessage(String code) throws NoSuchMessageException {
		return getMessage(code, null, this.defaultHtmlEscape);
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getMessage(String code, Object[] args) throws NoSuchMessageException {
		return getMessage(code, args, this.defaultHtmlEscape);
	}

	/**
	 * Retrieve the message for the given code, using the "defaultHtmlEscape" setting.
	 * @param code code of the message
	 * @param args arguments for the message as a List, or <code>null</code> if none
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getMessage(String code, List args) throws NoSuchMessageException {
		return getMessage(code, (args != null ? args.toArray() : null), this.defaultHtmlEscape);
	}

	/**
	 * Retrieve the message for the given code.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param htmlEscape HTML escape the message?
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getMessage(String code, Object[] args, boolean htmlEscape) throws NoSuchMessageException {
		String msg = this.webApplicationContext.getMessage(code, args, this.locale);
		return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
	}

	/**
	 * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance),
	 * using the "defaultHtmlEscape" setting.
	 * @param resolvable the MessageSourceResolvable
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
		return getMessage(resolvable, this.defaultHtmlEscape);
	}

	/**
	 * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance).
	 * @param resolvable the MessageSourceResolvable
	 * @param htmlEscape HTML escape the message?
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getMessage(MessageSourceResolvable resolvable, boolean htmlEscape) throws NoSuchMessageException {
		String msg = this.webApplicationContext.getMessage(resolvable, this.locale);
		return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
	}


	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getThemeMessage(String code, String defaultMessage) {
		return getTheme().getMessageSource().getMessage(code, null, defaultMessage, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getThemeMessage(String code, Object[] args, String defaultMessage) {
		return getTheme().getMessageSource().getMessage(code, args, defaultMessage, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param args arguments for the message as a List, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getThemeMessage(String code, List args, String defaultMessage) {
		return getTheme().getMessageSource().getMessage(
				code, (args != null ? args.toArray() : null), defaultMessage, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getThemeMessage(String code) throws NoSuchMessageException {
		return getTheme().getMessageSource().getMessage(code, null, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getThemeMessage(String code, Object[] args) throws NoSuchMessageException {
		return getTheme().getMessageSource().getMessage(code, args, this.locale);
	}

	/**
	 * Retrieve the theme message for the given code.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param code code of the message
	 * @param args arguments for the message as a List, or <code>null</code> if none
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getThemeMessage(String code, List args) throws NoSuchMessageException {
		return getTheme().getMessageSource().getMessage(
				code, (args != null ? args.toArray() : null), this.locale);
	}

	/**
	 * Retrieve the given MessageSourceResolvable in the current theme.
	 * <p>Note that theme messages are never HTML-escaped, as they typically
	 * denote theme-specific resource paths and not client-visible messages.
	 * @param resolvable the MessageSourceResolvable
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException if not found
	 */
	public String getThemeMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
		return getTheme().getMessageSource().getMessage(resolvable, this.locale);
	}


	/**
	 * Retrieve the Errors instance for the given bind object,
	 * using the "defaultHtmlEscape" setting.
	 * @param name name of the bind object
	 * @return the Errors instance, or <code>null</code> if not found
	 */
	public Errors getErrors(String name) {
		return getErrors(name, this.defaultHtmlEscape);
	}

	/**
	 * Retrieve the Errors instance for the given bind object.
	 * @param name name of the bind object
	 * @param htmlEscape create an Errors instance with automatic HTML escaping?
	 * @return the Errors instance, or <code>null</code> if not found
	 */
	public Errors getErrors(String name, boolean htmlEscape) {
		if (this.errorsMap == null) {
			this.errorsMap = new HashMap();
		}
		Errors errors = (Errors) this.errorsMap.get(name);
		boolean put = false;
		if (errors == null) {
			errors = (Errors) getModelObject(BindingResult.MODEL_KEY_PREFIX + name);
			// Check old BindException prefix for backwards compatibility.
			if (errors == null) {
				errors = (Errors) getModelObject(BindException.ERROR_KEY_PREFIX + name);
			}
			if (errors == null) {
				return null;
			}
			put = true;
		}
		if (htmlEscape && !(errors instanceof EscapedErrors)) {
			errors = new EscapedErrors(errors);
			put = true;
		}
		else if (!htmlEscape && errors instanceof EscapedErrors) {
			errors = ((EscapedErrors) errors).getSource();
			put = true;
		}
		if (put) {
			this.errorsMap.put(name, errors);
		}
		return errors;
	}

	/**
	 * Retrieve the model object for the given model name,
	 * either from the model or from the request attributes.
	 * @param modelName the name of the model object
	 * @return the model object
	 */
	protected Object getModelObject(String modelName) {
		if (this.model != null) {
			return this.model.get(modelName);
		}
		else {
			return this.request.getAttribute(modelName);
		}
	}

	/**
	 * Create a BindStatus for the given bind object,
	 * using the "defaultHtmlEscape" setting.
	 * @param path the bean and property path for which values and errors
	 * will be resolved (e.g. "person.age")
	 * @return the new BindStatus instance
	 * @throws IllegalStateException if no corresponding Errors object found
	 */
	public BindStatus getBindStatus(String path) throws IllegalStateException {
		return new BindStatus(this, path, this.defaultHtmlEscape);
	}

	/**
	 * Create a BindStatus for the given bind object,
	 * using the "defaultHtmlEscape" setting.
	 * @param path the bean and property path for which values and errors
	 * will be resolved (e.g. "person.age")
	 * @param htmlEscape create a BindStatus with automatic HTML escaping?
	 * @return the new BindStatus instance
	 * @throws IllegalStateException if no corresponding Errors object found
	 */
	public BindStatus getBindStatus(String path, boolean htmlEscape) throws IllegalStateException {
		return new BindStatus(this, path, htmlEscape);
	}

}
