package com.tr.common.hibernate;

import java.util.List;
import java.util.Map;

import com.tr.common.hibernate.pojo.HqlQuerys;
import com.tr.common.pagination.hibernate.PageRes;

/**
 * hql查询行为
 * 
 * @author admin
 *
 */
public interface HqlBehavior {
    /**
     * hql 查询实体列表,分页或者不分页，通过qr参数确定
     * 
     * @param qr
     * @return
     */
    <T> List<T> list(HqlQuerys qr);
    /**
     * HQL查询一个
     * @param qr
     * @return
     */
    Object uniqueResult(HqlQuerys qr);
    /**
     * hql查询实体并返回分页
     * 
     * @param qr
     *            封装了查询参数
     * @return
     */
    <T> PageRes<List<T>> pageList(HqlQuerys qr);

    /**
     * hql查询实体并返回,分页
     * 
     * @param queryHql
     * @param countHql
     * @param page
     * @param pageSize
     * @param ps
     * @return
     */
    <T> PageRes<List<T>> pageList(String queryHql, String countHql, int page, int pageSize, Map<String, Object> ps);

    /**
     * hql查询实体并返回,不分页
     * 
     * @param queryHql
     * @param countHql
     * @param params
     *            参数
     * @return
     */
    <T> List<T> list(String queryHql, Map<String, Object> params);

    /**
     * hql 查询实体列表,分页
     * 
     * @param queryHql
     * @param page
     * @param pageSize
     * @param ps
     * @return
     */
    <T> List<T> list(String queryHql, int page, int pageSize, Map<String, Object> ps);

    /**
     * hql 查询count
     * 
     * @param qr
     * @return
     */
    long count(HqlQuerys qr);

    /**
     * hql 查询count
     * 
     * @param countHql
     * @param ps
     * @return
     */
    long count(String countHql, Map<String, Object> ps);
    /**
     * 执行hql语句，例如UPDATE Table SET name=:name WHERE no=:no AND mail=:mail
     * @param hql hql
     * @param ps 参数
     * @return 受影响的条数
     */
    int executeHql(String hql,Map<String, Object> ps);

    /**
     * 执行hql语句，例如UPDATE Table SET name=:name WHERE no=:no AND mail=:mail
     * @param qr 参数
     * @return 受影响的条数
     */
    int executeHql(HqlQuerys qr);
    
    /**
     * 判断属性是否唯一
     * @param entity
     * @param property 属性列表
     * @return
     */
//    boolean unique(Object entity,String ...property);
    
}
