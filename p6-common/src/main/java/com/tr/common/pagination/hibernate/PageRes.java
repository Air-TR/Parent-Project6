package com.tr.common.pagination.hibernate;

import java.util.List;

import com.tr.common.result.Result;

import io.swagger.annotations.ApiModelProperty;

//@JsonInclude(value=Include.NON_NULL)
public class PageRes<T> extends Result<T> {

	@ApiModelProperty(value = "第几页，默认值1")
	private int page = 1;

	@ApiModelProperty(value = "一页显示数据量，默认值10")
	private int pageSize = 10;

	@ApiModelProperty(value = "总数据")
	private long total = 0L;

	@ApiModelProperty(value = "分组条件")
	private List<Sort> sorts;

//	@ApiModelProperty(value = "分组条件")
//	private String group;

	public PageRes() {
	}

	public PageRes(PageRes<?> request) {
		this.page = request.getPage();
		this.pageSize = request.getPageSize();
		this.sorts = request.getSorts();
		this.total = request.getTotal();
//		this.group = request.getGroup();
	}

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

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<Sort> getSorts() {
		return sorts;
	}

	public void setSorts(List<Sort> sorts) {
		this.sorts = sorts;
	}

//	public String getGroup() {
//		return group;
//	}
//
//	public void setGroup(String group) {
//		this.group = group;
//	}
	
}
