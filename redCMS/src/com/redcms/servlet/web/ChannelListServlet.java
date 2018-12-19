package com.redcms.servlet.web;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.beans.Data;
import com.redcms.db.Db;
import com.redcms.db.PageDiv;
import com.redcms.servelt.core.Action;
/**
 * 栏目列表页，跳转到new_list.jsp
 * @author likang
 *
 */
@WebServlet("/web/channelList")
public class ChannelListServlet extends Action {

	@Override
	public void index() throws ServletException, IOException 
	{
		int pageNo=1;
		if(this.getInt("pageNo")>1)
			pageNo=this.getInt("pageNo");
	    setAttr("pageNo", pageNo);
	    
		int channelId=this.getInt("channelId");//得到子栏目ChannelID
		if(channelId>0)
		{
	        //二级栏目
			Channel channel=null;
			try {
				channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channelId);
				Channel parent=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channel.getParent_id());
				setAttr("parent", parent);
				
				String listPath=channel.getList_tem();
		        if(null!=listPath&&(!"".equals(listPath))&&(!"-1".equals(listPath)))
		        {
		          setAttr("channel", channel);
		          this.forword("template/"+listPath+".jsp");
		        }
			} catch (Exception e) {
				 e.printStackTrace();
			}
		}
		
	}

}
