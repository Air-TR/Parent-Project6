package com.tr.common.hibernate;

import java.util.List;
import java.util.Map;

import com.tr.common.hibernate.pojo.SqlQuerys;
import com.tr.common.pagination.hibernate.PageRes;

/**
 * Sql查询行为
 * 
 * @author admin
 *
 */
public interface SqlBehavior {
    /**
     * native SQL 查询count
     * 
     * @param qr
     * @return
     */
    long nativeCount(SqlQuerys qr);

    /**
     * native SQL 查询一列或者多列并封装成对象返回
     * 
     * @param qr
     * @param class1
     * @return
     */
    <T> List<T> nativeList(SqlQuerys qr, Class<T> class1);

    /**
     * native SQL 查询实体并返回分页
     * 
     * @param qr
     * @param class1
     * @return
     */
    <T> PageRes<List<T>> nativePageList(SqlQuerys qr, Class<T> class1);

    /**
     * native SQL 查询一列,是否分页在参数中定义
     * 
     * @param qr
     * @return 返回类型T在返回时确定
     */
    <T> List<T> nativeSingleList(SqlQuerys qr);

    /**
     * sql查询一个实体
     * 
     * @param qr 封装了查询语句和参数
     * @param class1 返回的实体类型
     * @return 返回类型T在返回时确定
     */
    <T> T nativeSingle(SqlQuerys qr, Class<T> class1);
    /**
     * 执行sql语句，例如UPDATE table SET real_name=:name WHERE no=:no AND mail=:mail
     * @param sql sql
     * @param ps 参数
     * @return 受影响的条数
     */
    int executeSql(String sql,Map<String, Object> ps);
    /**
     * 执行sql语句，例如UPDATE table SET real_name=:name WHERE no=:no AND mail=:mail
     * @param qr 参数
     * @return 受影响的条数
     */
    int executeSql(SqlQuerys qr);
}
