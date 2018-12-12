<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@taglib prefix="cms" uri="/redcms/web/tags" %>

<!DOCTYPE html>
<html>
<head>
<base href="<cms:rootpath/>"/>
<meta charset="UTF-8">
<title>Insert title here</title>
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

</body>
</html>

