package com.tr.common.hibernate.pojo;

import java.util.List;
import java.util.Objects;

import com.tr.common.pagination.hibernate.PageReq;
import com.tr.common.pagination.hibernate.Sort;
import com.tr.common.pagination.hibernate.SortModel;

/**
 * 封装hql查询参数
 * 
 * @author admin
 *
 */
public class SqlQuerys extends HqlQuerys {

    // Scalar只有在native sql查询且开启查询缓存时才有用
    //private String countScalar;
    //private Map<String, Type> scalarTypes = new LinkedHashMap<String, Type>();

    public SqlQuerys() {

    }

    public SqlQuerys(boolean pageAble) {
        super(pageAble);
    }

    public SqlQuerys addParam(String name,Object value, String buffer) {
        super.addParam(name, value, buffer);
        return this;
    }
    
    @Override
    public SqlQuerys addParam(String name, Object value) {
         super.addParam(name, value);
         return this;
    }

    @Override
    public SqlQuerys addParam(boolean nullCheck, String name, Object value) {
        super.addParam(nullCheck, name, value);
        return this;
    }

    @Override
    public SqlQuerys addParam(boolean nullCheck, String name, Object value, String buffer) {
         super.addParam(nullCheck, name, value, buffer);
         return this;
    }

    public SqlQuerys addParams(String[] names, List<?> values, String buffer) {
        if (Objects.nonNull(values) && values.size() > 0) {
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                Object value = values.get(i);
                if (value instanceof String) {
                    String s = value.toString().trim();
                    if (s.length() > 0) {
                        super.getParams().put(name, s);
                        // this.buffer.append(buffer);
                    }
                } else {
                    super.getParams().put(name, value);
                }
            }
            super.getBuffer().append(buffer);
        }
        return this;
    }
    
    public SqlQuerys setQueryString(String queryString) {
        super.setQueryString(queryString);
        return this;
    }

    public SqlQuerys setOrderAlias(String orderAlias) {
        super.setOrderAlias(orderAlias);
        return this;
    }

    public SqlQuerys setSorts(List<Sort> sorts) {
        super.setSorts(sorts);
        return this;
    }

    public SqlQuerys setPage(int page) {
        super.setPage(page);
        return this;
    }

    public SqlQuerys setPageSize(int pageSize) {
        super.setPageSize(pageSize);
        return this;
    }
    public SqlQuerys setCountString(String countString) {
        super.setCountString(countString);
        return this;
    }

   // public String countSql() {
    //    String tmp = getQueryString() + "    " + getBuffer().toString();
    //    return "select count(1) from ( " + tmp + ") as __tc";
    //}
    
    public SqlQuerys init(PageReq<?> request) {
        this.setSorts(request.getSorts())
        .setPage(request.getPage())
        .setPageSize(request.getPageSize());
        return this;
    }
    public SqlQuerys setGroup(String g){
        super.setGroup(g);
        return this;
    }

    public SqlQuerys addSortIfNoDefault(String string, SortModel model) {
        if(getSorts()==null||getSorts().size()==0){
            addSort(string, model);
        }
        return this;
    }
}
