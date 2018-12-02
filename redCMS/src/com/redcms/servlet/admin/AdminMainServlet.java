package com.redcms.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.redcms.servelt.core.Action;
/**
 * 跳转主页
 * @author likang
 *
 */
@WebServlet("/admin/index")
public class AdminMainServlet extends Action {

	@Override
	public void index()throws ServletException, IOException {
       forword("admin/index.jsp");
	}

}
