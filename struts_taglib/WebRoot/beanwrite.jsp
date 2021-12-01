<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>测试BeanWrite</title>
</head>
<body>
	<h1>测试BeanWrite</h1>
	<hr>
	<li>普通字符串</li><br>
	hello(jsp脚本):<%=request.getAttribute("hello") %><br>
	hello(标签):<bean:write name="hello"/><br>
	<p>
	<li>html文本</li><br>
	bj(default):<bean:write name="bj"/><br>
	bj(filter="true"):<bean:write name="bj" filter="true"/><br>
	bj(filter="false"):<bean:write name="bj" filter="false"/><br>
	<p>
	<li>格式化日期</li><br>
	today(default):<bean:write name="today"/><br>
	today(format="yyyy-MM-dd HH:mm:ss"):<bean:write name="today" format="yyyy-MM-dd HH:mm:ss"/>
	<p>
	<li>格式化数字</li><br>
	n(default):<bean:write name="n"/><br>
	n(format="###,###.####"):<bean:write name="n" format="###,###.####"/><br>
	n(format="###,###.####"):<bean:write name="n" format="###,###.0000"/><br>
	<p>
	<li>结构</li><br>
	姓名：<input type="text" value="<bean:write name="user" property="username"/>"><br>
	年龄：<input type="text" value="<bean:write name="user" property="age"/>"><br>
	所属组：<input type="text" value="<bean:write name="user" property="group.name"/>"><br>
	</body>
</html>