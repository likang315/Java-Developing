<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<head>
<%@include file="header.jsp" %>
<title>RedCMS后台系统</title>
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
               
                    <div class="ibox-title">
                        <h5>关于 RedCMS</h5>
                        <div class="ibox-tools">
                            <a class="collapse-link">
                                <i class="fa fa-chevron-up"></i>
                            </a>
                        </div>
                    </div>
                    <div class="ibox-content" style="font-size:12px;">
<!-- 内容开始 -->
       <h3>简介</h3>
       <p>使用最简单性能最高的框架，将CMS(内容管理系统)系统简单到极致，灵活的栏目扩展，快速的构建网站的后台管理系统
		     演示站（测试）： www.zpcgxx.com </p>

<h3>使用的框架技术：</h3>

<P>java，servlet，bootstrap3，kindeditor,mysql,jquery,druid,dbutils</P>            

<h3>WEB显示架构介绍</h3>
<p>
WebContent/WEB-INF/admin目录下：存放所有的后台显示页面
WebContent/WEB-INF/template目录下：存放所有的前端显示页面	

<br/>
<ul>
 <li>站点首页 -->index.jsp</li>
 <li>栏目首页  -->xxx_index.jsp</li>
 <li>栏目列表页  -->xxx_list.jsp</li>
 <li>内容页  -->xxx_content.jsp</li>
</ul>
</p>

<p>
	
标签uri:/com/redcms/web/tag<br/>
<ul>
  <li>WebRootTag:取站点根目录</li>
  <li>channelInfoTag:栏目信息</li>
  <li>channelListTag:栏目列表</li>
  <li>propertyTag:栏目或内容的属性值</li>
  <li>articleInfo:内容页信息</li>
  <li> articleList:内容列表</li>
  <li>PagerTag:分页</li>
</ul>
</p>
<br/>
<h3>网站效果</h3>
<img src="img/index.png" width="200px" height="500px">
<p>

<p>
 <!-- 内容结束 -->
                    </div>
                </div>
            </div>
       </div>
   </div>
   <%@include file="booter.jsp" %>
</body>
</html>
