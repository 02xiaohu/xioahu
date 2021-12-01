<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>����jstl���Ŀ�</title>
</head>
<body>
	<h1>����jstl���Ŀ�</h1>
	<hr>
	<li>����c:out</li><br>
	hello(default):<c:out value="hello"/><br>
	hello(default):<c:out value="${hello}"/><br>
	hello(el���ʽ):${hello }<br>
	
	
    hello(default="123"):<c:out value="${abcd}" default="123" /><br>
	hello(default="123"):<c:out value="${abcd}">123</c:out><br>
	bj(defalut):<c:out value="${bj}"/><br>
	bj(escapeXml="true"):<c:out value="${bj}" escapeXml="true"/><br>
	bj(escapeXml="false"):<c:out value="${bj}" escapeXml="false"/><br>
	bj(el���ʽ):${bj }<br>
	<p>
	<li>����c:set��c:remove</li><br>
	<c:set value="123" var="temp"/>
	temp:${temp }<br>
	<c:remove var="temp"/>
	temp:${temp }<br>
	<p>
	 <li>�����������Ʊ�ǩc:if</li><br>
	<c:if test="${v1 lt v2}" var="v">
		v1С��v2<br>v=${v }<br>
	</c:if>
	<c:if test="${empty v3}"var="v">
		v3Ϊ��<br>v=${v }<br>
	</c:if>
	<c:if test="${empty v4}">
		v4Ϊ��<br>
	</c:if>
	<c:if test="${empty v5}">
		v5Ϊ��<br>
	</c:if>
	<p>
	<li>�����������Ʊ�ǩc:choose,c:when,c:otherwise</li><br>
	<c:choose>
		<c:when test="${v1 lt v2}">
			v1С��v2<br>
		</c:when>
		<c:otherwise>
			v1����v2<br>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty v4}">
			v4Ϊ��<br>
		</c:when>
		<c:otherwise>
			v4��Ϊ��<br>
		</c:otherwise>
	</c:choose>
	<p>
	<li>����ѭ�����Ʊ�ǩc:forEach</li><br>
	<table border="1">
		<tr>
			<td>����</td>
			<td>����</td>
			<td>������</td>
		</tr>
		<c:choose>
			<c:when test="${empty userlist}">
				<tr>
					<td colspan="3">û�з�������������!</td>
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
	<li>����ѭ�����Ʊ�ǩc:forEach,varstatus</li><br>
	<table border="1">
		<tr>
			<td>����</td>
			<td>����</td>
			<td>������</td>
		</tr>
		<c:choose>
			<c:when test="${empty userlist}">
				<tr>
					<td colspan="3">û�з�������������!</td>
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
	<li>����ѭ�����Ʊ�ǩc:forEach,begin,end,step</li><br>
	<table border="1">
		<tr>
			<td>����</td>
			<td>����</td>
			<td>������</td>
		</tr>
		<c:choose>
			<c:when test="${empty userlist}">
				<tr>
					<td colspan="3">û�з�������������!</td>
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
	<li>����ѭ�����Ʊ�ǩc:forEach����ͨѭ��</li><br>
	<c:forEach begin="1" end="10">
		  ${hello} <br>
	       D<br>
	<!--A<br>-->
	</c:forEach>
	<p>
	<li>����ѭ�����Ʊ�ǩc:forEach�����map</li><br>
	<c:forEach  items="${mapvalue}" var="v">
		${v.key }=${v.value }<br>
	</c:forEach>
	<p>
	-----${mapvalue.k1}--------<br>
	-----${mapvalue.k2}--------
	<li>����ѭ�����Ʊ�ǩc:forTokens</li><br>
	<c:forTokens items="${strTokens}" delims="#" var="v">
		${v }<br>
	</c:forTokens>
	<p>
	<li>����c:catch</li><br>
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
	<li>����c:import</li><br>
	<c:import url="http://localhost:8080/struts_login"/>  
	<p>
	<li>����c:url��c:param</li><br>
	<c:url value="http://localhost:8080/drp/sysmgr/user_add.jsp" var="v">
		<c:param name="username" value="Jack"/>
		<c:param name="age" value="20"/>
	</c:url>
	${v }<br>
	<li>����:redirect</li><br>
       
	<%--   <c:redirect context="/struts_login" url="/index.jsp"/>  --%>
</html>