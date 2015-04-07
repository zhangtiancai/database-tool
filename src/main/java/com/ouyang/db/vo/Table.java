package com.ouyang.db.vo;

import java.io.Serializable;
import java.util.List;

public class Table implements Serializable {
	
	private String id;
	private String tabName;
	private Integer clumnId;
	private String dataType;
	private Integer dataLength;
	private String comments;
	private List<Column> column;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public List<Column> getColumn() {
		return column;
	}
	public void setColumn(List<Column> column) {
		this.column = column;
	}
	public Integer getClumnId() {
		return clumnId;
	}
	public void setClumnId(Integer clumnId) {
		this.clumnId = clumnId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Integer getDataLength() {
		return dataLength;
	}
	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
