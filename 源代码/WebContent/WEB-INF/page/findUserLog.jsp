<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@page import="java.util.*" %>
     <%@page import="com.SSSP.bean.*" %>
    <!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查看个人日志</title>
</head>
<body>
<%List list= (List)session.getAttribute("List");
Integer id=(Integer)session.getAttribute("id");
Iterator iter=list.iterator();
while(iter.hasNext()){
	 logBean bean=(logBean)iter.next();
	 %>
	 <table border="2" color="#FF0000" align="center">
	    <tr>
              <td>log_id</td>
			   <td>log_type</td>
			   <td>log_amount</td>
			    <td>userid</td>
		</tr>
		<tr>
			<td><%=bean.getLog_id()%></td>
			<td><%=bean.getLog_type()%></td>
			<td><%=bean.getLog_amount()%></td>
			<td><%=id%></td>
		</tr>
		</table>
	 <% 
}

%>

</body>
</html>