<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>权限功能</title>
</head>
<body>
<% String error=(String)session.getAttribute("freezeError");
if(error!=null){
	%>
	<% 
	out.println("<script>alert('查无此人，请重试')</script>");
	session.removeAttribute("freezeError");
}
%>
<form action="goTofreeze.do" method="post">
请你想要操作的用户:<input type="text" name="user"><br>
 <input type="submit" value="确定">               
</form>
</body>
</html>