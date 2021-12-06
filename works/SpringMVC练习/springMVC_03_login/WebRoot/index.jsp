<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    测试testLogin（）方法:     <a href="<%=request.getContextPath()%>/login.jsp">登录</a><br>
    测试testLogin2（）方法:    <a href="<%=request.getContextPath()%>/login2.jsp">登录2</a><br>
    测试testLogin3（）方法 :   <a href="<%=request.getContextPath()%>/login3.jsp">登录3</a><br>
    测试testLogin4（）方法 :   <a href="<%=request.getContextPath()%>/login4.jsp">登录4</a><br>
  </body>
</html>
