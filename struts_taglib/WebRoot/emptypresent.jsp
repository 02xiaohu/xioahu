<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>测试empty,notEmpty,present,notPresent</title>
</head>
<body>
	<h1>测试empty,notEmpty,present,notPresent</h1>
	<hr>
	<logic:empty name="attr1">
		attr1为空<br>
	</logic:empty>
	<logic:notEmpty name="attr1">
		attr1不为空<br>
	</logic:notEmpty>
	<logic:present name="attr1">
		attr1存在<br>
	</logic:present>
	<logic:notPresent name="attr1">
		attr1不存在<br>
	</logic:notPresent>
	
	<p>
	<logic:empty name="attr2">
		attr2为空<br>
	</logic:empty>
	<logic:notEmpty name="attr2">
		attr2不为空<br>
	</logic:notEmpty>
	<logic:present name="attr2">
		attr2存在<br>
	</logic:present>
	<logic:notPresent name="attr2">
		attr2不存在<br>
	</logic:notPresent>
	
	<p>
	<logic:empty name="attr3">
		attr3为空<br>
	</logic:empty>
	<logic:notEmpty name="attr3">
		attr3不为空<br>
	</logic:notEmpty>
	<logic:present name="attr3">
		attr3存在<br>
	</logic:present>
	<logic:notPresent name="attr3">
		attr3不存在<br>
	</logic:notPresent>
</body>
</html>