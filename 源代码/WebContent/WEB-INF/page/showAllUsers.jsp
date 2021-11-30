<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@page import="java.util.*" %>
        <%@page import="com.SSSP.bean.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查看所有用户</title>
</head>
<body>
<% 
List<AllUserBean> list=(List)session.getAttribute("AllUsers");
Iterator iter=list.iterator();
while(iter.hasNext()){
	AllUserBean bean=(AllUserBean)iter.next();
	 %>
	 <table border="2" color="#FF0000" align="center">
	    <tr>
              <td>user_id</td>
			   <td>user_name</td>
			   <td>user_password</td>
			    <td>flag</td>
			    <td>balance</td>
		</tr>
		<tr>
			<td><%=bean.getUser_id()%></td>
			<td><%=bean.getUser_name()%></td>
			<td><%=bean.getUser_password() %></td>
			<td><%=bean.getUser_flag()%></td>
			<td><%=bean.getBalance()%></td>
		</tr>
		</table>
	 <% 
}
%>
</body>
</html>