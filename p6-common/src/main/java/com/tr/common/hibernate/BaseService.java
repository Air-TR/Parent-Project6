package com.tr.common.hibernate;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tr.common.hibernate.pojo.HqlQuerys;
import com.tr.common.hibernate.pojo.SqlQuerys;
import com.tr.common.hibernate.transform.MyAliasToBeanResultTransformer;
import com.tr.common.pagination.hibernate.PageRes;
import com.tr.common.pagination.hibernate.Sort;
import com.tr.common.pagination.hibernate.SortModel;

/**
 * 基础
 * 
 * @author admin
 *
 * @param <E>
 */
public abstract class BaseService<E> implements HqlBehavior, SqlBehavior {

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	// @Autowired
	// protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 新增
	 * 
	 * @param transientEntity
	 */
	@Transactional
	public void persist(Object transientEntity) {
		hibernateTemplate.persist(transientEntity);
	}

	/**
	 * 批量保存
	 * 
	 * @param list
	 */
	@Transactional
	public void batchPersist(Collection<?> list) {
		int i = 0;
		for (Object o : list) {
			hibernateTemplate.persist(o);
			if (i % 30 == 0) {
				hibernateTemplate.flush();
				hibernateTemplate.clear();
			}
			i++;
		}
	}

	/**
	 * 批量保存
	 * 
	 * @param map
	 */
	@Transactional
	public void batchPersistMap(LinkedHashMap<String, List<?>> map) {
		Iterator<?> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entity = (Entry) iter.next();
			// Object key = entity.getKey();
			Collection<?> list = (Collection<?>) entity.getValue();
			int i = 0;
			for (Object o : list) {
				hibernateTemplate.persist(o);
				if (i % 30 == 0) {
					hibernateTemplate.flush();
					hibernateTemplate.clear();
				}
				i++;
			}
		}
	}

	/**
	 * 
	 * @param map
	 */
	@Transactional
	public void batchMergeMap(LinkedHashMap<String, List<?>> map) {
		Iterator<?> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entity = (Entry) iter.next();
			// Object key = entity.getKey();
			Collection<?> list = (Collection<?>) entity.getValue();
			int i = 0;
			for (Object o : list) {
				hibernateTemplate.merge(o);
				if (i % 30 == 0) {
					hibernateTemplate.flush();
					hibernateTemplate.clear();
				}
				i++;
			}
		}
	}

	/**
	 * 批量更新
	 * 
	 * @param list
	 */
	@Transactional
	public void batchMerge(Collection<?> list) {
		int i = 0;
		for (Object o : list) {
			hibernateTemplate.merge(o);
			if (i % 30 == 0) {
				hibernateTemplate.flush();
				hibernateTemplate.clear();
			}
			i++;
		}
	}

	/**
	 * 更新实体 适用场景：<br/>
	 * 1.根据id，逻辑删除一条数据<br/>
	 * 2. 。。。。。。
	 * 
	 * @param entity
	 *            实体，需要指定id
	 * @return
	 */
	@Transactional
	public Object merge(Object entity) {
		return hibernateTemplate.merge(entity);
	}

	@Transactional
	public void delete(Object entity) {
		hibernateTemplate.delete(entity);
	}

	/**
	 * 根据id查询
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public <T> T get(Class<T> entityClass, Serializable id) {
		return hibernateTemplate.get(entityClass, id);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public <T> PageRes<List<T>> pageList(HqlQuerys qr) {
		PageRes<List<T>> response = new PageRes<List<T>>();
		response.setSorts(qr.getSorts());
		long total = this.count(qr);
		if (total == 0L) {
			List<T> data = new ArrayList<>(0);
			response.setData(data);
			return response;
		}
		response.setTotal(total);
		response.setData(this.list(qr));
		response.setPage(qr.getPage());
		response.setPageSize(qr.getPageSize());
		return response;
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public <T> PageRes<List<T>> pageList(String queryHql, String countHql, int page, int pageSize,
			Map<String, Object> ps) {
		PageRes<List<T>> response = new PageRes<List<T>>();
		long total = this.count(countHql, ps);
		if (total == 0L) {
			List<T> data = new ArrayList<>(0);
			response.setData(data);
			return response;
		}
		response.setTotal(total);
		response.setData(this.list(queryHql, page, pageSize, ps));
		response.setPage(page);
		response.setPageSize(pageSize);
		return response;
	}

	public Object uniqueResult(HqlQuerys qr) {
		return hibernateTemplate.execute((Session s) -> {
			List<Sort> sorts = qr.getSorts();
			String hql = qr.querySql(sorts);
			Query query = s.createQuery(hql);
			setParams(query, qr.getParams());
			return query.uniqueResult();
		});
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public <T> List<T> list(HqlQuerys qr) {
		Map<String, Object> ps = qr.getParams();
		List<Sort> sorts = qr.getSorts();
		String queryHql = qr.querySql(sorts);
		int page = 0, pageSize = 0;
		if (qr.getPageAble()) {
			page = qr.getPage();
			pageSize = qr.getPageSize();
		}
		return list(queryHql, page, pageSize, ps);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> list(String queryHql, int page, int pageSize, Map<String, Object> ps) {

		return hibernateTemplate.execute((Session s) -> {
			Query sq = s.createQuery(queryHql);
			if (page > 0 && pageSize > 0) {
				sq.setFirstResult(pageSize * (page - 1));
				sq.setMaxResults(pageSize);
			} else {
				// do nothing
			}
			this.setParams(sq, ps);
			return sq.list();
		});
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public <T> List<T> list(String queryHql, Map<String, Object> ps) {
		return this.list(queryHql, 0, 0, ps);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public long count(HqlQuerys qr) {
		String countSql = qr.countSql();
		Map<String, Object> ps = qr.getParams();
		return count(countSql, ps);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public long count(String countHql, Map<String, Object> ps) {
		return hibernateTemplate.execute((Session s) -> {
			Query sq = s.createQuery(countHql);
			this.setParams(sq, ps);
			Object obj = sq.uniqueResult();
			if (null == obj) {
				return 0L;
			}
			if (obj instanceof Long) {
				return ((Long) obj).longValue();
			}
			BigInteger b = (BigInteger) obj;
			return b.longValue();

		}).longValue();
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public long nativeCount(SqlQuerys qr) {

		return hibernateTemplate.executeWithNativeSession((Session s) -> {
			Map<String, Object> ps = qr.getParams();

			SQLQuery sq = s.createSQLQuery(qr.countSql());

			this.setParams(sq, ps);
			Object obj = sq.uniqueResult();
			if (null == obj) {
				return 0L;
			}
			if (obj instanceof Long) {
				return ((Long) obj).longValue();
			}
			BigInteger b = (BigInteger) obj;
			return b.longValue();

		}).longValue();
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public <T> PageRes<List<T>> nativePageList(SqlQuerys qr, Class<T> class1) {
		PageRes<List<T>> response = new PageRes<List<T>>();
		long total = this.nativeCount(qr);
		if (total == 0L) {
			List<T> data = new ArrayList<>(0);
			response.setData(data);
			return response;
		}
		response.setTotal(total);
		response.setData(this.nativeList(qr, class1));
		return response;
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> nativeList(SqlQuerys qr, Class<T> class1) {
		return hibernateTemplate.executeWithNativeSession((Session s) -> {
			Map<String, Object> ps = qr.getParams();
			String nativeSql = qr.querySql(qr.getSorts());

			SQLQuery sq = s.createSQLQuery(nativeSql);
			if (Integer.class.equals(class1)) {

			} else if (String.class.equals(class1)) {

			} else if (Map.class.equals(class1)) {
				sq.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			} else {
				ResultTransformer transformer = new MyAliasToBeanResultTransformer(class1);
				sq.setResultTransformer(transformer);
			}

			if (qr.getPageAble()) {
				sq.setFirstResult(qr.getPageSize() * (qr.getPage() - 1));
				sq.setMaxResults(qr.getPageSize());
			}
			this.setParams(sq, ps);
			return sq.list();
		});

	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> nativeSingleList(SqlQuerys qr) {

		return hibernateTemplate.executeWithNativeSession((Session s) -> {
			Map<String, Object> ps = qr.getParams();
			String nativeSql = qr.querySql(qr.getSorts());
			SQLQuery sq = s.createSQLQuery(nativeSql);
			if (qr.getPageAble()) {
				sq.setFirstResult(qr.getPageSize() * (qr.getPage() - 1));
				sq.setMaxResults(qr.getPageSize());
			}
			this.setParams(sq, ps);
			return sq.list();
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public <T> T nativeSingle(SqlQuerys qr, Class<T> class1) {

		return (T) hibernateTemplate.executeWithNativeSession((Session s) -> {
			String nativeSql = qr.querySql(qr.getSorts());
			SQLQuery sq = s.createSQLQuery(nativeSql);
			if (Integer.class.equals(class1)) {

			} else if (String.class.equals(class1)) {

			}
			// TODO 其他数据类型
			else {
				ResultTransformer transformer = new MyAliasToBeanResultTransformer(class1);
				sq.setResultTransformer(transformer);
			}
			Map<String, Object> ps = qr.getParams();
			this.setParams(sq, ps);
			Object object = sq.uniqueResult();
			return object;
		});
	}

	private void setParams(final Query query, final Map<String, Object> ps) {
		if (query != null && null != ps && ps.size() > 0) {
			String[] nps = query.getNamedParameters();
			for (String name : nps) {
				Object val = ps.get(name);
				// String name = p.getName();
				if (val instanceof Collection<?>) {
					Collection<?> values = (Collection<?>) val;
					query.setParameterList(name, values);
				} else {
					query.setParameter(name, val);
				}
			}
		}
	}

	/**
	 * 拼order 语句
	 * 
	 * @param sorts
	 * @param orderAlias
	 * @return
	 */
	protected String order(final List<Sort> sorts, final String orderAlias) {
		// List<Sort> sorts = request.getSorts();
		if (!CollectionUtils.isEmpty(sorts)) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(" ORDER BY ");
			int i = 0;
			for (Sort sort : sorts) {
				String key = sort.getField();
				String val = null == sort.getSortModel() ? SortModel.ASC.name() : sort.getSortModel().name();
				if (null != orderAlias) {
					buffer.append(orderAlias).append(".");
				}
				buffer.append(key).append(" ").append(val);
				if (i != (sorts.size() - 1)) {
					buffer.append(',');
				}
				i++;
			}
			return buffer.toString();
		}
		return "";
	}

