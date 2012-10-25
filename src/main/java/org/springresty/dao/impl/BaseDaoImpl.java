package org.springresty.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springresty.dao.BaseDao;
import org.springresty.model.Pagination;
import org.springresty.model.QueryFilter;
import org.springresty.model.QueryProperty;


@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> extends
		HibernateDaoSupport implements BaseDao<T> {

	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	private Class<T> clazz;

	public BaseDaoImpl() {
		clazz = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	private void buildQueryParameters(Query query, Map<String, Object> params) {
		if(null == params){
			return;
		}
		String[] parameters = query.getNamedParameters();
		for (String param : parameters) {
			Object paramValue = params.get(param);
			if (paramValue instanceof Collection) {
				Collection collection = (Collection) paramValue;
				if (collection.size() != 0) {
					query.setParameterList(param, (Collection) collection);
				}
			} else {
				query.setParameter(param, paramValue);
			}
		}
	}

	public Serializable create(T obj) {
		return (Serializable) getHibernateTemplate().save(obj);
	}

	public void create(Collection<T> objs) {
		// TODO Auto-generated method stub
		getHibernateTemplate().saveOrUpdateAll(objs);
	}

	public void save(T obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public void save(Collection<T> objs) {
		// TODO Auto-generated method stub
//		Session session = getHibernateTemplate().getSessionFactory().openSession();
//		Transaction tx = null;
		 try{
//			 tx = session.beginTransaction();
			 if (objs.size() > 0){
				for(Iterator<T> it=objs.iterator();it.hasNext();){
				T rs = (T) it.next();
				save(rs);
//				session.save(rs);
				}
			}
//			 tx.commit();
			}
		 catch (HibernateException he){
//			 tx.rollback();
			 he.printStackTrace();
			 throw he;
		 }
		finally{
//			session.close();
		}
	}

	public void delete(T obj) {
		getHibernateTemplate().delete(obj);
	}

	 public void delete(Serializable id) {
		 getHibernateTemplate().delete(find(id));
	 }
	
	// public int delete(Collection<PK> ids) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	public void delete(List<T> objs) {
		getHibernateTemplate().deleteAll(objs);
	}

	public int delete(Map<String, Object> deleteParams) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void merge(T obj) {
		// TODO Auto-generated method stub
		getHibernateTemplate().merge(obj);
	}

	public void update(T obj) {
		getHibernateTemplate().update(obj);
	}

	public void update(Collection<T> objs) {
		for (T obj : objs) {
			update(obj);
		}
	}


	public T find(Serializable id) {
		
		 if (null == id || "".equals(id)) {
				return null;

			} else {
				String hql = "from "+clazz.getSimpleName()+ " where id = :id";
				Map<String, Object> queryParams = new HashMap<String, Object>();
				queryParams.put("id", id);
				List<T> lst = findList(hql, queryParams);
				if(lst.size()>0){
					return (T) lst.get(0);
				}
				else{
					return null;
				}
				
//				return this.getHibernateTemplate().get(clazz, id);
				
			}
		
	}
	

	public T find(String hql, QueryFilter filter) {
		if (StringUtils.isEmpty(hql)) {
			return null;
		}
		
		StringBuffer queryHql = new StringBuffer();
		Map<String, Object> queryParams = new HashMap<String, Object>();
		
		filter.buildQueryHQL(queryHql, queryParams);
		
		
		if(queryHql.length() > 0){
			queryHql.insert(0, " where ");
		}		
		queryHql.insert(0, hql);
		
		return find(queryHql.toString(), queryParams);		
		
		
	}

 
	
	public T find(String hql, Map<String, Object> queryParams) {

		List<T> res =findList(hql, queryParams);
		if (res != null && res.size() > 0) {
			return (T) res.get(0);
		} else {
			return null;
		}
	}
	
	
	

	public List<T> findAll() {
		return getHibernateTemplate().find("from " + clazz.getSimpleName());
	}
	
	public List<T> findAllByIds(List<Integer> ids) {
		String hql = "from " + clazz.getSimpleName() + " where id in ( :ids )";
		return getSession().createQuery(hql).setParameterList("ids", ids).list();
	}


	
	
	/**
	 * 
	 * @param hql
	 * @param filter
	 * @return
	 */
	public Integer findRowsCount(String hql, final QueryFilter filter){
		if (StringUtils.isEmpty(hql)) {
			return 0;
		}
		StringBuffer queryHql = new StringBuffer();
		Map<String, Object> queryParams = new HashMap<String, Object>();
		
		filter.buildQueryHQL(queryHql, queryParams);
		
		if(queryHql.length() > 0){
			queryHql.insert(0, " where ");
		}		
		queryHql.insert(0, hql);		
		
		return findRowsCount(queryHql.toString(), queryParams);	
	}
	
	
	
	protected StringBuffer buildQueryHQL(final StringBuffer hql, final Map<String, Object> queryParams, final QueryFilter filter){
		
		StringBuffer queryHql = new StringBuffer();
		List<QueryProperty> propertyList = filter.getPropertyList();
		int index=1;	
		
		for(QueryProperty property: propertyList){
			StringBuffer paramHql = new StringBuffer();
			int i = property.buildHql(paramHql, queryParams, index);
			if(queryHql.length()>0){
				queryHql.append(" and ");
			}
			queryHql.append(paramHql);
			index = i + 1;
		}
		
		hql.append(queryHql);
		return hql;
	}
	

	

	
	public Object executeHQL(final String hql, final Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		 return getHibernateTemplate().execute(new HibernateCallback() {

	            @Override
	            public Object doInHibernate(Session session)
	                    throws HibernateException, SQLException {
	                Query query = session.createQuery(hql);
	                buildQueryParameters(query, queryParams);
	                return query.executeUpdate();
	            }

	        });
		 
		
	}

	public Object executeSQL(String sql, Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Integer findRowsCount(String hql,Map<String, Object> queryParams){
		if (StringUtils.isEmpty(hql)) return 0;
		Query query = getSession().createQuery(hql);
		buildQueryParameters(query, queryParams);
		return ((Long)query.uniqueResult()).intValue();
	}

	@Override
	public List<T> findList(String hql, final QueryFilter filter, final Pagination pagination){
		if (StringUtils.isEmpty(hql)) {
			return new ArrayList<T>();
		}
		
		StringBuffer queryHql = new StringBuffer();
		Map<String, Object> queryParams = new HashMap<String, Object>();
		
		filter.buildQueryHQL(queryHql, queryParams);
		
		
		if(queryHql.length() > 0){
			queryHql.insert(0, " where ");
		}		
		queryHql.insert(0, hql);
		
		return findList(queryHql.toString(), queryParams, pagination);		
	}
	
	
	
	
	public List<T> findList(final String hql, final Map<String, Object> queryParams, final Pagination pagination) {
		String queryHql = hql;
		//如果Pagination的Sort不是是默认的排序
		if(!(pagination.getSortName().equals("id") && pagination.getSortMode().equals(Pagination.SortModeAesc))){
			StringBuffer withOrderBy = new StringBuffer();
			withOrderBy.append(hql);
			withOrderBy.append(" order by ").append(pagination.getSortName()).append(" ").append(pagination.getSortMode());
			queryHql = 	withOrderBy.toString();
		}
		return findList(queryHql, queryParams, pagination.getPageIndex(), pagination.getPageSize());
	}
	
	public List<T> findList(final String hql,	final Map<String, Object> queryParams){
		return findList(hql, queryParams, 0, 0);
	}
	
	private List<T> findList(final String hql,	final Map<String, Object> queryParams, final int page,	final int pageSize) {
		if (StringUtils.isEmpty(hql)) return new ArrayList<T>();
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				buildQueryParameters(query, queryParams);
				if (page > 0 && pageSize > 0) {
					query.setFirstResult((page - 1) * pageSize);
					query.setMaxResults(pageSize);
				}
				return query.list();
			}
		});
	}
	



}
