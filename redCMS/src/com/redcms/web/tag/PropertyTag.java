package com.redcms.web.tag;

import java.lang.reflect.Field;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.redcms.beans.Channel;
import com.redcms.beans.Data;

/**
 * 根据栏目或文章的字段的名字，取相应的值
 * @author likang
 *
 */
public class PropertyTag extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name; //栏目字段的名字
    
	@Override
	public int doEndTag() throws JspException 
	{
		 try{
				  String result=null;
				  Tag parenttag=this.getParent();
				  
				  if(parenttag instanceof ChannelListTag)
				  {
					  ChannelListTag parent=(ChannelListTag)parenttag;
					  Channel curr=parent.getCurrent();
					  
					  //先找扩展字段
					  result=curr.getAttrs().get(name);
					  
					  if(null==result||"".equals(result))
					  {
						  //没有找到，栏目里找
					      Class clazz=curr.getClass();
					      Field field=clazz.getDeclaredField(name);
	
					      if(null!=field)
						  {
							  field.setAccessible(true);
							  result=field.get(curr).toString();
						  }
					  }
					  
				  }else if(parenttag instanceof ArticleListTag)
				  {
					  ArticleListTag parent=(ArticleListTag)parenttag;
					  Data curr=parent.getCurrent();
					 
					  result=curr.getAttrs().get(name);
					  if(null==result||"".equals(result))
					  {
					     Class clazz=curr.getClass();
					     Field field=clazz.getDeclaredField(name);
					     if(null!=field)
					     {
							 field.setAccessible(true);
							 result=field.get(curr).toString();
						 }
					  }
				  }
				  
				  pageContext.getOut().print(result);
				  
			} catch (Exception e) {
				e.printStackTrace();
			}
		return Tag.EVAL_PAGE; 
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
		
  
}
