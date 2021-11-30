<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
   <%@page import="com.SSSP.bean.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<body>
<%UserBean user=(UserBean)session.getAttribute("UserBean");
%>
<h1>欢迎你，尊敬的<%=user.getName() %>用户，登录成功！</h1>
<a href="function.do">点击这里进入功能界面</a>
<hr>
</body>
</html>