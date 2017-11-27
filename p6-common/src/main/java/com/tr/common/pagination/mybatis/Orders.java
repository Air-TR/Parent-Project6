package com.tr.common.pagination.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DB排序模型
 * 
 * @author taorun
 * @date 2017年11月23日 下午3:56:27
 *
 */

public class Orders implements Serializable {
	
	private static final long serialVersionUID = -4322512157420009378L;
	
	/** 排序模型条目 */
	private List<OrderItem> orders = new ArrayList<Orders.OrderItem>();

	public Orders() {
		
	}

	/**
	 * 建立针对一个字段的排序模型，默认降序
	 * @param name 字段名称
	 */
	public Orders(String name) {
		orders.add(new OrderItem(name));
	}

	/**
	 * 建立针对一个字段的排序模型
	 * @param name 字段名称
	 * @param order 排序方式 false:降序 true:升序
	 */
	public Orders(String name, boolean order) {
		orders.add(new OrderItem(name, order));
	}

	/**
	 * 添加针对一个字段的排序模型，默认降序
	 * @param name 字段名称
	 */
	public Orders addOrder(String name) {
		orders.add(new OrderItem(name));
		return this;
	}

	/**
	 * 添加针对一个字段的排序模型
	 * @param name 字段名称
	 * @param order 排序方式 false:降序 true:升序
	 */
	public Orders addOrder(String name, boolean order) {
		orders.add(new OrderItem(name, order));
		return this;
	}

	/**
	 * @return the orders
	 */
	public List<OrderItem> getOrders() {
		return orders;
	}

	/**
	 * 排序模型条目
	 */
	public class OrderItem implements Serializable {
		
		private static final long serialVersionUID = 219628579511055007L;

		/**
		 * 默认降序
		 * @param name
		 */
		public OrderItem(String name) {
			super();
			this.name = name;
		}

		public OrderItem(String name, boolean order) {
			super();
			this.name = name;
			this.order = order;
		}

		/**
		 * 排序字段的名称
		 */
		private String name;

		/**
		 * 标识倒序还是升序 false：降序
		 */
		private boolean order;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name - the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the order
		 */
		public boolean isOrder() {
			return order;
		}

		/**
		 * @param order - the order to set
		 */
		public void setOrder(boolean order) {
			this.order = order;
		}

	}

}
