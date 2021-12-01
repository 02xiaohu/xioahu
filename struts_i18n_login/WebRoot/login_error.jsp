<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>       
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title></title>
</head>
<body>
	<font color="red">
	<li>
		<html:messages id="msg" property="error1">
			<bean:write name="msg"/>
		</html:messages>
	</li>
	</font>	
	<font color="blue">
	<li>
		<html:messages id="msg" property="error2">
			<bean:write name="msg"/>
		</html:messages>
	</li>	
	</font>	
	
</body>
</html>