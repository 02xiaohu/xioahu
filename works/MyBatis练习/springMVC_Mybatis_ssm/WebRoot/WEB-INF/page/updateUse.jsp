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
  
  <body>
    <form action="<%=request.getContextPath()%>/updateUser.do" method="post">
		户号：<input type="text" name="id" value=${user.id} readonly><br>
		户名：<input type="text" name="username" value=${user.username} ><br>
		密码：<input type="password" name="password" value=${user.password}><br>
		<input type="submit" value="确认">
	</form>

  </body>
</html>
