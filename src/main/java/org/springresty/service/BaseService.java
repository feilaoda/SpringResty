package org.springresty.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springresty.model.QueryFilter;





public interface BaseService<T> {

    /**
     * 创建一个实体
     * 
     * @param newInstance
     * @return
     */
	Serializable create(T newInstance);

    /**
     * 批量创建实体
     * 
     * @param newInstance
     * @return
     */
    void create(Collection<T> instances);

    /**
     * 根据id获取一个实体
     * 
     * @param id
     * @return
     */
    T find(long id);

 
    /**
     * 更新一个实体
     * 
     * @param obj
     */
    void update(T obj);

    /**
     * 批量更新
     * 
     * @param instances
     */
    void update(Collection<T> instances);

    /**
     * 删除一个实体
     * 
     * @param persistentObj
     */
    void delete(T obj);

//    /**
//     * 根据id删除
//     * 
//     * @param id
//     */
//    int delete(Serializable id);
//
//    /**
//     * 根据id批量删除
//     * 
//     * @param ids
//     */
//    int delete(Collection<PK> ids);

    /**
     * 删实体
     * 
     * @param objs
     */
    void delete(List<T> objs);

    /**
     * 根据deleteParams删除
     * 
     * @param deleteParams
     */
    int delete(Map<String, Object> deleteParams);

    /**
     * 新增或修改
     * 
     * @param obj
     */
    void save(T obj);

    /**
     * 批量新增或修改
     * 
     * @param instances
     */
    void save(Collection<T> objs);

   
    /**
     * 使用命名参数查询,不分页。
     * 
     * @param hql
     *            查询语句
     * @param queryParams
     *            命名参数键值对
     * @return List
     */
    List<T> findList(String hql, Map<String, Object> queryParams);

 
    T find(String hql, QueryFilter filter);
    
    /**
     * 查询出一个结果
     * 
     * @param hql
     * @param queryParams
     * @return
     */
    T find(String hql, Map<String, Object> queryParams);

    /**
     * 查询所有
     * 
     * @return
     */
    List<T> findAll();
    
    /**
     * 根据ids查找所有
     * @param ids
     * @return
     */
    public List<T> findAllByIds(List<Integer> ids);


    /**
     * 直接执行hql
     * 
     * @param hql
     * @return Object
     */
    Object executeHQL(String hql, Map<String, Object> queryParams);

    /**
     * 直接执行sql
     * 
     * @param sql
     * @return Object
     */
    Object executeSQL(String sql, Map<String, Object> queryParams);

    
}
