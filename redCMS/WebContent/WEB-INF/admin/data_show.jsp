<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.util.*,com.redcms.beans.*" %>
    
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title> - 文章页面</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico"> <link href="css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="css/animate.css" rel="stylesheet">
    <link href="css/style.css?v=4.1.0" rel="stylesheet">

</head>


<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight article">
        <div class="row">
            <div class="col-lg-10 col-lg-offset-1">
                <div class="ibox">
                    <div class="ibox-content">

                        <div class="text-center article-title">
                            <h1>
                                    ${data.title}
                                </h1>
                        </div>
                      ${data.dis}
                        <hr>
					  ${data.tags}&nbsp;&nbsp;${data.author}<hr> 
					  ${data.pic1} <br/>
		 
		 
		 <%
				List<Pictures> pic1=(List<Pictures>)request.getAttribute("pics1");
				if(null != pic1 && pic1.size()>0)
				{
					for(int i=0;i<pic1.size();i++)
					{
					 %>	
					 	 
	<img class="img-responsive" src="<%= pic1.get(i).getPath() %>" width="200" height="100"><br/>
	 				
	 				 <%
					}
				}
	 %>
	 
	 
                    </div>
                </div>
            </div>
        </div>

    </div>

    <!-- 全局js -->
    <script src="js/jquery.min.js?v=2.1.4"></script>
    <script src="js/bootstrap.min.js?v=3.3.6"></script>


    <!-- 自定义js -->
    <script src="js/content.js?v=1.0.0"></script>


    
    

</body>

</html>
reu