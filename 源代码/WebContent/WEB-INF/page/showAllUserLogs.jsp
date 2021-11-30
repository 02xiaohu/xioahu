<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@page import="org.springframework.data.domain.Page" %>
    <%@page import="java.util.*" %>
    <%@page import="com.SSSP.bean.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查看所有用户日志</title>
</head>
<body>
<% Page p=(Page)session.getAttribute("AllUserLogs");
String pageNoS=request.getParameter("pageNo");
int pageNo=Integer.parseInt(pageNoS);
List list=p.getContent();
for(Iterator iter=list.iterator();iter.hasNext();){
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
			<td><%=bean.getUser()%></td>
		</tr>
		</table>
	<% 

	
}
%>
<a href="findAllUserLogs.do?pageNo=--pageNoS">上一页</a>&amp;&amp;&amp;&amp;&amp;
<a href="findAllUserLogs.do?pageNo=++pageNoS">下一页</a>
</body>
</html>