<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>   
<%@ taglib prefix="my" uri="http://www.zte.com/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>����jstl������</title>
</head>
<body>
	<h1>����jstl������</h1>
	<hr>
	hello.length=(jsp�ű�):<%=((String)request.getAttribute("hello")).length() %><br>
	hello.length(jstl�����⣬�������ñ�����el���ʽ�� ǰ׺+ð��+������):${fn:length(hello) }<br>
	list.length:${fn:length(list)}<br>
	<p>
	<li>�����Զ��庯����</li><br>
	${my:say(name) }<br>
</body>
</html>