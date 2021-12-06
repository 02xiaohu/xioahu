<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>   
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'lonin.jsp' starting page</title>
    
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
    <form action="<%=request.getContextPath()%>/login.do" method="get">
		  <spring:message code="username" /><input type="text" name="username"><br>
		  <spring:message code="password" /> <input type="password" name="password"><br>
		  <input type="submit" value="<spring:message code="submit"/>">  
	</form>

  </body>
</html>
