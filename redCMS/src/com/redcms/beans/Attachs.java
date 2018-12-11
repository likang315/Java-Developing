package com.redcms.beans;

/**
 * 附件管理，现在存放的是图片
 * @author likang
 *
 */
public class Attachs {
	private long id;
	private String path;
	private String mimetype;
	private String orgname; //上传文件原 名
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	
	
}
