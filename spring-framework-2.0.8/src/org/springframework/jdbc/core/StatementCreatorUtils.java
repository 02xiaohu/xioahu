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

package org.springframework.jdbc.core;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.ClassUtils;

/**
 * Utility methods for PreparedStatementSetter/Creator and CallableStatementCreator
 * implementations, providing sophisticated parameter management (including support
 * for LOB values).
 *
 * <p>Used by PreparedStatementCreatorFactory and CallableStatementCreatorFactory,
 * but also available for direct use in custom setter/creator implementations.
 *
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @since 1.1
 * @see PreparedStatementSetter
 * @see PreparedStatementCreator
 * @see CallableStatementCreator
 * @see PreparedStatementCreatorFactory
 * @see CallableStatementCreatorFactory
 * @see SqlParameter
 * @see SqlTypeValue
 * @see org.springframework.jdbc.core.support.SqlLobValue
 */
public abstract class StatementCreatorUtils {

	private static final Log logger = LogFactory.getLog(StatementCreatorUtils.class);

	// Determine whether JDK 1.4's CharSequence interface is available,
	// treating any of its implementations as String value.
	private static final boolean charSequenceAvailable =
			ClassUtils.isPresent("java.lang.CharSequence", StatementCreatorUtils.class.getClassLoader());


	/**
	 * Set the value for a parameter. The method used is based on the SQL type
	 * of the parameter and we can handle complex types like arrays and LOBs.
	 * @param ps the prepared statement or callable statement
	 * @param paramIndex index of the parameter we are setting
	 * @param param the parameter as it is declared including type
	 * @param inValue the value to set
	 * @throws SQLException if thrown by PreparedStatement methods
	 */
	public static void setParameterValue(
	    PreparedStatement ps, int paramIndex, SqlParameter param, Object inValue)
	    throws SQLException {

		setParameterValueInternal(ps, paramIndex, param.getSqlType(), param.getTypeName(), param.getScale(), inValue);
	}

	/**
	 * Set the value for a parameter. The method used is based on the SQL type
	 * of the parameter and we can handle complex types like arrays and LOBs.
	 * @param ps the prepared statement or callable statement
	 * @param paramIndex index of the parameter we are setting
	 * @param sqlType the SQL type of the parameter
	 * @param inValue the value to set (plain value or a SqlTypeValue)
	 * @throws SQLException if thrown by PreparedStatement methods
	 * @see SqlTypeValue
	 */
	public static void setParameterValue(
	    PreparedStatement ps, int paramIndex, int sqlType, Object inValue)
	    throws SQLException {

		setParameterValueInternal(ps, paramIndex, sqlType, null, null, inValue);
	}

	/**
	 * Set the value for a parameter. The method used is based on the SQL type
	 * of the parameter and we can handle complex types like arrays and LOBs.
	 * @param ps the prepared statement or callable statement
	 * @param paramIndex index of the parameter we are setting
	 * @param sqlType the SQL type of the parameter
	 * @param typeName the type name of the parameter
	 * (optional, only used for SQL NULL and SqlTypeValue)
	 * @param inValue the value to set (plain value or a SqlTypeValue)
	 * @throws SQLException if thrown by PreparedStatement methods
	 * @see SqlTypeValue
	 */
	public static void setParameterValue(
	    PreparedStatement ps, int paramIndex, int sqlType, String typeName, Object inValue)
	    throws SQLException {

		setParameterValueInternal(ps, paramIndex, sqlType, typeName, null, inValue);
	}

