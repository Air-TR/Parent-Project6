package com.tr.common.hibernate.pojo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.tr.common.pagination.hibernate.PageReq;
import com.tr.common.pagination.hibernate.Sort;
import com.tr.common.pagination.hibernate.SortModel;
import com.tr.common.util.StringUtilies;

/**
 * 封装hql查询参数
 * @author admin
 *
 */
public class HqlQuerys {
    private boolean pageAble = true;
    private String queryString = "";
    private String countString = "";
    private int page = 0;
    private int pageSize = 10;
    private StringBuffer buffer = new StringBuffer();
    private Map<String, Object> params = new LinkedHashMap<>();
    private List<Sort> sorts;
    private String orderAlias;
    private String group = "";


    public HqlQuerys() {

    }

    public HqlQuerys(boolean pageAble) {
        this.pageAble = pageAble;
    }

    public boolean getPageAble() {
        return pageAble;
    }

    public String getQueryString() {
        return queryString;
    }

    public HqlQuerys setQueryString(String queryString) {
        this.queryString = queryString;
        return this;
    }


    public String getCountString() {
        return countString;
    }

    public HqlQuerys setCountString(String countString) {
        this.countString = countString;
        return this;
    }

    public StringBuffer getBuffer() {
        return buffer;
    }

    public HqlQuerys setBuffer(StringBuffer buffer) {
        this.buffer = buffer;
        return this;
    }

    public List<Sort> getSorts() {
        if(null==sorts){
            sorts = new ArrayList<>();
        }
        return sorts;
    }

    public HqlQuerys setSorts(List<Sort> sorts) {
        this.sorts = sorts;
        return this;
    }


    public HqlQuerys setOrderAlias(String orderAlias) {
        this.orderAlias = orderAlias;
        return this;
    }

    public int getPage() {
        return page;
    }

    public HqlQuerys setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public HqlQuerys setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }


    public HqlQuerys setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public HqlQuerys addParams(String[] names, List<?> values, String buffer) {
        if (Objects.nonNull(values) && values.size() > 0) {
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                Object value = values.get(i);
                if (value instanceof String) {
                    String s = value.toString().trim();
                    if (s.length() > 0) {
                        params.put(name, s);
                        // this.buffer.append(buffer);
                    }
                } else {
                    params.put(name, value);
                }
            }
            this.buffer.append(buffer);
        }
        return this;
    }
    public HqlQuerys addParam(String name,Object value) {
        	return this.addParam(name, value, null);
    }
    public HqlQuerys addParam(String name,Object value, String buffer) {
        return this.addParam(true, name, value, buffer);
    }
    public HqlQuerys addParam(boolean nullCheck,String name,Object value) {
        return this.addParam(nullCheck, name, value, null);
    }
    public HqlQuerys addParam(boolean nullCheck, String name,Object value, String buffer) {
        if(!nullCheck){//不校验null
            params.put(name,value);
            if(null!=buffer){
                this.buffer.append(buffer);
            }
        }else if (Objects.nonNull(value)) {
            if (value instanceof String) {
                String s = value.toString().trim();
                if (s.length() > 0) {
                    params.put(name,s);
                    if(null!=buffer){
                        this.buffer.append(" ").append(buffer).append(" ");
                    }
                }
            } else {
                params.put(name,value);
                if(null!=buffer){
                    this.buffer.append(" ").append(buffer).append(" ");
                }
            }
        }
        return this;
    }

    //public QueryRunner addParam(Param param) {
     //   params.add(param);
     //   return this;
    //}

    public HqlQuerys appendBuffer(String string) {
        buffer.append(string);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String countSql() {
        // buffer = buffer.insert(0, " ");
        // buffer = buffer.insert(0, countSql);
        if(StringUtilies.isNotBlank(countString)){
            return countString + "    " + buffer.toString();
        }
        String tmp = queryString + "    " + getBuffer().toString();
        return "select count(1) from ( " + tmp + ") as __tc";
    }

    private void appendSorts(List<Sort> sorts,StringBuffer buffer) {
        if (null != sorts && sorts.size() > 0) {
            int i = 0;
            buffer.append(" ORDER BY ");
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
        }
    }

    public String querySql(List<Sort> sorts) {
        StringBuffer buffer = new StringBuffer(this.buffer);
        buffer.insert(0, " " + queryString);
        buffer.append(" ").append(group).append(" ");
        this.appendSorts(sorts,buffer);
        return buffer.toString();
    }

    public String getOrderAlias() {
        return orderAlias;
    }

    public HqlQuerys init(PageReq<?> request) {
        this.setSorts(request.getSorts())
        .setPage(request.getPage())
        .setPageSize(request.getPageSize());
        return this;
    }

    public HqlQuerys addSort(String string, SortModel model) {
        if(sorts==null){
            sorts = new ArrayList<>();
        }
        Sort e = new Sort();
        e.setField(string);
        e.setSortModel(model);
        sorts.add(e );
        return this;
    }
    public HqlQuerys setGroup(String g){
        this.group = g;
        return this;
    }

    public String getGroup() {
        return group;
    }
    public HqlQuerys addSortIfNoDefault(String string, SortModel model) {
        if(getSorts()==null||getSorts().size()==0){
            addSort(string, model);
        }
        return this;
    }
}