//	public boolean unique(Object entity, String... property) {
//		if (null == property || property.length == 0) {
//			throw new RuntimeException("property is null");
//		}
//		Class<?> clazz = (Class<?>) entity.getClass();
//		// 得到该实体的详细信息
//		ClassMetadata classMetadata = hibernateTemplate.getSessionFactory().getClassMetadata(clazz);
//		// 得到id字段名
//		String idName = classMetadata.getIdentifierPropertyName();
//		PropertyDescriptor pd = BeanUtil.getPropertyDescriptor(clazz, idName);
//		// 通过反射获取id的值
//		Object id = null;
//		try {
//			id = pd.getReadMethod().invoke(entity);
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			throw new RuntimeException(e);
//		}
//		// id =
//		// entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
//		// Serializable x = (Serializable)id;
//		HqlQuerys qr = new HqlQuerys(false).setQueryString("FROM " + clazz.getName() + " WHERE 1=1 ").addParam("id", id,
//				" and id <> :id ");
//		for (String propertyName : property) {
//			PropertyDescriptor descriptor = BeanUtil.getPropertyDescriptor(clazz, propertyName);
//			// 通过反射获取值
//			Object propertyValue = null;
//			try {
//				propertyValue = descriptor.getReadMethod().invoke(entity);
//			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				throw new RuntimeException(e);
//			}
//			qr.addParam(propertyName, propertyValue, " and " + propertyName + " = :" + propertyName);
//		}
//		List<?> list = this.list(qr);
//		return list.size() == 0;
//	}

	public int executeHql(final String hql, final Map<String, Object> ps) {
		return hibernateTemplate.execute((Session s) -> {
			Query query = s.createQuery(hql);
			setParams(query, ps);
			return query.executeUpdate();
		});
	}

	public int executeHql(HqlQuerys qr) {
		final String hql = qr.querySql(qr.getSorts());
		final Map<String, Object> ps = qr.getParams();
		return executeHql(hql, ps);
	}

	public int executeSql(final String sql, final Map<String, Object> paramMap) {
		return hibernateTemplate.execute((Session s) -> {
			SQLQuery query = s.createSQLQuery(sql);
			this.setParams(query, paramMap);
			return query.executeUpdate();
		});
	}

	public int executeSql(SqlQuerys qr) {
		final String sql = qr.querySql(qr.getSorts());
		final Map<String, Object> ps = qr.getParams();
		return executeSql(sql, ps);
	}

	public String like(String str) {
		if (null == str || str.trim().length() == 0) {
			return str;
		}
		return "%" + str + "%";
	}

