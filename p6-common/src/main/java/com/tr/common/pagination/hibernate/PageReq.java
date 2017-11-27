package com.tr.common.pagination.hibernate;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import io.swagger.annotations.ApiModelProperty;

public class PageReq<T> {
	
    @ApiModelProperty(value = "排序条件")
    private List<Sort> sorts;
    
    //@ApiModelProperty(value = "分组条件")
    //private String group;
    
    @ApiModelProperty(value = "默认值1")
    private int page = 1;
    
    @ApiModelProperty(value = "默认值10")
    private int pageSize = 10;
    
    @ApiModelProperty(value="具体查询条件")
    private T data;
    
    public List<String> allowOrderColums() {
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(getClass());
        List<String> list = new ArrayList<String>();
        for (PropertyDescriptor f : pds) {
            String name = f.getName();
            if ("class".equals(name) 
                    || "sorts".equals(name) 
                    || "firstResult".equals(name) 
                    || "maxResult".equals(name)
                    || "group".equals(name)) {
                continue;
            }
            list.add(name);
        }
        return list;
    }


    public T getData() {
        /*
        if(null==data){
            java.lang.reflect.Type type0 = getClass().getGenericSuperclass();
            ParameterizedType type = (ParameterizedType) type0;
            Class<?> entityClass = (Class<?>) type.getActualTypeArguments()[0]; 
            try {
                data = (T) entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        */
        return data;
    }


    public void setData(T data) {
        this.data = data;
    }

/*
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
*/
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

}
