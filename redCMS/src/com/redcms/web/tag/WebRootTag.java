package com.redcms.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * root路径标签
 * @author likang
 *
 */
public class WebRootTag extends SimpleTagSupport 
{
	@Override
	public void doTag() throws JspException, IOException 
	{
		    HttpServletRequest  req=(HttpServletRequest) ((PageContext)this.getJspContext()).getRequest();
		    String scheme=req.getScheme();
		    String host=req.getServerName();
			int port=req.getServerPort();
			String path=req.getContextPath();
			String reqpath=null;
		    
			if(port==80)
		    	reqpath=scheme+"://"+host+path;
		    else
		    	reqpath=scheme+"://"+host+":"+port+path;
		
		     
		    this.getJspContext().getOut().print(reqpath);
	}

}
