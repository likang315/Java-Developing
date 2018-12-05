package com.redcms.servlet.admin;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.beans.Data;
import com.redcms.beans.ModelItem;
import com.redcms.db.Db;
import com.redcms.db.PageDiv;
import com.redcms.idcreater.IdWorker;
import com.redcms.servelt.core.Action;


@WebServlet("/admin/article")
public class ArticleServlet extends Action {

	@Override
	public void index() throws ServletException, IOException {
		/* if(pageNo==0)pageNo=1;
		 int pageSize=12;
		 Page<Data> page =null;
		  if(channelId==0)
		  {
				page=this.getiDataService().selectAllData(new Page(pageNo,pageSize));
				
		  }else
		  {
			    Channel channel= this.getiChannelService().selectById(channelId);
				mod.addAttribute("channel", channel); 
		
				 page =this.getiDataService().selectByChannel(new Page(pageNo,pageSize),channel);
				
				
		  }
		  mod.addAttribute("channelId", channelId);
		  mod.addAttribute("page", page);
		  
		  int indexCount=9;//总共
		  int startPage=(page.getCurrent()-indexCount/2)<1?1:(page.getCurrent()-indexCount/2);
		  int endPage=(page.getCurrent()+indexCount/2)>page.getTotal()?page.getTotal():(page.getCurrent()+indexCount/2);*/
		 
		forword("admin/data_list.jsp");
	}
	
	
	public void list() throws ServletException, IOException {
		  try {
			 int pageNo=this.getInt("pageNo");
			 if(pageNo==0)
				 pageNo=1;
			 int pageSize=12;
			 PageDiv<Data> page =null;
			 int channelId=this.getInt("channel_id");
			 
			  if(channelId==0)
			  {
				  //查询所有的内容，不分栏目，页面上没有增加的按纽
					//page=this.getiDataService().selectAllData(new Page(pageNo,pageSize));
					
			  }else
			  {
				  //指定查询哪个栏目的内容，页面上有增加按纽
				  
				  
				    Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channelId);
					setAttr("channel", channel); 
			
					// page =this.getiDataService().selectByChannel(new Page(pageNo,pageSize),channel);
					
					
			  }
			  setAttr("channelId", channelId);
			  setAttr("page", page);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		// int indexCount=9;//总共
		 // int startPage=(page.getCurrent()-indexCount/2)<1?1:(page.getCurrent()-indexCount/2);
		  //int endPage=(page.getCurrent()+indexCount/2)>page.getTotal()?page.getTotal():(page.getCurrent()+indexCount/2);
		 
		forword("admin/data_list.jsp");
		
	}
	
	
	//跳转到增加新文章 
	public void toAdd() throws ServletException, IOException {
		
		   int channelId=this.getInt("channelId");
		   try {
			   Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channelId);
			   List<ModelItem> modelItems=Db.query("select * from model_item where model_id=? and is_channel=0 and is_display=1 order by priority", new BeanListHandler<ModelItem>(ModelItem.class),channel.getModel_id());		
			   
			   setAttr("channel", channel);
			   setAttr("modelItems", modelItems);
			   forword("admin/data_add.jsp");
			   
		} catch (SQLException e) {
			   list();
			   e.printStackTrace();
		}	
	}
	
	//保存增加新文章
	public void saveAdd() throws ServletException, IOException
	{
         try {
			 Data data=new Data();
			 this.getBean(data);
			 data.setCreatetime(new Date());
			 
			 //分表查询，所有的表产生唯一的id
			 long lastid=IdWorker.getId();
			 data.setId(lastid);
			 //每个栏目对应一个表
			 Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),data.getChannel_id());
		     data.setT_name(channel.getT_name());
         
		     String sqlorg="insert into %s(id,channel_id,title,tags,author,level,txt1,txt2,dis,state,createtime,pic1,pic2,pic3,links,c1,c2,c3,c4,n1,n2,n3,d1,d2,attach1,attach2,content_tem) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		     String sql=String.format(sqlorg,data.getT_name());
          
		     Db.update(sql,data.getId(),data.getChannel_id(),data.getTitle(),data.getTags(),data.getAuthor(),data.getLevel(),data.getTxt1(),data.getTxt2(),data.getDis(),data.getState(),data.getCreatetime(),data.getPic1(),data.getPic2(),data.getPic3(),data.getLinks(),data.getC1(),data.getC2(),data.getC3(),data.getC4(),data.getN1(),data.getN2(),data.getN3(),data.getD1(),data.getD2(),data.getAttach1(),data.getAttach2(),data.getContent_tem());
		    
		    
			//需要操作图集
			for(int i=1;i<3;i++)
			{
				String [] ids=req.getParameterValues("pics"+i+"_ids");
				String [] prio=req.getParameterValues("pics"+i+"_priority");
				String [] diss=req.getParameterValues("pics"+i+"_dis");

				if(null!=ids&&null!=prio&&null!=diss)
				{
				
					String sqlba="update pictures set data_id=?,picdis=?,priority=?,sequ=? where id=?";
					Object[][] parasm=new Object[ids.length][];
					for(int z=0;z<ids.length;z++)
					{
						Object[] row=new Object[5];
						row[0]=lastid;
						row[1]=diss[z];
						row[2]=Integer.parseInt(prio[z]);
						row[3]=i;
						row[4]=ids[z];
						
						parasm[z]=row;
					}
					
					Db.batch(sqlba, parasm);
				}
			}
		    
			
			
            //第三步：增加额外内容字段
			//还需要增加扩展字段
			//查询那些是自定义字段
			//然后获取请求参数
			//如果有值，就写入扩展数据表中
			
			String sqlmol="select * from model_item where model_id=? and is_channel=0 and is_custom=1 order by priority";
			List<ModelItem> modelitemlist=Db.query(sqlmol, new BeanListHandler<ModelItem>(ModelItem.class),channel.getModel_id());
			
			if(null!=modelitemlist&&modelitemlist.size()>0)
			{	
			   
				for(ModelItem mi:modelitemlist)
				{
					String insersql="insert into data_attr(data_id,field_name,field_value) values(?,?,?)";
					String value=req.getParameter(mi.getField());
					Object[] row=new Object[3];
					row[0]=lastid;
					row[1]=mi.getField();
					row[2]=value;
					
					Db.update(insersql,row);
				}
				
			}
				    
		    setAttr("msg","增加文章成功!");
         } catch (SQLException e) {
        	setAttr("err", "增加文章失败");
			e.printStackTrace();
		}
         list();
	}

	
}
