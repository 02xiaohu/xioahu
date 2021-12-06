<!-- 解决数据库中显示乱码 -->
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%> 

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
              <a href="<%=request.getContextPath()%>/reg.jsp">添加用户</a><br>
                <table width="98%" border="0" >

                <tr gn="center" bgcolor="#FAFAF1" height="22"
                cellpadding="2" cellspacing="1" bgcolor="#D1DDAA" 
                align="center" style="margin-top:8px" >
               
					<td width="18%">ID</td>
					<td width="18%">用户名</td>
					<td width="18%">密码</td>
					 
		        </tr>	
				<c:forEach items="${all}" var="s">
				<tr align='center' bgcolor="#FFFFFF" height="22">
					<td bgcolor="#FFFFFF" align="center">
						${s.id}
					</td>
					<td bgcolor="#FFFFFF" align="center">
						${s.username}
					</td>
					<td bgcolor="#FFFFFF" align="center">
					    ${s.password}
					</td>
					 
					<td bgcolor="#FFFFFF" align="center">
						<a href="<%=path %>/delUser.do?id=${s.id}">删除</a>
						<a href="<%=path %>/findUserById.do?id=${s.id}">修改</a>
					</td>
				</tr>
				</c:forEach>
			</table>
  
  
  </body>
</html>
