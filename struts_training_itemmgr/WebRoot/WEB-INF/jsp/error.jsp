<%@ page contentType="text/html;charset=GB18030"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html>
	<head>
		<title>������Ϣ!</title>
		<meta http-equiv="content-type" content="text/html;charset=gb2312">
		<style type="text/css">
<!--
.STYLE1 {
	font-size: 24px;
	font-weight: bold;
	color: #FF0000;
}
-->
</style>
	</head>
	<body>
		<br>
		<br>
		<center>
			<table width="500">
				<tr>
					<td align="center">
						<p align="left">
							<span class="STYLE1">������Ϣ��</span>
					</td>
				</tr>
			</table>
			<hr>
			<table width="500">
				<tr>
					<td>
						<html:errors/>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2">
							<input type="button" name="goback" value="����"
								onClick="javascript:history.go(-1);">
					</td>
				</tr>
			</table>
		</center>
	</body>
</html>
