<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.lang.Integer"  %>

<%@taglib prefix="cms" uri="/redcms/web/tags"%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"%>"/>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
 <link href="css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
 <link href="css/font-awesome.css?v=4.4.0" rel="stylesheet">
 <link href="css/animate.css" rel="stylesheet">
 <link href="css/style.css?v=4.1.0" rel="stylesheet">
</head>
<body>

<h2><cms:rootpath/></h2>

<h2>栏目列表</h2>
<ul>
	<cms:channelList parentId="0" top="3">
	  <li><cms:property name="id"/>---><cms:property name="name"/>---><cms:property name="title"/>---><cms:property name="price"/></li> 
	  <img src="<cms:property name="pic01"/>" width=200px height=100px>
		
	</cms:channelList>
</ul>

<h2>取一个栏目的信息</h2>
  <cms:channelInfo id="3" name="name"/>--> <cms:channelInfo id="3" name="title"/>

<h2>取内容列表:</h2>
<ul>
  <%
   int pageNo=null!=request.getParameter("pageNo")?Integer.parseInt(request.getParameter("pageNo")):1;
  %>
  <cms:articleList channelId="11" pageNo="1" pageSize="1">
     <li><cms:property name="title"/>----<cms:property name="createtime"/></li>
  </cms:articleList>
  
  <cms:pager url="web/index" param="userid=10&uname=lisi"/>
</ul> 


  <%--跳转显示指定栏目的所有信息 --%>
<h2><cms:channelInfo name="name" id="3"/><a href="channelList.jsp"> 更多>></a></h2>
  
  
  <%--点击文章链接显示文章 --%>
<ul>
  <cms:articleList channelId="3" top="3" >
      <li><a href="content.jsp"><cms:property name="title"/></a>----<cms:property name="createtime"/></li>
  </cms:articleList>
</ul>

  <hr/>
  <h2><cms:channelInfo name="name" id="3"/><a href="#"> 更多>></a></h2>
  
  <hr/>
  <cms:article dataId="1071588772090499072" name="title" />
  
  
  <hr/> 
  <h1>这里是首页面</h1>
  <center>
    <a href="web/channelindex?channelId=3">通信与信息工程学院</a>
  </center>




</body>
</html>

