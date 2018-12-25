package com.redcms.servlet.admin;

import java.io.File;
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
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Channel;
import com.redcms.beans.ChannelAttr;
import com.redcms.beans.Model;
import com.redcms.beans.ModelItem;
import com.redcms.beans.Pictures;
import com.redcms.db.Db;
import com.redcms.servelt.core.Action;
import com.redcms.util.HtmlGenerator;

/**
 * 增加栏目,以及栏目的修改，删除，发布（后续功能）
 * @author likang
 */
@WebServlet("/admin/channel")
public class ChannelServlet extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	//跳转栏目首页
	@Override
	public void index() throws ServletException, IOException
	{		try {
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

	//增加栏目，并且更新图片
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
						row[3]=i;
						row[4]=ids[z];
						parasm[z]=row;
					}
					Db.batch(sqlba, parasm);
				}
			}			
			
			//第三步：增加额外字段，利用扩展字段
			//还需要增加扩展字段
			//查询那些是自定义字段
			//如果有值，就写入扩展数据表中后获取请求参数
			
			
			String sqlmol="select * from model_item where model_id=? and is_channel=1 and is_custom=1 order by priority";
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
	
	//跳转到修改界面，携带各种信息
	public void channelEdit() throws ServletException, IOException
	{
		int id=this.getInt("id");
		try {
			if(id>0)
			{
				Channel channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),id);
				Model mo=Db.query("select * from model where id=?", new BeanHandler<Model>(Model.class),channel.getModel_id());
				List<ModelItem> modelItems=Db.query("select * from model_item where model_id=? and is_channel=1 and is_display=1 order by priority", new BeanListHandler<ModelItem>(ModelItem.class),mo.getId());
				
				//额外字段,放进map
				List<ChannelAttr> mapattr=Db.query("select * from channel_attr where channel_id=?", new BeanListHandler<ChannelAttr>(ChannelAttr.class),id);

		        Map<String,String> channelattr=new HashMap<String,String>();
		        if(null!=mapattr)
		        for(ChannelAttr ca:mapattr)
		        {
		        	channelattr.put(ca.getField_name(), ca.getField_value());
		        }
				
		        //父栏目
		        List<Channel> parentchannel=Db.query("select * from channel where parent_id=0 or parent_id is null", new BeanListHandler<Channel>(Channel.class));
		        //图集一
				List<Pictures> pics1=Db.query("select * from pictures where channel_id=? and sequ=1", new BeanListHandler<Pictures>(Pictures.class),id);
				//图集二
				List<Pictures> pics2=Db.query("select * from pictures where channel_id=? and sequ=2", new BeanListHandler<Pictures>(Pictures.class),id);
				
				setAttr("channalattr", channelattr);
				setAttr("pics1", pics1);
				setAttr("pics2", pics2);
				setAttr("mo", mo);
				setAttr("modelItems", modelItems);
				setAttr("parentchannel", parentchannel);
				setAttr("channel",channel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			index();
		}

		this.forword("admin/channel_edit.jsp");
	}

	//修改栏目
	public void editSave() throws ServletException, IOException 
	{
		try {
			
			//修改栏目的字段
			Channel channel=new Channel();
			this.getBean(channel);
			channel.setCreate_time(new Date());
			String sql="update channel set model_id=?,name=?,title=?,keywords=?,description=?,parent_id=?,pic01=?,pic02=?,priority=?,links=?,t_name=?,index_tem=?,list_tem=?,content_tem=?,create_time=?,txt=?,txt1=?,txt2=?,num01=?,num02=?,date1=?,date2=? where id=?";
			Object rowparam[]=new Object[] {channel.getModel_id(),channel.getName(),channel.getTitle(),channel.getKeywords(),channel.getDescription(),channel.getParent_id(),channel.getPic01(),channel.getPic02(),channel.getPriority(),channel.getLinks(),channel.getT_name(),channel.getIndex_tem(),channel.getList_tem(),channel.getContent_tem(),channel.getCreate_time(),
					channel.getTxt(),channel.getTxt1(),channel.getTxt2(),channel.getNum01(),channel.getNum02(),channel.getDate1(),channel.getDate2(),channel.getId()};
			System.out.println(channel.getT_name());
			Db.update(sql,rowparam);
			
			
			
			//修改扩展字段，先删除然后找到自定义字段，新增加一条
			Db.update("delete from channel_attr where channel_id=?",channel.getId());
			String sqlmol="select * from model_item where model_id=? and is_channel=1 and is_custom=1 order by priority";
			List<ModelItem> modelitemlist=Db.query(sqlmol, new BeanListHandler<ModelItem>(ModelItem.class),channel.getModel_id());		
			if(null!=modelitemlist && modelitemlist.size()>0)
			{
			    String insersql="insert into channel_attr(channel_id,field_name,field_value) values(?,?,?)";
				for(ModelItem mi:modelitemlist)
				{ 
					String value=req.getParameter(mi.getField());
					Object[] row=new Object[3];
					row[0]=channel.getId();
					row[1]=mi.getField();
					row[2]=value;
					Db.update(insersql,row);
				}
			}

			
			//修改图集pictures
			for(int i=1;i<3;i++)
			{
				String [] ids=req.getParameterValues("pics"+i+"_ids");
				String [] prio=req.getParameterValues("pics"+i+"_priority");
				String [] diss=req.getParameterValues("pics"+i+"_dis");

				if(null!=ids && null!=prio && null!=diss)
				{
					String sqlba="update pictures set channel_id=?,picdis=?,priority=?,sequ=? where id=?";
					Object[][] parasm=new Object[ids.length][];
					for(int z=0;z<ids.length;z++)
					{
						Object[] row=new Object[5];
						row[0]=channel.getId();
						row[1]=diss[z];
						row[2]=Integer.parseInt(prio[z]);
						row[3]=i;
						row[4]=ids[z];
						parasm[z]=row;
					}
					
					Db.batch(sqlba, parasm);
				}
			}	
		setAttr("msg", "修改栏目成功!");
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		index();
	}
	
	//删除栏目
	public void channelDel() throws ServletException, IOException 
	{
		int id=this.getInt("id");
		if(id>0)
		{
			  try {
				//删除栏目的扩展字段
				Db.update("delete from channel_attr where channel_id=?",id);
				//删除栏目的图集
				Db.update("delete from pictures where channel_id=?",id);
				//删除栏目子栏目，子栏目的父id就是父栏目的id
				Db.update("delete from channel where parent_id=?",id);
				//删除自己
				Db.update("delete from channel where id=?",id);
				setAttr("msg", "删除栏目成功!");
				
			} catch (SQLException e) {
				setAttr("err", "删除栏目失败！");
				e.printStackTrace();
			}
		    
		}
	      
		index();
	}
	
	
	/**
	 * 发布一个目录下的文章
	 * @throws ServletException
	 * @throws IOException
	 */
	public void pubchannel()throws ServletException, IOException 
	{
		int cid=this.getInt("cid");
		int pageSize=20;
		if(this.getInt("pageSize")>0)
			pageSize=this.getInt("pageSize");
		
		try {
			pubUtils(cid,pageSize);
			setAttr("msg", "发布成功");
			
		} catch (Exception e) 
		{
			setAttr("err", "发布失败");		
		}
		
		index();
	}
	
	/**
	 * 生成静态功能，所有栏目下的文章
	 * @throws ServletException
	 * @throws IOException
	 */
	public void pubAllchannel() throws ServletException, IOException 
	{
	  try {
		  	int pageSize=20;
			if(this.getInt("pageSize")>0)
				pageSize=this.getInt("pageSize");
			
		List<Object[]> allc=Db.query("select id from channel",new ArrayListHandler());
		for(Object[] tem:allc)
		{
			BigInteger cid=(BigInteger)tem[0];
			pubUtils(cid.intValue(),pageSize);
		}
		setAttr("msg", "发布成功");
		
	} catch (SQLException e) {
		setAttr("err", "发布失败");
		
	}
	  index();
	}
	
	
	/**
	 * 发布的栏目的工具类,参数为栏目id和页面大小
	 */
	public void pubUtils(int cid,int pageSize)throws SQLException
	{
		 String basePath=req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/";
		 Channel channel=null;
	
		if(cid>0)
		{	 
			 String indexPath=basePath+"web/channelindex?channelId="+cid;
			 channel=Db.query("select * from channel where id=?", new BeanHandler<Channel>(Channel.class),cid);
			 
			 //取一级栏目(栏目首页面)
			 if(null!=channel.getIndex_tem()&&channel.getIndex_tem().endsWith("index"))
			 {
				 //有首页文件
				 String targfile=req.getServletContext().getRealPath("html/channel_"+cid);
				 File f=new File(targfile);
				 if(!f.exists())
					 f.mkdirs();
				 
				 HtmlGenerator.createHtmlPage(indexPath, targfile+"/index.html");
			 }
			 
			 //栏目列表页，有分页查询
			 if(null!=channel.getList_tem()&&channel.getList_tem().endsWith("list"))
			 {
				 String sql="select count(id) from "+channel.getT_name()+" where channel_id=?";
				 Long totalcount=(Long)Db.query(sql, new ArrayHandler(),cid)[0];
				 
				 int totalPage=(totalcount.intValue()+pageSize-1)/pageSize;
				 
				 String targfile=req.getServletContext().getRealPath("html/channel_"+cid);
				 File f=new File(targfile);
				 if(!f.exists())
					 f.mkdirs();
				 
				 for(int i=1;i<=totalPage;i++)
				 {
					 String listpath=basePath+"web/channelList?channelId="+cid+"&pageNo="+i;
					 HtmlGenerator.createHtmlPage(listpath, targfile+"/list_"+i+".html");
				 }
				
			 }
			 setAttr("msg", "发布成功");
		}

	}
	
}
