package com.redcms.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.redcms.servelt.core.Action;
import com.redcms.util.HtmlGenerator;
/**
 * 生成网站首页
 * @author likang
 *
 */
@WebServlet("/admin/createIndexHtml")
public class HtmlIndexServlet extends Action {

	@Override
	public void index() throws ServletException, IOException
	{
        String basePath=req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/web/index";
		String realpath=req.getServletContext().getRealPath("html");
		HtmlGenerator.createHtmlPage(basePath, realpath+"/index.html");
        setAttr("msg","生成首页成功");
        
        forword("admin/welcome.jsp");
	}
	
	
	
}

