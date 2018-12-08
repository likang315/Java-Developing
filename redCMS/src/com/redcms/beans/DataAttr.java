package com.redcms.beans;

/**
 * 内容额外字段
 * @author likang
 *
 */
public class DataAttr {
	
	private long data_id;
	private String field_name;
	private String field_value;
	
	public long getData_id() {
		return data_id;
	}
	public void setData_id(long data_id) {
		this.data_id = data_id;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public String getField_value() {
		return field_value;
	}
	public void setField_value(String field_value) {
		this.field_value = field_value;
	}
	
}
