package com.redcms.servlet.admin;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.beans.Data;
import com.redcms.beans.DataAttr;
import com.redcms.beans.Model;
import com.redcms.beans.ModelItem;
import com.redcms.beans.Pictures;
import com.redcms.db.Db;
import com.redcms.db.PageDiv;
import com.redcms.idcreater.IdWorker;
import com.redcms.servelt.core.Action;


@WebServlet("/admin/article")
public class ArticleServlet extends Action {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 文章显示页面，含分页操作
	 */
	@Override
	public void index() throws ServletException, IOException 
	{
		try {
			int pageNo=this.getInt("pageNo");
			int channelId=this.getInt("channelId");
			
			if(pageNo==0)
				pageNo=1;
			int pageSize=8;
			PageDiv<Data> page =null;
			 
			 if(channelId>0)
			 {
				  //先得到channel
				  Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channelId);
				  //在指定的表中查出指定栏目的字段
				  String sqlorg="select d.id,d.title,d.channel_id,d.createtime,c.name as channel_name,(select inc.t_name from channel  inc where d.channel_id=inc.id) as t_name from "+channel.getT_name()+" d inner join channel c on (d.channel_id=? and d.channel_id=c.id) order by d.id desc limit ?,?";
				  
				  List<Data> pagelist=Db.query(sqlorg, new BeanListHandler<Data>(Data.class),channelId,(pageNo-1)*pageSize,pageSize);
				    
				  Object obj=Db.query("select count(id) from "+channel.getT_name()+" where channel_id=?", new ArrayHandler(),channelId)[0];
				   
					Long total=0L;
					if(obj instanceof Long)
					{
						total=(Long)obj;
					}else if(obj instanceof BigInteger)
					{
						total=((BigInteger)obj).longValue();
					}
					
					page=new PageDiv<Data>(pageNo, pageSize, total.intValue(), pagelist);
					setAttr("channel", channel); 
					
			 }else
			 {
				 	//从所有内容表中查出一页内容
				    String sql="select d.id,d.title,d.channel_id,d.createtime,c.name as channel_name,(select inc.t_name from channel  inc where d.channel_id=inc.id) as t_name from alldata d inner join channel c on d.channel_id=c.id order by d.id desc limit ?,?";
				    List<Data> pagelist=Db.query(sql, new BeanListHandler<Data>(Data.class),(pageNo-1)*pageSize,pageSize);
				    
				    Object obj=Db.query("select count(id) from alldata", new ArrayHandler())[0];
				 
					Long total=0L;
					if(obj instanceof Long)
					{
						total=(Long)obj;
					}else if(obj instanceof BigInteger)
					{
						total=((BigInteger)obj).longValue();
					}
					
					page=new PageDiv<Data>(pageNo, pageSize, total.intValue(), pagelist);
			  }

			  setAttr("channelId", channelId);
			  setAttr("page", page);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		forword("admin/data_list.jsp");
	}
	
	
	
