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

package org.springframework.orm.ibatis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcAccessor;
import org.springframework.util.Assert;

/**
 * Helper class that simplifies data access via the iBATIS
 * {@link com.ibatis.sqlmap.client.SqlMapClient} API, converting checked
 * SQLExceptions into unchecked DataAccessExceptions, following the
 * <code>org.springframework.dao</code> exception hierarchy.
 * Uses the same {@link org.springframework.jdbc.support.SQLExceptionTranslator}
 * mechanism as {@link org.springframework.jdbc.core.JdbcTemplate}.
 *
 * <p>The main method of this class executes a callback that implements a
 * data access action. Furthermore, this class provides numerous convenience
 * methods that mirror {@link com.ibatis.sqlmap.client.SqlMapExecutor}'s
 * execution methods.
 *
 * <p>It is generally recommended to use the convenience methods on this template
 * for plain query/insert/update/delete operations. However, for more complex
 * operations like batch updates, a custom SqlMapClientCallback must be implemented,
 * usually as anonymous inner class. For example:
 *
 * <pre class="code">
 * getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
 * 	 public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
 * 		 executor.startBatch();
 * 		 executor.update("insertSomething", "myParamValue");
 * 		 executor.update("insertSomethingElse", "myOtherParamValue");
 * 		 executor.executeBatch();
 * 		 return null;
 * 	 }
 * });</pre>
 *
 * The template needs a SqlMapClient to work on, passed in via the "sqlMapClient"
 * property. A Spring context typically uses a {@link SqlMapClientFactoryBean}
 * to build the SqlMapClient. The template an additionally be configured with a
 * DataSource for fetching Connections, although this is not necessary if a
 * DataSource is specified for the SqlMapClient itself (typically through
 * SqlMapClientFactoryBean's "dataSource" property).
 *
 * @author Juergen Hoeller
 * @since 24.02.2004
 * @see #execute
 * @see #setSqlMapClient
 * @see #setDataSource
 * @see #setExceptionTranslator
 * @see SqlMapClientFactoryBean#setDataSource
 * @see com.ibatis.sqlmap.client.SqlMapClient#getDataSource
 * @see com.ibatis.sqlmap.client.SqlMapExecutor
 */
public class SqlMapClientTemplate extends JdbcAccessor implements SqlMapClientOperations {

	private SqlMapClient sqlMapClient;

	private boolean lazyLoadingAvailable = true;


	/**
	 * Create a new SqlMapClientTemplate.
	 */
	public SqlMapClientTemplate() {
	}

	/**
	 * Create a new SqlMapTemplate.
	 * @param sqlMapClient iBATIS SqlMapClient that defines the mapped statements
	 */
	public SqlMapClientTemplate(SqlMapClient sqlMapClient) {
		setSqlMapClient(sqlMapClient);
		afterPropertiesSet();
	}

	/**
	 * Create a new SqlMapTemplate.
	 * @param dataSource JDBC DataSource to obtain connections from
	 * @param sqlMapClient iBATIS SqlMapClient that defines the mapped statements
	 */
	public SqlMapClientTemplate(DataSource dataSource, SqlMapClient sqlMapClient) {
		setDataSource(dataSource);
		setSqlMapClient(sqlMapClient);
		afterPropertiesSet();
	}


	/**
	 * Set the iBATIS Database Layer SqlMapClient that defines the mapped statements.
	 */
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	/**
	 * Return the iBATIS Database Layer SqlMapClient that this template works with.
	 */
	public SqlMapClient getSqlMapClient() {
		return this.sqlMapClient;
	}

	/**
	 * If no DataSource specified, use SqlMapClient's DataSource.
	 * @see com.ibatis.sqlmap.client.SqlMapClient#getDataSource()
	 */
	public DataSource getDataSource() {
		DataSource ds = super.getDataSource();
		return (ds != null ? ds : this.sqlMapClient.getDataSource());
	}

