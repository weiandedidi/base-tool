package com.lsh.base.tools.model.db;


import com.lsh.base.tools.model.PropBaseVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableVO extends PropBaseVO {

	// 表名称
	private String tableName;
	// 属性
	private List<ColumnVO> columns;
	// 备注
	private String remark;
	// 多主键
	private List<ColumnVO> primaryKeys;
	// 主键
	private ColumnVO primaryKey;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ColumnVO> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnVO> columns) {
		this.columns = columns;
	}

	public String getRemark() {
		return remark == null ? "" : remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ColumnVO> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(List<ColumnVO> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public ColumnVO getPrimaryKey() {
		if (primaryKeys != null && !primaryKeys.isEmpty()) {
			primaryKey = primaryKeys.get(0);
		}
		return primaryKey;
	}

	public void setPrimaryKey(ColumnVO primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public Map<String, ColumnVO> getColumnMap() {
		Map<String, ColumnVO> columnMap = new HashMap<String, ColumnVO>();
		if (columns == null || columns.isEmpty()) {
			return columnMap;
		}
		for (int i = 0, l = columns.size(); i < l; i++) {
			ColumnVO col = columns.get(i);
			columnMap.put(col.getDbColumnName(), col);
		}
		return columnMap;
	}
	
	public Map<String, Integer> getColumnOrderMap() {
		Map<String, Integer> columnOrderMap = new HashMap<String, Integer>();
		if (columns == null || columns.isEmpty()) {
			return columnOrderMap;
		}
		for (int i = 0, l = columns.size(); i < l; i++) {
			ColumnVO col = columns.get(i);
			columnOrderMap.put(col.getColumnName(), i);
		}
		return columnOrderMap;
	}

}