	//跳转到增加 
	public void toAdd() throws ServletException, IOException {
		
		   int channelId=this.getInt("channelId");
		   try {
			Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channelId);
			   List<ModelItem> modelItems=Db.query("select * from model_item where model_id=? and is_channel=0 and is_display=1 order by priority", new BeanListHandler<ModelItem>(ModelItem.class),channel.getModel_id());		
			   setAttr("channel", channel);
			   setAttr("modelItems", modelItems);
			   forword("admin/data_add.jsp");
		} catch (SQLException e) {
			   index();
			e.printStackTrace();
		}
		
	}
	
	//保存增加
	public void saveAdd() throws ServletException, IOException
	{
         try {
			Data data=new Data();
			 this.getBean(data);
			 data.setCreatetime(new Date());
			 
			 long lastid=IdWorker.getId();
			 data.setId(lastid);
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

				if(null!=ids&&null!=prio&&null!=diss&&ids.length==prio.length&&ids.length==diss.length)
				{
				
					String sqlba="update pictures set data_id=?,picdis=?,priority=?,sequ=? where id=?";
					Object [][]parasm=new Object[ids.length][];
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
		    
//第三步：增加额外字段
			
			//还需要增加扩展字段
			//查询吧些是自定义字段
			//然后获取请求参数
			//如果有值，就写入扩展数据表中
			String sqlmol="select * from model_item where model_id=? and is_channel=0 and is_custom=1 order by priority";
			List<ModelItem> modelitemlist=Db.query(sqlmol, new BeanListHandler<ModelItem>(ModelItem.class),channel.getModel_id());
			if(null!=modelitemlist&&modelitemlist.size()>0)
			{
				Object[][]params=new Object[modelitemlist.size()][];
				
			   List<Object[]> attrlist=new ArrayList<Object[]>();
			 
				for(ModelItem mi:modelitemlist)
				{
					  String insersql="insert into data_attr(data_id,field_name,field_value) values(?,?,?)";
					String value=req.getParameter(mi.getField());
					Object[]row=new Object[3];
					row[0]=lastid;
					row[1]=mi.getField();
					row[2]=value;
					
					attrlist.add(row);
					Db.update(insersql,row);
				}
				
				
			}
		    
		    setAttr("msg","增加文章成功!");
         } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       index();
	}
	
	//跳转到个页面
	public void toEdit() throws ServletException, IOException
	{
		long dataid=this.getLong("dataId");
		String tname=this.getString("tName");
		
		try {
			if(tname.matches("data[1-4]{1}")) 
			{
				Data data=Db.query("select * from "+tname+" where id=?", new BeanHandler<Data>(Data.class),dataid);
				data.setT_name(tname);
				 Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),data.getChannel_id());
				 Model model=Db.query("select * from model where id=?", new BeanHandler<Model>(Model.class),channel.getModel_id());
				 List<ModelItem> modelItems=Db.query("select * from model_item where model_id=? and is_channel=0 and is_display=1 order by priority", new BeanListHandler<ModelItem>(ModelItem.class),channel.getModel_id());		
		
			
				//额外字段
		          List<DataAttr>  mapattr=Db.query("select * from data_attr where data_id=?",new BeanListHandler<DataAttr>(DataAttr.class),dataid);
		          Map<String,String> dataattr=new HashMap<String,String>();
		          if(null!=mapattr)
		          for(DataAttr ca:mapattr)
		          {
		          	
		          	dataattr.put(ca.getField_name(), ca.getField_value());
		          }
		          
		        //图集一
		    		List<Pictures> pics1=Db.query("select * from pictures where data_id=? and sequ=1", new BeanListHandler<Pictures>(Pictures.class),dataid);//this.getiPicturesService().selectList(new EntityWrapper<Pictures>().eq("data_id", dataId).eq("sequ", 1));
		    		//图集二
		    		List<Pictures> pics2=Db.query("select * from pictures where data_id=? and sequ=2", new BeanListHandler<Pictures>(Pictures.class),dataid);
		    		
		    		 setAttr("dataattr", dataattr);
		    		 setAttr("pics1", pics1);
		    		 setAttr("pics2", pics2);
		    		//mod.addAttribute("mo", model);
		    		 setAttr("modelItems", modelItems);
		    	//	mod.addAttribute("parentchannel", parentchannel);
		    		 setAttr("channel",channel);
		            
		    		 setAttr("channel", channel);
		    		 setAttr("modelItems", modelItems);
		    		 setAttr("data", data);
		    	         
		  		
			}
			  forword("admin/data_edit.jsp");
			
		} catch (Exception e) {
			e.printStackTrace();
			index();
		}
		
	}
	
	//保存修改
	public void editSave() throws ServletException, IOException
	{
		int channelId=this.getInt("channel_id");
		Data data=new Data();
		this.getBean(data);
		data.setCreatetime(new Date());
		try {
			 Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),channelId);
			  //修改数据表，完成分表
			 String sql="update "+data.getT_name()+" set channel_id=?,title=?,tags=?,author=?,level=?,txt1=?,txt2=?,dis=?,state=?,createtime=?,pic1=?,pic2=?,pic3=?,links=?,c1=?,c2=?,c3=?,c4=?,n1=?,n2=?,n3=?,d1=?,d2=?,attach1=?,attach2=?,content_tem=? where id=?";
			 Db.update(sql,data.getChannel_id(),data.getTitle(),data.getTags(),data.getAuthor(),data.getLevel(),data.getTxt1(),data.getTxt2(),data.getDis(),data.getState(),data.getCreatetime(),data.getPic1(),data.getPic2(),data.getPic3(),data.getLinks(),data.getC1(),data.getC2(),data.getC3(),data.getC4(),data.getN1(),data.getN2(),data.getN3(),data.getD1(),data.getD2(),data.getAttach1(),data.getAttach2(),data.getContent_tem(),data.getId());
			  
				//删除扩展字段
			 Db.update("delete from data_attr where data_id=?",data.getId());
				
			//还需要增加扩展字段
				//查询吧些是自定义字段
				//然后获取请求参数
				//如果有值，就写入扩展数据表中
				String sqlmol="select * from model_item where model_id=? and is_channel=0 and is_custom=1 order by priority";
				List<ModelItem> modelitemlist=Db.query(sqlmol, new BeanListHandler<ModelItem>(ModelItem.class),channel.getModel_id());
				if(null!=modelitemlist&&modelitemlist.size()>0)
				{
					Object[][]params=new Object[modelitemlist.size()][];
					
				   List<Object[]> attrlist=new ArrayList<Object[]>();
				 
					for(ModelItem mi:modelitemlist)
					{
						  String insersql="insert into data_attr(data_id,field_name,field_value) values(?,?,?)";
						String value=req.getParameter(mi.getField());
						Object[]row=new Object[3];
						row[0]=data.getId();
						row[1]=mi.getField();
						row[2]=value;
						
						attrlist.add(row);
						Db.update(insersql,row);
					}
					
					
				}
				//需要操作图集
				for(int i=1;i<3;i++)
				{
					String [] ids=req.getParameterValues("pics"+i+"_ids");
					String [] prio=req.getParameterValues("pics"+i+"_priority");
					String [] diss=req.getParameterValues("pics"+i+"_dis");

					if(null!=ids&&null!=prio&&null!=diss&&ids.length==prio.length&&ids.length==diss.length)
					{
					
						String sqlba="update pictures set data_id=?,picdis=?,priority=?,sequ=? where id=?";
						Object [][]parasm=new Object[ids.length][];
						for(int z=0;z<ids.length;z++)
						{
							Object[] row=new Object[5];
							row[0]=data.getId();
							row[1]=diss[z];
							row[2]=Integer.parseInt(prio[z]);
							row[3]=i;
							row[4]=ids[z];
							
							parasm[z]=row;
							
						}
						
						Db.batch(sqlba, parasm);
					}
				}
				setAttr("msg", "修改成功!");
			 
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("err", "修改失败!");
		}

		index();
	}
	
	//删除
	public void delete() throws ServletException, IOException
	{
		long dataid=this.getLong("dataId");
		if(dataid>0)
		{
		 try {
			
			    //删除数据扩展字段
			 Db.update("delete from data_attr where data_id=?",dataid);
			    
				
				//删除图集
				Db.update("delete from pictures where data_id=?",dataid);
				
			
				String sql="delete from "+this.getString("tName")+" where id=?";
				System.out.println(sql+"--->");
				Db.update(sql,dataid);
				
				
				
			    setAttr("channelId",this.getInt("channelId"));
				setAttr("msg", "删除成功!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  		
		}
		index();
	}
}
