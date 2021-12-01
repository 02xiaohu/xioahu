<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>   
<%@ taglib prefix="my" uri="http://www.zte.com/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>测试jstl函数库</title>
</head>
<body>
	<h1>测试jstl函数库</h1>
	<hr>
	hello.length=(jsp脚本):<%=((String)request.getAttribute("hello")).length() %><br>
	hello.length(jstl函数库，函数调用必须在el表达式中 前缀+冒号+函数名):${fn:length(hello) }<br>
	list.length:${fn:length(list)}<br>
	<p>
	<li>测试自定义函数库</li><br>
	${my:say(name) }<br>
</body>
</html>