<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="cms" uri="/redcms/web/tags" %>

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
<h1>首页->${channel.name}</h1>

<c:forEach items="${subs}" var="sub">
  <h2><cms:channelInfo name="name" id="${sub.id}"/><a href="web/channelList?channelId=${sub.id}"> 更多>></a></h2>
 
<ul>  
  <cms:articleList top="3" channelId="${sub.id}">
      <li><a href="web/channelContent?dataId=<cms:property name="id"/>"><cms:property name="title"/></a>--><cms:property name="createtime"/></li>
  </cms:articleList>

</ul>
</c:forEach>

</body>
</html>