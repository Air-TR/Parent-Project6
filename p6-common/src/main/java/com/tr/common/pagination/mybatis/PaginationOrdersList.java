package com.tr.common.pagination.mybatis;

import java.io.Serializable;
import java.util.List;

/**
 * Pagination model Orders model and Data list 分页模型、排序模型、数据列表
 * 
 * @author taorun
 * @date 2017年11月23日 下午4:02:26
 *
 */

public class PaginationOrdersList<T> implements Serializable {

	private static final long serialVersionUID = -2167286131851693088L;

	private Pagination pagination = new Pagination();

	private Orders orders;

	private List<T> datas;

	public PaginationOrdersList() {
		super();
	}

	public static <K> PaginationOrdersList<K> getInstance() {
		return new PaginationOrdersList<K>();
	}

	public PaginationOrdersList(Pagination pagination) {
		super();
		this.pagination = pagination;
	}

	public PaginationOrdersList(Pagination pagination, Orders orders, List<T> datas) {
		super();
		this.pagination = pagination;
		this.orders = orders;
		this.datas = datas;
	}

	/**
	 * @return pagination
	 */
	public Pagination getPagination() {
		return pagination;
	}

	/**
	 * @return orders
	 */
	public Orders getOrders() {
		return orders;
	}

	/**
	 * @return datas
	 */
	public List<T> getDatas() {
		return datas;
	}

	/**
	 * @param orders : the orders to set
	 */
	public PaginationOrdersList<T> setOrders(Orders orders) {
		this.orders = orders;
		return this;
	}

	/**
	 * @param pagination : the pagination to set
	 */
	public PaginationOrdersList<T> setPagination(Pagination pagination) {
		this.pagination = pagination;
		return this;
	}

	/**
	 * @param datas : the datas to set
	 */
	public PaginationOrdersList<T> setDatas(List<T> datas) {
		this.datas = datas;
		return this;
	}

}
