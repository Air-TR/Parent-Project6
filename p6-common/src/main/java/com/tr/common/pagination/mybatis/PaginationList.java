package com.tr.common.pagination.mybatis;

import java.io.Serializable;
import java.util.List;

/**
 * Pagination model and Data list
 * 分页模型和数据列表
 * 
 * @author taorun
 * @date 2017年11月23日 下午2:18:45
 *
 */

public class PaginationList<T> implements Serializable {
	
	private static final long serialVersionUID = -4724078148282894955L;

	private Pagination pagination = new Pagination();
	
	private List<T> datas;

	public PaginationList() {
		super();
	}
	
	public static <K> PaginationList<K> getInstance(){
	    return new PaginationList<K>();
	}
	
	public PaginationList(Pagination pagination) {
		super();
		this.pagination = pagination;
	}

	public PaginationList(Pagination pagination, List<T> datas) {
		super();
		this.pagination = pagination;
		this.datas = datas;
	}
	
	/**
	 * @return pagination
	 */
	public Pagination getPagination() {
		return pagination;
	}

	/**
	 * @return datas
	 */
	public List<T> getDatas() {
		return datas;
	}
	
	/**
	 * @param pagination : the pagination to set
	 */
	public PaginationList<T> setPagination(Pagination pagination) {
		this.pagination = pagination;
		return this;
	}

	/**
	 * @param datas : the datas to set
	 */
	public PaginationList<T> setDatas(List<T> datas) {
		this.datas = datas;
		return this;
	}
	
}
