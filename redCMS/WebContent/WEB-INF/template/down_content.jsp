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
<h1>内容页面</h1>

<h3>首页->${parent.name}-->${channel.name}-->${data.title}</h3>

<div class="container">
   <h1>${data.title}</h1>
   <hr/>
   <div>
       ${data.txt1}-------${data.createtime}
   </div>
</div>
</body>
</html>