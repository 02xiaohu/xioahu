<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>    
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>   
<%@ page import="java.util.*" %>
<%@ page import="com.bjsxt.struts.*" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>测试Iterate</title>
</head>
<body>
	<h1>测试Iterate</h1>
	<hr>
	<li>jsp脚本</li><br>
	<table border="1">
		<tr>
			<td>姓名</td>
			<td>年龄</td>
			<td>所属组</td>
		</tr>
		<%
			List userList = (List)request.getAttribute("userlist");
			if (userList == null || userList.size() == 0) {
		%>
			<tr>
				<td colspan="3">没有符合条件的数据!</td>
			</tr>
		<%
			}else {
				for (Iterator iter=userList.iterator(); iter.hasNext(); ) {
					User user = (User)iter.next();
		%>
			<tr>
				<td><%=user.getUsername() %></td>
				<td><%=user.getAge() %></td>
				<td><%=user.getGroup().getName() %></td>
			</tr>
		<%
				}
			}
		%>
	</table>
	
	<p>
	<li>标签</li><br>
	<table border="1">
		<tr>
			<td>姓名</td>
			<td>年龄</td>
			<td>所属组</td>
		</tr>
		<logic:empty name="userlist">
			<tr>
				<td colspan="3">没有符合条件的数据!</td>
			</tr>
		</logic:empty>
		<logic:notEmpty name="userlist">
			<logic:iterate id="u" name="userlist">
				<tr>
					<td>
						<bean:write name="u" property="username"/>
					</td>
					<td>
						<bean:write name="u" property="age"/>
					</td>
					<td>
						<bean:write name="u" property="group.name"/>
					</td>
				</tr>
			</logic:iterate>
		</logic:notEmpty>
	</table>	
</body>
</html>