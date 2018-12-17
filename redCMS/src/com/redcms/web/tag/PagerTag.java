package com.redcms.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.redcms.db.PageDiv;

import javafx.scene.chart.PieChart.Data;

public class PagerTag extends SimpleTagSupport {
    private String url;
    private String param;
  
	@Override
	public void doTag() throws JspException, IOException {
    StringBuilder sb=new StringBuilder();
    PageContext pageContext=(PageContext)this.getJspContext();
    PageDiv<Data> page=(PageDiv<Data>)pageContext.getAttribute("pd", PageContext.REQUEST_SCOPE);
    
    sb.append("<div class='btn-group'>");
    if(page.getPageNo()-1<1)
    sb.append("<a href='"+url+"?pageNo=1&"+param+"'  class='btn btn-white' type='button'><i class='fa fa-chevron-left'></i>");
    else
    sb.append("<a href='"+url+"?pageNo="+(page.getPageNo()-1)+"&"+param+"'  class='btn btn-white' type='button'><i class='fa fa-chevron-left'></i>");	
   
    sb.append("</a>");
      for(int i=page.getStart();i<=page.getEnd();i++)
      {
            sb.append("<a href='"+url+"?pageNo="+i+"&"+param+"'  class='btn btn-white'>"+i+"</a>");
      }
  
      
      if(page.getPageNo()+1>page.getTotalPage())
      {
    	 sb.append(" <a href='"+url+"?pageNo="+page.getTotalPage()+"&"+param+"' class='btn btn-white' type='button'><i class='fa fa-chevron-right'></i></a>"); 
      }else
      {
    	  sb.append(" <a href='"+url+"?pageNo="+(page.getPageNo()+1)+"&"+param+"' class='btn btn-white' type='button'><i class='fa fa-chevron-right'></i></a>") ;
      }
      
      sb.append("<button class='btn btn-white'>第<span style='color:red;'>"+page.getPageNo()+"</span>/<b>"+page.getTotalPage()+"</b>页,共<b>"+page.getTotalCount()+"<b/>条</button>");
         
          
      sb.append("</div>");

		this.getJspContext().getOut().println(sb.toString());

		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
