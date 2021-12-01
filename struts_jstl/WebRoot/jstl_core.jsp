<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>测试jstl核心库</title>
</head>
<body>
	<h1>测试jstl核心库</h1>
	<hr>
	<li>测试c:out</li><br>
	hello(default):<c:out value="hello"/><br>
	hello(default):<c:out value="${hello}"/><br>
	hello(el表达式):${hello }<br>
	
	
    hello(default="123"):<c:out value="${abcd}" default="123" /><br>
	hello(default="123"):<c:out value="${abcd}">123</c:out><br>
	bj(defalut):<c:out value="${bj}"/><br>
	bj(escapeXml="true"):<c:out value="${bj}" escapeXml="true"/><br>
	bj(escapeXml="false"):<c:out value="${bj}" escapeXml="false"/><br>
	bj(el表达式):${bj }<br>
	<p>
	<li>测试c:set和c:remove</li><br>
	<c:set value="123" var="temp"/>
	temp:${temp }<br>
	<c:remove var="temp"/>
	temp:${temp }<br>
	<p>
	 <li>测试条件控制标签c:if</li><br>
	<c:if test="${v1 lt v2}" var="v">
		v1小于v2<br>v=${v }<br>
	</c:if>
	<c:if test="${empty v3}"var="v">
		v3为空<br>v=${v }<br>
	</c:if>
	<c:if test="${empty v4}">
		v4为空<br>
	</c:if>
	<c:if test="${empty v5}">
		v5为空<br>
	</c:if>
	<p>
	<li>测试条件控制标签c:choose,c:when,c:otherwise</li><br>
	<c:choose>
		<c:when test="${v1 lt v2}">
			v1小于v2<br>
		</c:when>
		<c:otherwise>
			v1大于v2<br>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty v4}">
			v4为空<br>
		</c:when>
		<c:otherwise>
			v4不为空<br>
		</c:otherwise>
	</c:choose>
	<p>
	<li>测试循环控制标签c:forEach</li><br>
	<table border="1">
		<tr>
			<td>姓名</td>
			<td>年龄</td>
			<td>所属组</td>
		</tr>
		<c:choose>
			<c:when test="${empty userlist}">
				<tr>
					<td colspan="3">没有符合条件的数据!</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${userlist}" var="u">
					<tr>
						<td>${u.username }</td>
						<td>${u.age }</td>
						<td>${u.group.name }</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>	
	<p>	
	<li>测试循环控制标签c:forEach,varstatus</li><br>
	<table border="1">
		<tr>
			<td>姓名</td>
			<td>年龄</td>
			<td>所属组</td>
		</tr>
		<c:choose>
			<c:when test="${empty userlist}">
				<tr>
					<td colspan="3">没有符合条件的数据!</td>
			</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${userlist}" var="user" varStatus="vs">
					<c:choose>
						<c:when test="${vs.count % 2 == 0}">
							<tr bgcolor="red">
						</c:when>
						<c:otherwise>
							<tr>
						</c:otherwise>
		            </c:choose>
								<td>
									<c:out value="${user.username}"/>
								</td>
								<td>
									<c:out value="${user.age}"/>
								</td>
								<td>
									<c:out value="${user.group.name}"/>
								</td>
   </tr>		   
			</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	<p>
	<li>测试循环控制标签c:forEach,begin,end,step</li><br>
	<table border="1">
		<tr>
			<td>姓名</td>
			<td>年龄</td>
			<td>所属组</td>
		</tr>
		<c:choose>
			<c:when test="${empty userlist}">
				<tr>
					<td colspan="3">没有符合条件的数据!</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${userlist}" var="user" begin="2" end="8" step="2">
					<tr>
						<td>${user.username}</td>
						<td>${user.age}</td>
						<td>${user.group.name }</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>	
	<p>
	<li>测试循环控制标签c:forEach，普通循环</li><br>
	<c:forEach begin="1" end="10">
		  ${hello} <br>
	       D<br>
	<!--A<br>-->
	</c:forEach>
	<p>
	<li>测试循环控制标签c:forEach，输出map</li><br>
	<c:forEach  items="${mapvalue}" var="v">
		${v.key }=${v.value }<br>
	</c:forEach>
	<p>
	-----${mapvalue.k1}--------<br>
	-----${mapvalue.k2}--------
	<li>测试循环控制标签c:forTokens</li><br>
	<c:forTokens items="${strTokens}" delims="#" var="v">
		${v }<br>
	</c:forTokens>
	<p>
	<li>测试c:catch</li><br>
	<%
		 try {
			 Integer.parseInt("asdfsdf");
		 }catch(Exception e) {
		 	out.println(e);
		 }	
	%>
	<p>
	<c:catch var="exinfo">
		<%
			Integer.parseInt("asdfsdf");
		%>
	</c:catch>
	${exinfo }<br>
	<p>
	<li>测试c:import</li><br>
	<c:import url="http://localhost:8080/struts_login"/>  
	<p>
	<li>测试c:url和c:param</li><br>
	<c:url value="http://localhost:8080/drp/sysmgr/user_add.jsp" var="v">
		<c:param name="username" value="Jack"/>
		<c:param name="age" value="20"/>
	</c:url>
	${v }<br>
	<li>测试:redirect</li><br>
       
	<%--   <c:redirect context="/struts_login" url="/index.jsp"/>  --%>
</html>