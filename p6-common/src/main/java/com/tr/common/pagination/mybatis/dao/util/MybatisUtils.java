package com.tr.common.pagination.mybatis.dao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * Mybatis 工具类
 * 
 * @author taorun
 * @date 2017年11月23日 下午3:47:42
 *
 */

public class MybatisUtils {
	
	public static final Log CONNECTION_LOG = LogFactory.getLog(Connection.class);
	
	public static final Log STATEMENT_LOG = LogFactory.getLog(Statement.class);
	
	public static final Log PREPAREDSTATEMENT_LOG = LogFactory.getLog(PreparedStatement.class);

	/**
	 * return mybatis MappedStatement
	 * 
	 * @param sqlSession
	 * @param statement
	 * @param parameter
	 * @return
	 */
	public MappedStatement getMappedStatement(SqlSession sqlSession, String statement) {
		return sqlSession.getConfiguration().getMappedStatement(statement);
	}

	/**
	 * return PreparedStatement sql
	 * 
	 * @param sqlSession
	 * @param statement
	 * @param parameter
	 * @return
	 */
	public String getSql(SqlSession sqlSession, String statement, Object parameter) {
		MappedStatement mappedStatement = sqlSession.getConfiguration().getMappedStatement(statement);
		return mappedStatement.getBoundSql(parameter).getSql(); // PreparedStatement sql
	}

	/**
	 * 对SQL参数(?)设值
	 * 参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
	 * 
	 * @param stmt
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameter
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setParameters(PreparedStatement stmt, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameter) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameter == null ? null : configuration.newMetaObject(parameter);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameter == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameter.getClass())) {
						value = parameter;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
								+ " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(stmt, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}
	
}
