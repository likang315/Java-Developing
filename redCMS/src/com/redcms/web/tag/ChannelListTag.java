package com.redcms.web.tag;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.beans.ChannelAttr;
import com.redcms.db.Db;
/**
 * 列出栏目列表
 * @author Administrator
 *
 */
public class ChannelListTag extends TagSupport 
{
	  private int top; //取几个栏目
	  private int parentId;
	  //当前查询栏目的列表
	  private List<Channel> channelList=null;
	  private Channel current;//迭代当前的channel
	  private int index=0;//当前的索引
	

	@Override
	public int doStartTag() throws JspException
	{
		 try { 
			    if(parentId==0)
			    	channelList=Db.query("select * from channel where parent_id=? order by priority", new BeanListHandler<Channel>(Channel.class),parentId);
			    else
			    	channelList=Db.query("select * from channel where parent_id=? order by priority limit ?", new BeanListHandler<Channel>(Channel.class),parentId,top);	
				
			    if(null!=channelList && channelList.size()>0)
				{
					current=channelList.get(index++);
				    fillAttr(current);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 
		return Tag.EVAL_BODY_INCLUDE;
	}
			 
	@Override
	public int doAfterBody() throws JspException 
	{
	    if(null!=channelList && channelList.size()>1)
	    {
	    	if(index<channelList.size())
	    	{
	    		current=channelList.get(index++);
	    		fillAttr(current);
	    		return IterationTag.EVAL_BODY_AGAIN;
	    	}else
	    	{
	    		return Tag.SKIP_BODY;
	    	}
	    }else
	    {
	    	return Tag.SKIP_BODY;
	    }
	}
	
	@Override
	public int doEndTag() throws JspException {
		
		index=0;
		current=null;
		channelList=null;
		return Tag.EVAL_PAGE;
	}
	
	//取扩展字段
	public void fillAttr(Channel current) 
	{
		try {
			List<ChannelAttr> attrlist=Db.query("select * from channel_attr where channel_id=?", new BeanListHandler<ChannelAttr>(ChannelAttr.class),current.getId());
			if(null!=attrlist && attrlist.size()>0)
			{
				//haspMap
				for(ChannelAttr ca:attrlist)
				{
					current.getAttrs().put(ca.getField_name(), ca.getField_value());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	
	
	//-----------------------------------
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public Channel getCurrent() {
		return current;
	}
	public void setCurrent(Channel current) {
		this.current = current;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	  
}
