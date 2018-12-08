package com.redcms.servlet.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Model;
import com.redcms.beans.ModelItem;
import com.redcms.db.Db;
import com.redcms.servelt.core.Action;
/**
 * 模型字段列表管理
 * @author likang
 *
 */
@WebServlet("/admin/modelItem")
public class ModelItemServlet extends Action {

	@Override
	public void index() throws ServletException, IOException 
	{
		
	}
	
	/**
	 * 内容字段或者栏目字段管理,把某个model的所有字段拿出来，然后跳转到modelItem_list.jsp显示
	 * @throws ServletException
	 * @throws IOException
	 */
	public void channelList() throws ServletException, IOException 
	{
	
		int ischannel=null!=req.getAttribute("ischannel")?(Integer)req.getAttribute("ischannel"):this.getInt("ischannel");
		int modelId=null!=req.getAttribute("model_id")?((Long)req.getAttribute("model_id")).intValue():this.getInt("model_id");
	
		if(modelId>0)
		{
			try {
				String sql="select * from model_item where model_id=? and is_channel=?  order by id";
				List<ModelItem> showlist=Db.query(sql, new BeanListHandler<ModelItem>(ModelItem.class),modelId,ischannel);
		
			    setAttr("showlist", showlist);
			    setAttr("ischannel", ischannel);
			     
			    Model model = Db.query("select * from model where id=?", new BeanHandler<Model>(Model.class),modelId);
			    setAttr("model", model);
			    
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.forword("admin/modelItem_list.jsp");
		}
		
	}
	
	
	/**
	 * 往 model_item 中增加一个自定义的字段
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addSave() throws ServletException, IOException 
	{
		   ModelItem mi=new ModelItem();
		   this.getBean(mi); //填充model_item
		  
		   //不选时，它的值时空，最后转成 -1
		   if(mi.getIs_display()==-1)
			   mi.setIs_display(0);
		   if(mi.getIs_required()==-1)
			   mi.setIs_required(0);
		   if(mi.getIs_single()==-1)
			   mi.setIs_single(0);
		   if(mi.getIs_custom()==-1)
			   mi.setIs_custom(1);
		   
		try {
				String sql="insert into model_item(model_id,field,field_dis,priority,def_value,opt_value,txt_size,help_info,data_type,is_single,is_channel,is_custom,is_display,is_required) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			    Db.update(sql,mi.getModel_id(),mi.getField(),mi.getField_dis(),mi.getPriority(),mi.getDef_value(),mi.getOpt_value(),mi.getTxt_size(),mi.getHelp_info(),mi.getData_type(),mi.getIs_single(),mi.getIs_channel(),mi.getIs_custom(),mi.getIs_display(),mi.getIs_required());
				setAttr("ischannel", mi.getIs_channel());
				setAttr("id", mi.getModel_id());
				setAttr("msg", "增加自定义字段成功！");
				
			} catch (SQLException e) {
				setAttr("err", "增加自定义字段失败！");
				e.printStackTrace();
			}
	
			channelList();

	}
	
	/**
	 * 修改每个字段
	 * @throws ServletException
	 * @throws IOException
	 */
	public void update() throws ServletException, IOException 
	{
		   ModelItem mi=new ModelItem();
		   this.getBean(mi);
		   
		   if(mi.getIs_display()==-1)
			   mi.setIs_display(0);
		   if(mi.getIs_required()==-1)
			   mi.setIs_required(0);
		   if(mi.getIs_single()==-1)
			   mi.setIs_single(0);
		
		try {
				String sql="update model_item set model_id=?,field_dis=?,priority=?,def_value=?,opt_value=?,txt_size=?,help_info=?,data_type=?,is_single=?,is_channel=?,is_custom=?,is_display=?,is_required=? where id=?";
			    Db.update(sql,mi.getModel_id(),mi.getField_dis(),mi.getPriority(),mi.getDef_value(),mi.getOpt_value(),mi.getTxt_size(),mi.getHelp_info(),mi.getData_type(),mi.getIs_single(),mi.getIs_channel(),mi.getIs_custom(),mi.getIs_display(),mi.getIs_required(),mi.getId());
				
			    setAttr("ischannel", mi.getIs_channel());
				setAttr("id", mi.getModel_id());
				setAttr("msg", "修改自定义字段成功！");
				
			} catch (SQLException e) {
				setAttr("err", "修改自定义字段失败！");
				e.printStackTrace();
			}
			
			channelList();
	}
	
	/**
	 * 批量隐藏
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateBatchId() throws ServletException, IOException 
	{	
		//action=updateBatchId&modelId=1&ischannel=0&miid=42&miid=43
		try {
			String[] values=req.getParameterValues("miid");
			
			String sql="update model_item set is_display=0 where id=?";
			
			Object[][] params=new Object[values.length][];
			for(int i=0;i<params.length;i++)
			{
				params[i]=new Object[] {Integer.parseInt(values[i])};
			}
			Db.batch(sql, params);
			
			setAttr("ischannel",this.getInt("ischannel"));
			setAttr("model_id", Long.parseLong(this.getString("model_id")));	
			setAttr("msg", "批量隐藏成功！");
			
		} catch (Exception e) {
			setAttr("err", "批量隐藏失败！");
			e.printStackTrace();
		} 
		
		channelList();
	}
	
	
	
	
	
	
}
