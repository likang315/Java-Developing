package com.redcms.web.tag;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.beans.ChannelAttr;
import com.redcms.db.Db;

public class ChannelInfoTag extends TagSupport 
{
	private String id;
	private String name;
	
	private Channel current=null;
	
	@Override
	public int doStartTag() throws JspException 
	{
	    String sql="select * from channel where id=?";
	    try {
	    	
	    	Channel buffer=(Channel)pageContext.getAttribute("channelinfo_"+id, PageContext.SESSION_SCOPE);
	    	if(null!=buffer)
	    	{
	    		current=buffer;
	    	}else
	    	{
				current=Db.query(sql, new BeanHandler<Channel>(Channel.class),Integer.parseInt(id));
				fillAttr(current);
				pageContext.setAttribute("channelinfo_"+id, PageContext.SESSION_SCOPE);
	    	}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Tag.SKIP_BODY;
	}
	
	//填充扩展字段
	public void fillAttr(Channel current) 
	{
		try {
			List<ChannelAttr> attrlist=Db.query("select * from channel_attr where channel_id=?", new BeanListHandler<ChannelAttr>(ChannelAttr.class),current.getId());
			if(null!=attrlist&&attrlist.size()>0)
			{
				for(ChannelAttr ca:attrlist)
				{
					current.getAttrs().put(ca.getField_name(), ca.getField_value());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	
	@Override
	public int doEndTag() throws JspException 
	{
		try {
			  String result=null;
			  //先找扩展字段
			  result=current.getAttrs().get(name);
			  
			  if(null==result || "".equals(result))
			  {
			      Class clazz=current.getClass();
			      Field field=clazz.getDeclaredField(name);
			 
				  field.setAccessible(true);
				  result=field.get(current).toString();
			  }
			   
			  pageContext.getOut().print(result);
			  
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Tag.EVAL_PAGE;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
