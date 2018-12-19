package com.redcms.web.tag;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Data;
import com.redcms.beans.DataAttr;
import com.redcms.db.Db;
import com.redcms.db.PageDiv;
/**
 * 显示 指定栏目文章列表
 *
 */
public class ArticleListTag extends TagSupport
{
	private static final long serialVersionUID = 1L;
	
	private int top;//显示前几条
	private int channelId;//栏目id
	private int pageNo;
	private int pageSize;
    private Data current;//当前的文章
    private List<Data> dataList = null;  //当前查询的文章列表
    private int index=0; //当前的索引
    
    //初始信息，top和分页必须要有一个
    @Override
    public int doStartTag() throws JspException
    {
    	 try {
    		 PageDiv<Data> pd=null;
    		 if(channelId>0)
    		 {
    			  //要取栏目下的内容
    			  if(top>0)
    			  {
    				  //只取前top条
    				  dataList=Db.query("select d.*,c.name as channel_name,(select inc.t_name from channel inc where d.channel_id=inc.id) as t_name from alldata d inner join channel c on  (d.channel_id=? and d.channel_id=c.id) order by d.id desc limit ?", new BeanListHandler<Data>(Data.class),channelId,top);
    			  }else if(pageNo>0&&pageSize>0)
    			  {
    				  //你要分页显示
    				  dataList=Db.query("select d.*,c.name as channel_name,(select inc.t_name from channel  inc where d.channel_id=inc.id) as t_name from alldata d inner join channel c on  (d.channel_id=? and d.channel_id=c.id) order by d.id desc limit ?,?", new BeanListHandler<Data>(Data.class),channelId,(pageNo-1)*pageSize,pageSize);
    			      Object obj=Db.query("select count(id) from alldata where channel_id=?", new ArrayHandler(),channelId)[0];
    			         
    					Long total=0l;
    					if(obj instanceof Long)
    					{
    						total=(Long)obj;
    					}else if(obj instanceof BigInteger)
    					{
    						total=((BigInteger)obj).longValue();
    					}
    				
    					pd=new PageDiv<Data>(pageNo, pageSize, total.intValue(), dataList);
    			  }
    		 }else
    		 {
    			  //取所有栏目的内容
    			  if(top>0)
	   			  {
	   				 //只取前top条
    				 dataList=Db.query("select d.*,c.name as channel_name,(select inc.t_name from channel  inc where d.channel_id=inc.id) as t_name from alldata d inner join channel c on d.channel_id=c.id order by d.id desc limit ?", new BeanListHandler<Data>(Data.class),top);
	   			  }else if(pageNo>0&&pageSize>0)
	   			  {
	   				//你要分页显示
	   				dataList=Db.query("select d.*,c.name as channel_name,(select inc.t_name from channel  inc where d.channel_id=inc.id) as t_name from alldata d inner join channel c on d.channel_id=c.id order by d.id desc limit ?,?", new BeanListHandler<Data>(Data.class),(pageNo-1)*pageSize,pageSize);
 				    Object obj=Db.query("select count(id) from alldata", new ArrayHandler())[0];
 					   
 					Long total=0l;
 					if(obj instanceof Long)
 					{
 						total=(Long)obj;
 					}else if(obj instanceof BigInteger)
 					{
 						total=((BigInteger)obj).longValue();
 					}
 					
 					pd=new PageDiv<Data>(pageNo, pageSize, total.intValue(), dataList);
	   			  }
    		 }
    		     		
    	     pageContext.setAttribute("pd", pd, PageContext.REQUEST_SCOPE);
    		 if(null!=dataList && dataList.size()>0)
    		 {
    			current=dataList.get(index++);
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
        if(null!=dataList && dataList.size()>1)
        {
        	if(index<dataList.size())
        	{
        		current=dataList.get(index++);
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
    	dataList=null;
    	pageNo=0;
    	pageSize=0;
    	channelId=0;
    	top=0;
    	
    	return Tag.EVAL_PAGE;
    }

    //填充 data_attr的扩展字段
    public void fillAttr(Data current) 
    {
    	try {
    		List<DataAttr> attrlist=Db.query("select * from  where data_id=?", new BeanListHandler<DataAttr>(DataAttr.class),current.getId());
    		if(null!=attrlist && attrlist.size()>0)
    		{
    			for(DataAttr ca:attrlist)
    			{
    				current.getAttrs().put(ca.getField_name(), ca.getField_value());
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
     }

	//-----------------------------
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Data getCurrent() {
		return current;
	}

	public void setCurrent(Data current) {
		this.current = current;
	}
	
}
