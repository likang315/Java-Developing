package com.redcms.servlet.admin;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.beans.ChannelAttr;
import com.redcms.beans.Model;
import com.redcms.beans.ModelItem;
import com.redcms.db.Db;
import com.redcms.servelt.core.Action;
/**
 * 增加栏目
 * @author likang
 *
 */
@WebServlet("/admin/channel")
public class ChannelServlet extends Action {

	@Override
	public void index() throws ServletException, IOException
	{
		try {
			List<Model> models=Db.query("select * from model order by priority", new BeanListHandler<Model>(Model.class));
			setAttr("models", models);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		forword("admin/channel_list.jsp");
	}

	//跳转增加栏目页面
	public void toAdd() throws ServletException, IOException 
	{
		int mid=this.getInt("mid");
		if(mid>0)
		{
			try {
				//取出指定模型
				Model mo=Db.query("select * from model where id=?", new BeanHandler<Model>(Model.class),mid);
				//取出指定栏目模型字段并且是显示的
				List<ModelItem> modelItems=Db.query("select * from model_item where model_id=? and is_channel=1 and is_display=1 order by priority", new BeanListHandler<ModelItem>(ModelItem.class),mid);
				
				List<Channel> parentchannel=Db.query("select * from channel where parent_id=0 or parent_id is null", new BeanListHandler<Channel>(Channel.class));
				
				List<Model> models=Db.query("select * from model order by priority", new BeanListHandler<Model>(Model.class));
				
				setAttr("mo", mo);
				setAttr("modelItems", modelItems);
				setAttr("parentchannel", parentchannel);
				forword("admin/channel_add.jsp");
				
			} catch (SQLException e) {
				e.printStackTrace();
				index();
			}
			
		 }else
		 {
			  index();
		 }
	}

	
	public void addSave() throws ServletException, IOException 
	{
		//第一步：增加channel
		String sql="insert into channel(model_id,name,title,keywords,description,parent_id,pic01,pic02,priority,links,t_name,index_tem,list_tem,content_tem,create_time,txt,txt1,txt2,num01,num02,date1,date2) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Channel channel=new Channel();
		this.getBean(channel);
		channel.setCreate_time(new Date());
		
		try {
			Db.update(sql,channel.getModel_id(),channel.getName(),channel.getTitle(),channel.getKeywords(),channel.getDescription(),channel.getParent_id(),channel.getPic01(),channel.getPic02(),channel.getPriority(),channel.getLinks(),channel.getT_name(),channel.getIndex_tem(),channel.getList_tem(),channel.getContent_tem(),channel.getCreate_time(),
					channel.getTxt(),channel.getTxt1(),channel.getTxt2(),channel.getNum01(),channel.getNum02(),channel.getDate1(),channel.getDate2());
			
			//得到channel的最后id
			Object lastobj=Db.query("select LAST_INSERT_ID() from dual", new ArrayHandler())[0];
			long lastid=0;
			if(lastobj instanceof Long)
			{
				lastid=((Long)lastobj);
			}else if(lastobj instanceof BigInteger)
			{
				lastid=((BigInteger)lastobj).longValue();
			}
			
			
		 //第二步：更改pictures表  图集i
			for(int i=1;i<3;i++)
			{	
				String[] ids=req.getParameterValues("pics"+i+"_ids");
				String[] prio=req.getParameterValues("pics"+i+"_priority");
				String[] diss=req.getParameterValues("pics"+i+"_dis");

				if(null!=ids&&null!=prio&&null!=diss)
				{
					String sqlba="update pictures set channel_id=?,picdis=?,priority=?,sequ=? where id=?";
					Object[][] parasm=new Object[ids.length][];
					for(int z=0;z<ids.length;z++)
					{
						Object[] row=new Object[5];
						row[0]=lastid;
						row[1]=diss[z];
						row[2]=Integer.parseInt(prio[z]);
						row[3]=z;
						row[4]=ids[z];
						
						parasm[z]=row;
						
					}
					Db.batch(sqlba, parasm);
				}
			}			
			
			
			//第三步：增加额外字段，利用扩展字段
			
			
			//还需要增加扩展字段
			//查询那些是自定义字段
			//然后获取请求参数
			//如果有值，就写入扩展数据表中
			
			String sqlmol="select * from model_item where model_id=? and is_channel=1 and is_custom=0 order by priority";
			List<ModelItem> modelitemlist=Db.query(sqlmol, new BeanListHandler<ModelItem>(ModelItem.class),channel.getModel_id());
			if(null!=modelitemlist&&modelitemlist.size()>0)
			{
				//所有的自定义字段
			    List<Object[]> attrlist=new ArrayList<Object[]>();
				for(ModelItem mi:modelitemlist)
				{
					String insersql="insert into channel_attr(channel_id,field_name,field_value) values(?,?,?)";
					String value=req.getParameter(mi.getField());
					Object[] row=new Object[3];
					row[0]=lastid;
					row[1]=mi.getField();
					row[2]=value;
					
					attrlist.add(row);
					Db.update(insersql,row);
				}
			}
			
			setAttr("msg", "增加栏目成功");
		
		} catch (SQLException e) {
			setAttr("err", "增加栏目失败");
			e.printStackTrace();
		}
		
		index();
    }
	
	
}
