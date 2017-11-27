package com.tr.common.pagination.hibernate;

public class Sort {

	private String field; // ": "time",
	
	private SortModel sortModel; // ": "DESC",

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public SortModel getSortModel() {
		return sortModel;
	}

	public void setSortModel(SortModel sortModel) {
		this.sortModel = sortModel;
	}

}