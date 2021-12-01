<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>测试EL表达式</title>
</head>
<body>
	<h1>测试EL表达式</h1><br>
	<hr>
	<li>普通字符串</li><br>
	hello(jsp脚本):<%=request.getAttribute("hello") %><br>
	hello(el表达式,el表达式的使用方法$和{}):${hello}<br>
	hello(el表达式,el的隐含对象pageScope,requestScope,sessionScope,applicationScope,<br> 如果未指定scope,它的搜索顺序为pageScope~applicationScope):${requestScope.hello }<br>
	hello(el表达式,scope=session):${sessionScope.hello }<br>
	<p>
	<li>结构,采用.进行导航，也称存取器</li><br>
	姓名：${user.username}<br>
	年龄：${user.age }<br>
	所属组：${user.group.name }<br>
	<p>
	<li>输出map,采用.进行导航，也称存取器</li><br>
	<!-- 从mapvalue所指的map中，取得key=key1的value -->
	mapvalue.key1:${mapvalue.key1 }<br>
	mapvalue.key2:${mapvalue.key2 }<br>
	<p>
	<li>输出数组,采用[]和下标</li><br>
	str[1]:${str[1] }<br>
	<p>
	<li>输出对象数组,采用[]和下标</li><br>
	usersArray[2].username:${usersArray[2].username }<br>
	<p>
	<li>输出list,采用[]和下标</li><br>
	userlist[4].username:${userlist[4].username }<br>
	<p>
	<li>el表达式对运算符的支持</li><br>
	1+2=${1+2 }<br>
	10/0=${10/0 }<br>
	10 div 5=${10 div 5 }<br>
	10%3=${10 % 3 }<br>
	10 mod 3=${10 mod 3 }<br>
	 abcd=${abcd+ 3 }<br>
	<!--
		 ==/eq
		 !=/ne 
		 </lt
		 >/gt
		 <=/le
		 >=/ge
		 &&/and
		 ||/or
		 !/not
		 //div
		 %/mod
	 -->  
	 ${10 > 5 }
	 
	 <li>测试empty</li><br>
	 value1:${empty value1 }<br>
	 value2:${empty value2 }<br>
	 value3:${empty value3 }<br>
	 value4:${empty value4 }<br>
	 value4:${!empty value4 }<br>
</body>
</html>