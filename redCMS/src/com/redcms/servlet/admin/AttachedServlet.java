package com.redcms.servlet.admin;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.redcms.beans.Attachs;
import com.redcms.db.Db;
import com.redcms.db.PageDiv;
import com.redcms.servelt.core.Action;

@WebServlet("/admin/attach")
public class AttachedServlet extends Action {

	@Override
	public void index() throws ServletException, IOException 
	{
		int pageNo=this.getInt("pageNo");
		if(pageNo<1)
			pageNo=1;
		int pageSize=20;
		PageDiv<Attachs> page=null;
		
		try {
			   List<Attachs> list=Db.query("select * from attachs order by id desc limit ?,?", new BeanListHandler<Attachs>(Attachs.class),(pageNo-1)*pageSize,pageSize);
			   Object obj=Db.query("select count(id) from attachs", new ArrayHandler())[0];
			   
				Long total=0L;
				if(obj instanceof Long)
				{
					total=(Long)obj;
				}else if(obj instanceof BigInteger)
				{
					total=((BigInteger)obj).longValue();
				}
				
				page=new PageDiv<>(pageNo, pageSize, total.intValue(), list);
				setAttr("page", page);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.forword("admin/attach_list.jsp");
		
	}
	
	//批量删除
	public void del() throws ServletException, IOException 
	{
		String[] ids=this.getStringArray("ids");
		
		if(null!=ids && ids.length>0)
		{
			for(int i=0;i<ids.length;i++)
			{
				try {
					Attachs at=Db.query("select * from attachs where id=?", new BeanHandler<Attachs>(Attachs.class),Long.parseLong(ids[i]));					
					File f=new File(this.getServletContext().getRealPath(at.getPath()));
					f.delete();
					
					String sql="delete from attachs where id=?";
					Db.update(sql,Long.parseLong(ids[i]));
					
					setAttr("msg", "删除成功!");
				} catch (Exception e) {
					setAttr("err", "删除失败！");
				}
			}
			
		}
		index();
	} 

}