	/**
	 * Set the value for a parameter. The method used is based on the SQL type
	 * of the parameter and we can handle complex types like arrays and LOBs.
	 * @param ps the prepared statement or callable statement
	 * @param paramIndex index of the parameter we are setting
	 * @param sqlType the SQL type of the parameter
	 * @param typeName the type name of the parameter
	 * (optional, only used for SQL NULL and SqlTypeValue)
	 * @param scale the number of digits after the decimal point
	 * (for DECIMAL and NUMERIC types)
	 * @param inValue the value to set (plain value or a SqlTypeValue)
	 * @throws SQLException if thrown by PreparedStatement methods
	 * @see SqlTypeValue
	 */
	private static void setParameterValueInternal(
	    PreparedStatement ps, int paramIndex, int sqlType, String typeName, Integer scale, Object inValue)
	    throws SQLException {

		if (logger.isDebugEnabled()) {
			logger.debug("Setting SQL statement parameter value: column index " + paramIndex +
					", parameter value [" + inValue +
					"], value class [" + (inValue != null ? inValue.getClass().getName() : "null") +
					"], SQL type " + (sqlType == SqlTypeValue.TYPE_UNKNOWN ? "unknown" : Integer.toString(sqlType)));
		}

		if (inValue == null) {
			if (sqlType == SqlTypeValue.TYPE_UNKNOWN) {
				boolean useSetObject = false;
				try {
					useSetObject = (ps.getConnection().getMetaData().getDatabaseProductName().indexOf("Informix") != -1);
				}
				catch (Throwable ex) {
					logger.debug("Could not check database product name", ex);
				}
				if (useSetObject) {
					ps.setObject(paramIndex, null);
				}
				else {
					ps.setNull(paramIndex, Types.NULL);
				}
			}
			else if (typeName != null) {
				ps.setNull(paramIndex, sqlType, typeName);
			}
			else {
				ps.setNull(paramIndex, sqlType);
			}
		}

		else {  // inValue != null
			if (inValue instanceof SqlTypeValue) {
				((SqlTypeValue) inValue).setTypeValue(ps, paramIndex, sqlType, typeName);
			}
			else if (sqlType == Types.VARCHAR) {
				ps.setString(paramIndex, inValue.toString());
			}
			else if (sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) {
				if (inValue instanceof BigDecimal) {
					ps.setBigDecimal(paramIndex, (BigDecimal) inValue);
				}
				else if (scale != null) {
					ps.setObject(paramIndex, inValue, sqlType, scale.intValue());
				}
				else {
					ps.setObject(paramIndex, inValue, sqlType);
				}
			}
			else if (sqlType == Types.DATE) {
				if (inValue instanceof java.util.Date) {
					if (inValue instanceof java.sql.Date) {
						ps.setDate(paramIndex, (java.sql.Date) inValue);
					}
					else {
						ps.setDate(paramIndex, new java.sql.Date(((java.util.Date) inValue).getTime()));
					}
				}
				else if (inValue instanceof Calendar) {
					Calendar cal = (Calendar) inValue;
					ps.setDate(paramIndex, new java.sql.Date(cal.getTime().getTime()), cal);
				}
				else {
					ps.setObject(paramIndex, inValue, Types.DATE);
				}
			}
			else if (sqlType == Types.TIME) {
				if (inValue instanceof java.util.Date) {
					if (inValue instanceof java.sql.Time) {
						ps.setTime(paramIndex, (java.sql.Time) inValue);
					}
					else {
						ps.setTime(paramIndex, new java.sql.Time(((java.util.Date) inValue).getTime()));
					}
				}
				else if (inValue instanceof Calendar) {
					Calendar cal = (Calendar) inValue;
					ps.setTime(paramIndex, new java.sql.Time(cal.getTime().getTime()), cal);
				}
				else {
					ps.setObject(paramIndex, inValue, Types.TIME);
				}
			}
			else if (sqlType == Types.TIMESTAMP) {
				if (inValue instanceof java.util.Date) {
					if (inValue instanceof java.sql.Timestamp) {
						ps.setTimestamp(paramIndex, (java.sql.Timestamp) inValue);
					}
					else {
						ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
					}
				}
				else if (inValue instanceof Calendar) {
					Calendar cal = (Calendar) inValue;
					ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
				}
				else {
					ps.setObject(paramIndex, inValue, Types.TIMESTAMP);
				}
			}
			else if (sqlType == SqlTypeValue.TYPE_UNKNOWN) {
				if (isStringValue(inValue)) {
					ps.setString(paramIndex, inValue.toString());
				}
				else if (isDateValue(inValue)) {
					ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
				}
				else if (inValue instanceof Calendar) {
					Calendar cal = (Calendar) inValue;
					ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()));
				}
				else {
					// Fall back to generic setObject call without SQL type specified.
					ps.setObject(paramIndex, inValue);
				}
			}
			else {
				// Fall back to generic setObject call with SQL type specified.
				ps.setObject(paramIndex, inValue, sqlType);
			}
		}
	}

	/**
	 * Check whether the given value can be treated as a String value.
	 */
	private static boolean isStringValue(Object inValue) {
		if (charSequenceAvailable) {
			// Consider any CharSequence (including JDK 1.5's StringBuilder) as String.
			return (inValue instanceof CharSequence || inValue instanceof StringWriter);
		}
		else {
			// Explicit enumeration of well-known types for JDK 1.3.
			return (inValue instanceof String || inValue instanceof StringBuffer || inValue instanceof StringWriter);
		}
	}

	/**
	 * Check whether the given value is a <code>java.util.Date</code>
	 * (but not one of the JDBC-specific subclasses).
	 */
	private static boolean isDateValue(Object inValue) {
		return (inValue instanceof java.util.Date && !(inValue instanceof java.sql.Date ||
				inValue instanceof java.sql.Time || inValue instanceof java.sql.Timestamp));
	}

	/**
	 * Clean up all resources held by parameter values which were passed to an
	 * execute method. This is for example important for closing LOB values.
	 * @param paramValues parameter values supplied. May be <code>null</code>.
	 * @see DisposableSqlTypeValue#cleanup()
	 * @see org.springframework.jdbc.core.support.SqlLobValue#cleanup()
	 */
	public static void cleanupParameters(Object[] paramValues) {
		if (paramValues != null) {
			cleanupParameters(Arrays.asList(paramValues));
		}
	}

	/**
	 * Clean up all resources held by parameter values which were passed to an
	 * execute method. This is for example important for closing LOB values.
	 * @param paramValues parameter values supplied. May be <code>null</code>.
	 * @see DisposableSqlTypeValue#cleanup()
	 * @see org.springframework.jdbc.core.support.SqlLobValue#cleanup()
	 */
	public static void cleanupParameters(Collection paramValues) {
		if (paramValues != null) {
			for (Iterator it = paramValues.iterator(); it.hasNext();) {
				Object inValue = it.next();
				if (inValue instanceof DisposableSqlTypeValue) {
					((DisposableSqlTypeValue) inValue).cleanup();
				}
			}
		}
	}

}
