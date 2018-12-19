package com.redcms.servlet.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.db.Db;
import com.redcms.servelt.core.Action;
/**
 * 栏目首页面，跳转到new_index.jsp
 * @author likang
 *
 */
@WebServlet("/web/channelindex")
public class ChannalIndexServlet extends Action {

	@Override
	public void index() throws ServletException, IOException 
	{
		int channelId=this.getInt("channelId");//得到一个ChannelID
		if(channelId>0)
		{
		    //只有一级栏目有栏目首页面
			Channel channel=null;
			try {
				channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channelId);
		        List<Channel> subs=Db.query("select * from channel where parent_id=? order by priority", new BeanListHandler<Channel>(Channel.class),channelId);
		        setAttr("subs", subs);
		        
				String indexPath=channel.getIndex_tem();
		        if(null!=indexPath&&(!"".equals(indexPath))&&(!"-1".equals(indexPath)))
		        {
		        	setAttr("channel", channel);
		        	this.forword("template/"+indexPath+".jsp");
		        }
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
