package com.redcms.servlet.admin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.io.FileUtils;

import com.redcms.servelt.core.Action;

/**
 * 模板管理
 * @author likang
 *
 */
@WebServlet("/admin/template")
public class TemplateServlet extends Action {

	@Override
	public void index() throws ServletException, IOException
	{
		ServletContext sc=req.getServletContext();
		File f=new File(sc.getRealPath("WEB-INF/template"));
		//匿名内部类
		String[] allfile=f.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith("index.jsp")||name.endsWith("_index.jsp")||name.endsWith("_list.jsp")||name.endsWith("_content.jsp"))
					return true;
				else
					return false;
			}
		});
		
		setAttr("allfile", allfile);
		forword("admin/template.jsp");
	}
	
	//新建jsp文件
	public void add() throws ServletException, IOException 
	{
		ServletContext sc=req.getServletContext();
		String name=this.getString("newname");
		if(null!=name&&(name.endsWith("_index.jsp")||name.endsWith("_list.jsp")||name.endsWith("_content.jsp")))
		{
			File target=new File(sc.getRealPath("WEB-INF/template"),name);
			
			File moban=new File(sc.getRealPath("WEB-INF/template/moban.jsp"));
			FileUtils.copyFile(moban, target);
			
			String str= FileUtils.readFileToString(target);
			setAttr("fname", name);
			setAttr("jspcont", str);
		}
		index();
	}

	//读取指定的jsp文件，然后跳转到显示页面
	public void edit() throws ServletException, IOException 
	{
		  ServletContext sc=req.getServletContext();
		  String name=this.getString("fname");
		  if(null!=name && name.length()>0)
		  {
			 File target=new File(sc.getRealPath("WEB-INF/template"),name);
			 String str= FileUtils.readFileToString(target);
			 setAttr("fname", name);
			 setAttr("jspcont", str);
		  }
		  index();
	}
	
	//保存修改
	public void editSave() throws ServletException, IOException 
	{
		
		  ServletContext sc=req.getServletContext();
		  String name=this.getString("fname");
		 
		  String txt=this.getString("txt");
		  
		  if(null!=name && name.length()>0)
		  {
			  File target=new File(sc.getRealPath("WEB-INF/template"),name);
			  FileUtils.writeStringToFile(target, txt, "utf-8", false);
			  
			  String str= FileUtils.readFileToString(target);
			  
			 setAttr("fname", name);
			 setAttr("jspcont", str);
			 setAttr("msg","修改成功!");
		  }
		  index();
	}
	
	
}
