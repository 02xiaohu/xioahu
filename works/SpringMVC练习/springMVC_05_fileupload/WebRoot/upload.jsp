<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>测试springmvc中上传的实现</title>
	</head>
	<body>
<form action="upload.do"  method="post" enctype="multipart/form-data">
			名字：<input type="text" name="name" /><br>
			上传的文件：<input type="file" name="myFile" /><br>
			<input type="submit" />
		</form>
	</body>
</html>
