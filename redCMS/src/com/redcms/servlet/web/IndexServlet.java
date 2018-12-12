package com.redcms.servlet.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.redcms.servelt.core.Action;

/**
 * 前端页面跳转
 * @author likang
 *
 */
@WebServlet("/web/index")
public class IndexServlet extends Action {

	@Override
	public void index() throws ServletException, IOException {
		
          forword("template/index.jsp");
	}

}
