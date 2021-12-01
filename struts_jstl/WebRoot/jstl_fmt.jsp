<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>测试jstl格式化库</title>
</head>
<body>
	<h1>测试jstl格式化库</h1>
	<hr>
	<li>测试日期的格式化</li><br>
	${today}<br>
	today(default):<fmt:formatDate value="${today}"/><br>
	today(type="date"):<fmt:formatDate value="${today}" type="date"/><br>
	today(type="time"):<fmt:formatDate value="${today}" type="time"/><br>
	today(type="both"):<fmt:formatDate value="${today}" type="both"/><br>
	today(dateStyle="short"):<fmt:formatDate value="${today}" dateStyle="short"/><br>
	today(dateStyle="medium"):<fmt:formatDate value="${today}" dateStyle="medium"/><br>
	today(dateStyle="long"):<fmt:formatDate value="${today}" dateStyle="long"/><br>
	today(dateStyle="full"):<fmt:formatDate value="${today}" dateStyle="full"/><br>
	today(pattern="yyyy/MM/dd HH:mm:ss"):<fmt:formatDate value="${today}" pattern="yyyy/MM/dd HH:mm:ss"/><br>
	today(pattern="yyyy/MM/dd HH:mm:ss"):<fmt:formatDate value="${today}" pattern="yyyy/MM/dd HH:mm:ss" var="d"/><br>
	.......${d }<br格式化value的值必>
	<p>
	<li>测试数字的格式化</li><br>
	${n}<br>
	n(default):<fmt:formatNumber value="${n}"/><br>
	n(pattern="###,###.##"):<fmt:formatNumber value="${n}" pattern="###,###.##"/><br>
	n(pattern="###,###.0000"):<fmt:formatNumber value="${n}" pattern="###,###.0000"/><br>
	n(groupingUsed="false"):<fmt:formatNumber value="${n}" groupingUsed="false"/><br>
	n(minIntegerDigits="10"):<fmt:formatNumber value="${n}" minIntegerDigits="10"/><br>
	n(type="currency"):<fmt:formatNumber value="${n}" type="currency"/><br>
	n(type="currency"):<fmt:formatNumber value="${n}" type="currency" currencySymbol="￥"/><br>
	n(type="percent"):<fmt:formatNumber value="${p}" type="percent"/><br>
	n(type="percent"):<fmt:formatNumber value="${p}" type="percent" maxFractionDigits="2" minFractionDigits="2"/><br>
</body>
</html>