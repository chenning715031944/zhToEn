package com.liudaxia.util;

import java.util.List;

public class ExcelObject<T> {

	private String sheetName;
	private String[] columnLabels;
	private List<T> dataList;
	private String[] fieldNames;

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String[] getColumnLabels() {
		return columnLabels;
	}

	public void setColumnLabels(String[] columnLabels) {
		this.columnLabels = columnLabels;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public ExcelObject() {
		super();
		this.sheetName = ExcelUtil.DEFAULT_SHEETNAME;
	}

	public ExcelObject(String[] columnLabels, String[] fieldNames,
			List<T> dataList) {
		super();
		this.sheetName = ExcelUtil.DEFAULT_SHEETNAME;
		this.columnLabels = columnLabels;
		this.dataList = dataList;
		this.fieldNames = fieldNames;
	}

	public ExcelObject(String sheetName, String[] columnLabels,
			String[] fieldNames, List<T> dataList) {
		super();
		this.sheetName = sheetName;
		this.columnLabels = columnLabels;
		this.dataList = dataList;
		this.fieldNames = fieldNames;
	}

}
