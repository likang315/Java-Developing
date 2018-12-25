
RedCMS：使用最简单性能最高的框架，将CMS(内容管理系统)系统简单到极致，灵活的栏目扩展，快速的构建网站的后台管理系统
静态演示站（测试）： www.zpcgxx.com 

使用的框架技术：
	java，servlet，bootstrap3，kindeditor,mysql,jquery,druid,dbutils

基本架构介绍
	1：站点所有的页面都是由静态页面构成，使用：HttpClient 
	2：数据库监控，使用：DruidStatView
	3：大量的文章生成全球唯一id ，使用：高效GUID产生算法(sequence),基于Snowflake实现64位自增ID算法，GitHub开源项目
	4：对数据库操作，使用：dbutils，为巩固SQL语句
	5：文章的内容，使用：kindeditor
	6：文件上传，使用：Apache的开元项目commons 的fileupload
	6：日志，使用：log4j

WEB显示架构介绍

WebContent/WEB-INF/admin目录下：存放所有的后台显示页面
WebContent/WEB-INF/template目录下：存放所有的前端显示页面	

    站点首页 -->index.jsp
    栏目首页 -->xxx_index.jsp
    栏目列表页 -->xxx_list.jsp
    内容页 -->xxx_content.jsp


标签uri:/com/redcms/web/tag

    WebRootTag:取站点根目录
    channelInfoTag:栏目信息
    channelListTag:栏目列表
    propertyTag:栏目或内容的属性值
    articleInfo:内容页信息
    articleList:内容列表
    PagerTag:分页

缺陷
    由于时间等原因，即将期末考试，还有很多细节没有完善，比如所有的异常都没有加上日志并且给日志编号等等，大家如果对此项目
    感兴趣，可以fork，完善一下，谢谢.
	








