package com.redcms.servlet.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.beans.Data;
import com.redcms.beans.DataAttr;
import com.redcms.db.Db;
import com.redcms.servelt.core.Action;

/**
 * 栏目内容页
 * @author likang
 *
 */
@WebServlet("/web/channelContent")
public class ChannelContentServlet extends Action {

	@Override
	public void index() throws ServletException, IOException 
	{
		long dataId=this.getLong("dataId");
		setAttr("dataId", dataId);
		try {
			if(dataId>0)
			{
				
				Data data=Db.query("select * from alldata where id=?", new BeanHandler<Data>(Data.class),dataId);
				fillAttr(data);
				
				Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),data.getChannel_id());
				Channel parent=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channel.getParent_id());
				
				String contentpath=data.getContent_tem();
				if(null!=contentpath&&(!"".equals(contentpath))&&(!"-1".equals(contentpath)))
		        {
			          setAttr("data", data);
			          setAttr("parent", parent);
			          setAttr("channel", channel);
			          this.forword("template/"+contentpath+".jsp");
		        }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

    //填充文章扩展字段
    public void fillAttr(Data current) 
    {
    	try {
    		//填充扩展字段
    		List<DataAttr> attrlist=Db.query("select * from data_attr where data_id=?", new BeanListHandler<DataAttr>(DataAttr.class),current.getId());
    		if(null!=attrlist&&attrlist.size()>0)
    		{
    			for(DataAttr ca:attrlist)
    			{
    				current.getAttrs().put(ca.getField_name(), ca.getField_value());
    			}
    		}
    	    attrlist=null;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
     }

}
