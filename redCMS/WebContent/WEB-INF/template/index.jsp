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


  <hr/><br/>
  <h1>首页</h1>
  <center>
    <a href="web/channelindex?channelId=3">通信与信息工程学院</a>&nbsp;&nbsp;
      <a href="web/channelindex?channelId=4">自动化学院</a>&nbsp;&nbsp;
        <a href="web/channelindex?channelId=6">电子与信息工程学院</a>
  </center>




</body>
</html>

