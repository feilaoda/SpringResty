package org.springresty.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springresty.dao.BaseDao;
import org.springresty.model.QueryFilter;
import org.springresty.service.BaseService;




public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected abstract  BaseDao<T> getDao();
	
	
	
	
	
	public void save(T obj) {

		getDao().save(obj);
	}

	public void save(Collection<T> objs) {

		getDao().save(objs);
	}

	public void delete(T obj) {

		getDao().delete(obj);
	}

	public void delete(List<T> objs) {
		
		getDao().delete(objs);
	}

	public int delete(Map<String, Object> deleteParams) {
		// TODO Auto-generated method stub
		return getDao().delete(deleteParams);
	}

	public Object executeHQL(String hql, Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return getDao().executeHQL(hql, queryParams);
	}

	public Object executeSQL(String sql, Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return getDao().executeSQL(sql, queryParams);
	}

	public T find(long id) {
		// TODO Auto-generated method stub
		return getDao().find(id);
	}

	public List<T> findAll() {
		// TODO Auto-generated method stub
		return getDao().findAll();
	}

    public List<T> findAllByIds(List<Integer> ids) {
    	return getDao().findAllByIds(ids);
    }
    
    public T find(String hql, QueryFilter filter){
    	return getDao().find(hql, filter);
    }

	public T find(String queryString, Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return getDao().find(queryString, queryParams);
	}

	public List<T> findList(String queryString,
			Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return getDao().findList(queryString, queryParams);
	}
 

	public Serializable create(T obj){
		
		return getDao().create(obj);
	}

	public void create(Collection<T> objs) {
		// TODO Auto-generated method stub
		getDao().create(objs);
	}

	public void update(T obj) {
		// TODO Auto-generated method stub
		getDao().update(obj);
	}

	public void update(Collection<T> objs) {
		// TODO Auto-generated method stub
		getDao().update(objs);
	}
	


}
