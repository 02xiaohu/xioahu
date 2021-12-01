<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${header['accept-language']}"/>
<fmt:setBundle basename="res.MessageResources"/>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title><fmt:message key="user.title"/></title>
</head>
<body>
	<h1><fmt:message key="user.title"/></h1>
	<hr>
	<form action="login.do" method="post">
		<fmt:message key="user.username"/>：<input type="text" name="username"><br>
		<fmt:message key="user.password"/>：<input type="password" name="password"><br>
		<input type="submit" value="<fmt:message key="user.button.login"/>">
	</form>
</body>
</html>