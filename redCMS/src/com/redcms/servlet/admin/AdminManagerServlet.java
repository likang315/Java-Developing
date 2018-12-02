package com.redcms.servlet.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.redcms.beans.Admin;
import com.redcms.db.Db;
import com.redcms.servelt.core.Action;
import com.redcms.util.Md5Encrypt;
/**
 * 用户管理跳转，增删改
 * @author likang
 *
 */
@WebServlet("/admin/admin")
public class AdminManagerServlet extends Action {
    private static Logger log=Logger.getLogger(AdminManagerServlet.class);
    
	@Override
	public void index() throws ServletException, IOException 
	{
		// 查出所有的管理员
		List<Admin> list=null;
		
		try {
			list=Db.query("select * from admin", new BeanListHandler<Admin>(Admin.class));
			setAttr("list", list);
		} catch (SQLException e) {
			setAttr("err","查询管理员失败!");
		}
		this.forword("admin/admin_manager.jsp");
	}
	
	public void saveAdd() throws ServletException, IOException 
	{
        Admin admin=new Admin();
        admin.setActive((byte)1);
        this.getBean(admin);
        admin.setUpwd(Md5Encrypt.md5(admin.getUpwd()));
		try {
			  Db.update("insert into admin(uname,upwd,upur,active) values(?,?,?,?)", admin.getUname(),admin.getUpwd(),admin.getUpur(),admin.getActive());
		      setAttr("msg", "增加管理员成功!");
		} catch (SQLException e) {
			  setAttr("err","增加管理员失败!");
			  log.error("ERROR_ADMIN_001_增加管理员失败!"+e.toString());
			  e.printStackTrace();
		}
		index();
	}
	
	public void update() throws ServletException, IOException 
	{
        Admin admin=new Admin();
        admin.setActive((byte)1);
        this.getBean(admin);
       
        String newpwd=this.getString("newpwd");
        if(!"".equals(newpwd))
        {
        	admin.setUpwd(Md5Encrypt.md5(newpwd));
        }        
		try {
			 Db.update("update admin set uname=?,upwd=?,upur=? where id=?", admin.getUname(),admin.getUpwd(),admin.getUpur(),admin.getId());
		     setAttr("msg", "修改管理员成功!");
		} catch (SQLException e) {
			  setAttr("err","修改管理员失败!");
			  log.error("ERROR_ADMIN_001_增加管理员失败!"+e.toString());
			  e.printStackTrace();
		}
		
		 index();
	}
	
	
	public void del() throws ServletException, IOException 
	{
        int id=this.getInt("id");
        if(id>0)
        {
			try {
				Db.update("delete from admin where id=?",id);
			    setAttr("msg", "删除管理员成功!");
			} catch (SQLException e) {
				setAttr("err","删除管理员失败!");
			    log.error("ERROR_ADMIN_003_删除管理员失败!"+e.toString());
			    e.printStackTrace();
			}
        }
		index();
	}
	

}
