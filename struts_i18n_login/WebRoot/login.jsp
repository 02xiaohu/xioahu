<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>  
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title><bean:message key="user.title"/></title>
</head>
<body>
	<h1><bean:message key="user.title"/></h1>
	<hr>
	 <!--
	<font color="red">
		<html:messages id="msg" property="error1">
			<bean:write name="msg"/>
		</html:messages>
	</font>	
	<font color="blue">
		<html:messages id="msg" property="error2">
			<bean:write name="msg"/>
		</html:messages>
	</font>	
	 -->
	 <html:errors/>
	<form action="login.do" method="post">
		<bean:message key="user.username"/>：<input type="text" name="username"><br>
		<bean:message key="user.password"/>：<input type="password" name="password"><br>
		<input type="submit" value="<bean:message key="user.button.login"/>">
	</form>
</body>
</html>