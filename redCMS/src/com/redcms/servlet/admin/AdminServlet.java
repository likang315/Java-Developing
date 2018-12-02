package com.redcms.servlet.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.dbutils.handlers.BeanHandler;

import com.redcms.beans.Admin;
import com.redcms.db.Db;
import com.redcms.servelt.core.Action;
import com.redcms.util.Md5Encrypt;
/**
 * 登录，注销
 * @author likang
 *
 */
@WebServlet("/admin/login")
public class AdminServlet extends Action {

	@Override
	public void index()throws ServletException, IOException {
		 
		this.forword("admin/login.jsp");
	}
	public void logout()throws ServletException, IOException {
		   
		   req.getSession().removeAttribute("loged");//从session中移除字段
		   req.getSession().invalidate();//释放session
		   redirect("login");
	}
	
	
	public void checkLogin()throws ServletException, IOException{
		 
		 Admin admin=new Admin();
		 this.getBean(admin);
		 
		 String randomCode=(String)req.getSession().getAttribute("randomCode");
		 String rand=this.getString("rand");//不需要填充，不和数据库对应
		 
		 if(rand.equals(randomCode))
		 {
			 String sql="select * from admin where uname=? and upwd=? limit 1"; 
			 try {
				Admin loged=Db.query(sql, new BeanHandler<Admin>(Admin.class),admin.getUname(),Md5Encrypt.md5(admin.getUpwd()));
				if(null!=loged)
				{
					//成功
					req.getSession().setAttribute("loged", loged);
					redirect("index");
				}else
				{
					setAttr("err", "用户名和密码不正确！");
					index();
				}
			} catch (SQLException e) {
				System.err.println("ERROR__003:登录失败");
				e.printStackTrace();
			}
		 
		 }else
		 {
			 setAttr("err", "验证码输入不正确！");
			 index();
		 }

	}

}
