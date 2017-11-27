package com.tr.p6.config;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class Jpa {
	
	@Value(value = "${hibernate.show_sql:false}")
	private String showSQL;

	/**
	 * 经过验证，直接使用jpa的entitymanager不能成功的切换数据源，所以还是使用HibernateTemplate
	 * 
	 * @return
	 */
	@Bean
	public SessionFactory sessionFactory() {
		SessionFactory sessionFactory = null;
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setDataSource(dataSource());
		bean.setPackagesToScan("com.tr.p6.entity");
		Properties p = new Properties();
		p.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		p.setProperty("hibernate.show_sql", showSQL);// 打印sql由druid插件完成,使用默认值即可
		p.setProperty("hibernate.format_sql", "true");
		p.setProperty("hibernate.hbm2ddl.auto", "none");
		p.setProperty("hibernate.connection.autocommit", "false");
		bean.setHibernateProperties(p);
		try {
			bean.afterPropertiesSet();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		sessionFactory = bean.getObject();
		return sessionFactory;
	}

	@Bean
	public HibernateTemplate hibernateTemplate() {
		HibernateTemplate bean = new HibernateTemplate();
		bean.setSessionFactory(sessionFactory());
		return bean;
	}

	/**
	 * 配置事务管理器，取代默认的JpaTransactionManager
	 * 
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		HibernateTransactionManager tx = new HibernateTransactionManager(sessionFactory());
		return tx;
	}

	public Log4jFilter log4jFilter() {
		Log4jFilter bean = new Log4jFilter();
		bean.setStatementExecutableSqlLogEnable(true);
		bean.setStatementCreateAfterLogEnabled(false);
		bean.setStatementPrepareAfterLogEnabled(false);
		bean.setStatementPrepareCallAfterLogEnabled(false);
		bean.setStatementExecuteAfterLogEnabled(false);
		bean.setStatementExecuteQueryAfterLogEnabled(false);
		bean.setStatementExecuteUpdateAfterLogEnabled(false);
		bean.setStatementExecuteBatchAfterLogEnabled(false);
		bean.setStatementCloseAfterLogEnabled(false);
		bean.setStatementParameterClearLogEnable(false);
		bean.setStatementParameterSetLogEnabled(false);
		return bean;
	}

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		// DynamicDataSource dataSource = new DynamicDataSource(tenantDBPrefix);
		// dataSource.setMasterDataSource(masterDataSource());
		// dataSource.afterPropertiesSet();
		// return dataSource;
		return masterDataSource();
	}

	@Value(value = "${tenantDBPrefix:ark_}")
	private String tenantDBPrefix;
	@Value(value = "${spring.datasource.url}")
	private String datasource_url;
	@Value(value = "${spring.datasource.username}")
	private String datasource_username;
	@Value(value = "${spring.datasource.password}")
	private String datasource_password;
	@Value(value = "${spring.datasource.driver-class-name}")
	private String datasource_driver_class_name;

	public DataSource createDataSource(PoolProperties p) {
		/*
		 * org.apache.tomcat.jdbc.pool.DataSource datasource = new
		 * org.apache.tomcat.jdbc.pool.DataSource(); datasource.setPoolProperties(p);
		 */

		DruidDataSource datasource = new DruidDataSource();
		List<String> initsqls = Lists.newArrayList();
		initsqls.add("set names utf8mb4;");
		datasource.setConnectionInitSqls(initsqls);
		datasource.setDriverClassName("com.mysql.jdbc.Driver");
		datasource.setUrl(p.getUrl());
		datasource.setUsername(p.getUsername());
		datasource.setPassword(p.getPassword());

		datasource.setInitialSize(3);
		datasource.setMinIdle(3);
		datasource.setMaxActive(50);
		datasource.setMaxWait(50000);
		datasource.setTimeBetweenEvictionRunsMillis(120000);
		datasource.setMinEvictableIdleTimeMillis(600000);
		datasource.setValidationQuery("select 1 from dual");
		datasource.setValidationQueryTimeout(3000);
		datasource.setTestWhileIdle(true);
		datasource.setTestOnBorrow(true);
		datasource.setTestOnReturn(true);

		try {
			datasource.setFilters("stat,log4j");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(log4jFilter());
//		filters.add(new CustomerFilter());
		datasource.setProxyFilters(filters);

		return datasource;
	}

	// @Bean(name="masterDataSource")
	private DataSource masterDataSource() {
		PoolProperties p = new PoolProperties();
		p.setUrl(datasource_url);
		p.setDriverClassName(datasource_driver_class_name);
		p.setUsername(datasource_username);
		p.setPassword(datasource_password);
		/*
		 * p.setJmxEnabled(true); p.setTestWhileIdle(false); p.setTestOnBorrow(true);
		 * p.setValidationQuery("SELECT 1"); p.setTestOnReturn(false);
		 * p.setValidationInterval(30000); p.setTimeBetweenEvictionRunsMillis(30000);
		 * p.setMaxActive(100); p.setInitialSize(10); p.setMaxWait(10000);
		 * p.setRemoveAbandonedTimeout(60); p.setMinEvictableIdleTimeMillis(30000);
		 * p.setMinIdle(10); p.setLogAbandoned(true); p.setRemoveAbandoned(true);
		 * p.setJdbcInterceptors(
		 * "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer"
		 * );
		 */

		// DriverManagerDataSource dataSource = new
		// DriverManagerDataSource(datasource_url, datasource_username,
		// datasource_password);
		// dataSource.setDriverClassName(datasource_driver_class_name);
		return createDataSource(p);
	}

}
