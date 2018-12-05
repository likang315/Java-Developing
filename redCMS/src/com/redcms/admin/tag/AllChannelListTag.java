package com.redcms.admin.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.redcms.beans.Channel;
import com.redcms.beans.Model;
import com.redcms.db.Db;

/**
 * 用来使栏目显示层次结构
 * @author likang
 *
 */
public class AllChannelListTag extends SimpleTagSupport
{ 
    private Logger log=Logger.getLogger(AllChannelListTag.class);
    
	@Override
	public void doTag() throws JspException, IOException 
	{
	    StringBuilder sb=new StringBuilder();
	    try {  
	    	//拿出所有父类目
	    	List<Channel> parent=Db.query("select * from channel where parent_id=0 or parent_id is null", new BeanListHandler<Channel>(Channel.class));		
			if(null!=parent && parent.size()>0)
			{
				int index=1;
				for(Channel c:parent)
				{
					sb.append("<tr>");
					sb.append("<td align='center'>"+(index++)+"</td>");
					sb.append("<td ><b>"+c.getName()+"</b></td>");
					
					Model mm=Db.query("select * from model where id=?", new BeanHandler<Model>(Model.class),c.getModel_id());
					sb.append("<td>"+mm.getName()+"</td>");
					
					sb.append("<td>"+c.getPriority()+"</td>");
					sb.append("<td>  <a class='btn btn-primary btn-sm' href='admin/channel?action=channelEdit&id="+c.getId()+"'>修改</a>");
					sb.append("&nbsp;<a class='btn btn-sm btn-danger'  href='admin/channel?action=channelDel&id="+c.getId()+"' >删除</a>");
					sb.append("&nbsp;<a class='btn btn-sm btn-success' href='admin/channel?action=pubChannel&id="+c.getId()+"&pageSize=20' >发布</a>");
					sb.append("</td>");
					sb.append("</tr>");
					
					//二级栏目
					List<Channel> subs=Db.query("select * from channel where parent_id=? order by priority", new BeanListHandler<Channel>(Channel.class),c.getId());
					
					if(null!=subs && subs.size()>0)
					{
						for(Channel s:subs)
						{
							sb.append("<tr>");
							sb.append("<td  align='center'>"+(index++)+"</td>");
							sb.append("<td>&nbsp;&nbsp;&nbsp;|--"+s.getName()+"</td>");
							
							Model mm2=Db.query("select * from model where id=?", new BeanHandler<Model>(Model.class),s.getModel_id());
							sb.append("<td>"+mm2.getName()+"</td>");
							
							sb.append("<td>"+s.getPriority()+"</td>");
							sb.append("<td><a   class='btn btn-info btn-xs' href='admin/channel?action=channelEdit&id="+s.getId()+"'>修改</a>");
							sb.append("&nbsp;<a class='btn btn-xs btn-warning' href='admin/channel?action=channelDel&id="+s.getId()+"'>删除</a>");
							
							sb.append("&nbsp;<a href='admin/channel?action=pubChannel&cid="+s.getId()+"&pageSize=20' class='btn btn-xs btn-primary'>发布</a>");
							sb.append("</td>");
							
							sb.append("</tr>");
						}
					}
					
					
				}
			}
			
		} catch (Exception e) {
			log.error("com.redcms.tag.admin.AllChannelListTag_ERROR_07_显示栏目列表出错"+e);
		}
	    
	    this.getJspContext().getOut().println(sb.toString());
	}

	
    
}