	public void afterPropertiesSet() {
		if (this.sqlMapClient == null) {
			throw new IllegalArgumentException("Property 'sqlMapClient' is required");
		}
		if (this.sqlMapClient instanceof ExtendedSqlMapClient) {
			// Check whether iBATIS lazy loading is available, that is,
			// whether a DataSource was specified on the SqlMapClient itself.
			this.lazyLoadingAvailable = (((ExtendedSqlMapClient) this.sqlMapClient).getDelegate().getTxManager() != null);
		}
		super.afterPropertiesSet();
	}


	/**
	 * Execute the given data access action on a SqlMapExecutor.
	 * @param action callback object that specifies the data access action
	 * @return a result object returned by the action, or <code>null</code>
	 * @throws DataAccessException in case of SQL Maps errors
	 */
	public Object execute(SqlMapClientCallback action) throws DataAccessException {
		Assert.notNull(action, "Callback object must not be null");
		Assert.notNull(this.sqlMapClient, "No SqlMapClient specified");

		// We always needs to use a SqlMapSession, as we need to pass a Spring-managed
		// Connection (potentially transactional) in. This shouldn't be necessary if
		// we run against a TransactionAwareDataSourceProxy underneath, but unfortunately
		// we still need it to make iBATIS batch execution work properly: If iBATIS
		// doesn't recognize an existing transaction, it automatically executes the
		// batch for every single statement...

		SqlMapSession session = this.sqlMapClient.openSession();
		Connection ibatisCon = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Opened SqlMapSession [" + session + "] for iBATIS operation");
			}
			Connection springCon = null;
			try {
				ibatisCon = session.getCurrentConnection();
				if (ibatisCon == null) {
					springCon = DataSourceUtils.getConnection(getDataSource());
					session.setUserConnection(springCon);
					if (logger.isDebugEnabled()) {
						logger.debug("Obtained JDBC Connection [" + springCon + "] for iBATIS operation");
					}
				}
				else {
					if (logger.isDebugEnabled()) {
						logger.debug("Reusing JDBC Connection [" + ibatisCon + "] for iBATIS operation");
					}
				}
				return action.doInSqlMapClient(session);
			}
			catch (SQLException ex) {
				throw getExceptionTranslator().translate("SqlMapClient operation", null, ex);
			}
			finally {
				DataSourceUtils.releaseConnection(springCon, getDataSource());
			}
		}
		finally {
			// Only close SqlMapSession if we know we've actually opened it
			// at the present level.
			if (ibatisCon == null) {
				session.close();
			}
		}
	}

	/**
	 * Execute the given data access action on a SqlMapExecutor,
	 * expecting a List result.
	 * @param action callback object that specifies the data access action
	 * @return the List result
	 * @throws DataAccessException in case of SQL Maps errors
	 */
	public List executeWithListResult(SqlMapClientCallback action) throws DataAccessException {
		return (List) execute(action);
	}

	/**
	 * Execute the given data access action on a SqlMapExecutor,
	 * expecting a Map result.
	 * @param action callback object that specifies the data access action
	 * @return the Map result
	 * @throws DataAccessException in case of SQL Maps errors
	 */
	public Map executeWithMapResult(SqlMapClientCallback action) throws DataAccessException {
		return (Map) execute(action);
	}


	public Object queryForObject(String statementName) throws DataAccessException {
		return queryForObject(statementName, null);
	}

	public Object queryForObject(final String statementName, final Object parameterObject)
			throws DataAccessException {

		return execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return executor.queryForObject(statementName, parameterObject);
			}
		});
	}

	public Object queryForObject(
			final String statementName, final Object parameterObject, final Object resultObject)
			throws DataAccessException {

		return execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return executor.queryForObject(statementName, parameterObject, resultObject);
			}
		});
	}

	public List queryForList(String statementName) throws DataAccessException {
		return queryForList(statementName, null);
	}

	public List queryForList(final String statementName, final Object parameterObject)
			throws DataAccessException {

		return executeWithListResult(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return executor.queryForList(statementName, parameterObject);
			}
		});
	}

	public List queryForList(String statementName, int skipResults, int maxResults)
			throws DataAccessException {

		return queryForList(statementName, null, skipResults, maxResults);
	}

	public List queryForList(
			final String statementName, final Object parameterObject, final int skipResults, final int maxResults)
			throws DataAccessException {

		return executeWithListResult(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return executor.queryForList(statementName, parameterObject, skipResults, maxResults);
			}
		});
	}

	public void queryWithRowHandler(String statementName, RowHandler rowHandler)
			throws DataAccessException {

		queryWithRowHandler(statementName, null, rowHandler);
	}

	public void queryWithRowHandler(
			final String statementName, final Object parameterObject, final RowHandler rowHandler)
			throws DataAccessException {

		execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.queryWithRowHandler(statementName, parameterObject, rowHandler);
				return null;
			}
		});
	}

	/**
	 * @deprecated as of iBATIS 2.3.0
	 */
	public PaginatedList queryForPaginatedList(String statementName, int pageSize)
			throws DataAccessException {

		return queryForPaginatedList(statementName, null, pageSize);
	}

	/**
	 * @deprecated as of iBATIS 2.3.0
	 */
	public PaginatedList queryForPaginatedList(
			final String statementName, final Object parameterObject, final int pageSize)
			throws DataAccessException {

		// Throw exception if lazy loading will not work.
		if (!this.lazyLoadingAvailable) {
			throw new InvalidDataAccessApiUsageException(
					"SqlMapClient needs to have DataSource to allow for lazy loading" +
					" - specify SqlMapClientFactoryBean's 'dataSource' property");
		}

		return (PaginatedList) execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return executor.queryForPaginatedList(statementName, parameterObject, pageSize);
			}
		});
	}

	public Map queryForMap(
			final String statementName, final Object parameterObject, final String keyProperty)
			throws DataAccessException {

		return executeWithMapResult(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return executor.queryForMap(statementName, parameterObject, keyProperty);
			}
		});
	}

	public Map queryForMap(
			final String statementName, final Object parameterObject, final String keyProperty, final String valueProperty)
			throws DataAccessException {

		return executeWithMapResult(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return executor.queryForMap(statementName, parameterObject, keyProperty, valueProperty);
			}
		});
	}

	public Object insert(String statementName) throws DataAccessException {
		return insert(statementName, null);
	}

	public Object insert(final String statementName, final Object parameterObject)
			throws DataAccessException {

		return execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return executor.insert(statementName, parameterObject);
			}
		});
	}

	public int update(String statementName) throws DataAccessException {
		return update(statementName, null);
	}

	public int update(final String statementName, final Object parameterObject)
			throws DataAccessException {

		Integer result = (Integer) execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return new Integer(executor.update(statementName, parameterObject));
			}
		});
		return result.intValue();
	}

	public void update(String statementName, Object parameterObject, int requiredRowsAffected)
			throws DataAccessException {

		int actualRowsAffected = update(statementName, parameterObject);
		if (actualRowsAffected != requiredRowsAffected) {
			throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(
					statementName, requiredRowsAffected, actualRowsAffected);
		}
	}

	public int delete(String statementName) throws DataAccessException {
		return delete(statementName, null);
	}

	public int delete(final String statementName, final Object parameterObject)
			throws DataAccessException {

		Integer result = (Integer) execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				return new Integer(executor.delete(statementName, parameterObject));
			}
		});
		return result.intValue();
	}

	public void delete(String statementName, Object parameterObject, int requiredRowsAffected)
			throws DataAccessException {

		int actualRowsAffected = delete(statementName, parameterObject);
		if (actualRowsAffected != requiredRowsAffected) {
			throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(
					statementName, requiredRowsAffected, actualRowsAffected);
		}
	}

}
