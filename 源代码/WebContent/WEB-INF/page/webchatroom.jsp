<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@page import="java.util.*" %>
     <%@page import="com.SSSP.bean.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<body>
<% 
UserBean user=(UserBean)session.getAttribute("UserBean");
String name=user.getName();
String empty=(String)session.getAttribute("Empty");
if(empty!=null){
	out.println("<script>alert('不能输入空的内容！')</script>");
	session.removeAttribute("Empty");
}
%>
<h2>欢迎<%=name%> 来到聊天室</h2>
<h2><a href="exit.do">下线</a></h2>
<h2>在线人员</h2>
<hr>
<%
	Set s = (Set)application.getAttribute("alluser");
	Iterator iter = s.iterator() ;
	while(iter.hasNext())
	{
		Object o=iter.next();
		if(o instanceof UserBean){
          UserBean user1=(UserBean)o;
      %>
		<li><%=user1.getName()%></li>
<% 
		}
	}
%>
 <hr>
<form action="messageShow.do" method="post">
请输入在线聊天人的姓名：
<input type="text" name="charname"><br>
请输入聊天信息：  
<input type="text" name="message"><br>
<input type="submit" value="提交">
 
</body>
</html>