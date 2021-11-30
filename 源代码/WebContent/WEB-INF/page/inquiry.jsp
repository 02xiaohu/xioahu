<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@page import="com.SSSP.bean.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查询余额界面</title>
</head>
<body>
<hr>
<% UserBean user=(UserBean)session.getAttribute("UserBean");
double money=(double)session.getAttribute("money");
%>
<h1>亲爱的用户：<%=user.getName() %>,您的余额是：<%=money %></h1>
</body>
</html>