//	public Result<Map<String, QueryFieldDefinition>> queryConds(Class<?> class1) {
//		Result<Map<String, QueryFieldDefinition>> result = new Result<>();
//		Map<String, QueryFieldDefinition> data = new LinkedHashMap<>();
//		Field[] fields = class1.getDeclaredFields();
//		for (Field field : fields) {
//			field.setAccessible(true);
//			if ("serialVersionUID".equals(field.getName())) {
//				continue;
//			}
//			QueryFieldDefinition qd = null;
//			QueryForm form = field.getAnnotation(QueryForm.class);
//			if (null != form) {
//				if (form.hidden()) {
//					continue;
//				}
//				qd = new QueryFieldDefinition();
//				String fieldText = form.text();
//				qd.setFieldText(fieldText == null ? field.getName() : fieldText);
//			} else {
//				continue;
//				// qd = new QueryFieldDefinition();
//				// qd.setFieldText(field.getName());
//			}
//			Class<?> typeClass = field.getType();
//			// logger.info("ft={}，isEnum={}",typeClass.getSimpleName(),typeClass.isEnum());
//			qd.setField(field.getName());
//			qd.setType(Type.parse(typeClass));
//			if (typeClass.isEnum()) {
//				qd.setValue(Enums.getEnumValuesMap(typeClass));
//			} else if (Boolean.class.equals(typeClass)) {
//				Boolean[] booleans = new Boolean[] { Boolean.TRUE, Boolean.FALSE };
//				qd.setValue(booleans);
//			}
//			data.put(qd.getField(), qd);
//		}
//		result.setData(data);
//		return result;
//	}

}
