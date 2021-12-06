<!-- 解决数据库中显示乱码 -->
<%@ page language="java" contentType="text/html; charset=utf-8"%>
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
  <h1>查找成功</h1>
   <h1>person id:     ${person.id}</h1><br/>
   <h1>person name:   ${person.name}</h1><br/>
   <h1>person email:  ${person.email}</h1><br/>
   <h1>person birth:  ${person.birth}</h1><br/>
   <h1>person address:${person.address.city}</h1>
</html>
