<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>存款</title>
</head>
<body>
<% String error=(String)session.getAttribute("Error");
if(error!=null){
	%>
	<% 
	out.println("<script>alert('输入无效，请再次检查')</script>");
	session.removeAttribute("Error");
}
%>
<form action="depositdd.do" method="post" >
 请输入金额：<input type="text" name="much"><br>
请再次输入：<input type="text" name="reMuch"><br>
 <input type="submit" value="确认">
 </form>
 
</body>
</html>