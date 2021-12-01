/*
 * Copyright 2002-2008 the original author or authors.
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

package org.springframework.jdbc.support.nativejdbc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.util.ReflectionUtils;

/**
 * Implementation of the NativeJdbcExtractor interface for the
 * Jakarta Commons DBCP connection pool.
 *
 * <p>Returns the underlying native Connection, Statement, etc to application
 * code instead of DBCP's wrapper implementations. The returned JDBC classes
 * can then safely be cast, e.g. to <code>oracle.jdbc.OracleConnection</code>.
 *
 * <p>This NativeJdbcExtractor can be set just to <i>allow</i> working with a
 * Commons DBCP DataSource: If a given object is not a Commons DBCP wrapper,
 * it will be returned as-is.
 *
 * <p>Tested against Commons DBCP 1.1 and 1.2, but should also work with 1.0.
 * Before Commons DBCP 1.1, DelegatingCallableStatement and DelegatingResultSet
 * have not offered any means to access underlying delegates: As a consequence,
 * <code>getNativeCallableStatement</code> and <code>getNativeResultSet</code>
 * will not work with Commons DBCP 1.0.
 *
 * <p>Note that this version of CommonsDbcpNativeJdbcExtractor will work
 * against the original Commons DBCP in <code>org.apache.commons.dbcp</code>
 * as well as against Tomcat 5.5's relocated Commons DBCP version in the
 * <code>org.apache.tomcat.dbcp.dbcp</code> package.
 *
 * @author Juergen Hoeller
 * @since 25.08.2003
 */
public class CommonsDbcpNativeJdbcExtractor extends NativeJdbcExtractorAdapter {

	private static final String GET_INNERMOST_DELEGATE_METHOD_NAME = "getInnermostDelegate";


	/**
	 * Extracts the innermost delegate from the given Commons DBCP object.
	 * Falls back to the given object if no underlying object found.
	 * @param obj the Commons DBCP Connection/Statement/ResultSet
	 * @return the underlying native Connection/Statement/ResultSet
	 */
	private static Object getInnermostDelegate(Object obj) throws SQLException {
		if (obj == null) {
			return null;
		}
		try {
			Class classToAnalyze = obj.getClass();
			while (!Modifier.isPublic(classToAnalyze.getModifiers())) {
				classToAnalyze = classToAnalyze.getSuperclass();
				if (classToAnalyze == null) {
					// No public provider class found -> fall back to given object.
					return obj;
				}
			}
			Method getInnermostDelegate = classToAnalyze.getMethod(GET_INNERMOST_DELEGATE_METHOD_NAME, (Class[]) null);
			Object delegate = ReflectionUtils.invokeJdbcMethod(getInnermostDelegate, obj);
			return (delegate != null ? delegate : obj);
		}
		catch (NoSuchMethodException ex) {
			return obj;
		}
		catch (SecurityException ex) {
			throw new IllegalStateException("Commons DBCP getInnermostDelegate method is not accessible: " + ex);
		}
	}


	protected Connection doGetNativeConnection(Connection con) throws SQLException {
		return (Connection) getInnermostDelegate(con);
	}

	public Statement getNativeStatement(Statement stmt) throws SQLException {
		return (Statement) getInnermostDelegate(stmt);
	}

	public PreparedStatement getNativePreparedStatement(PreparedStatement ps) throws SQLException {
		return (PreparedStatement) getNativeStatement(ps);
	}

	public CallableStatement getNativeCallableStatement(CallableStatement cs) throws SQLException {
		return (CallableStatement) getNativeStatement(cs);
	}

	public ResultSet getNativeResultSet(ResultSet rs) throws SQLException {
		return (ResultSet) getInnermostDelegate(rs);
	}

}
