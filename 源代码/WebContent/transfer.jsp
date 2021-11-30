<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<body>
<% String error=(String)session.getAttribute("Error");
String Moneyerror=(String)session.getAttribute("MoneyError");
String AccountError=(String)session.getAttribute("AccountError");
if(error!=null){
	out.println("<script>alert('输入无效，请再次检查')</script>");
	session.removeAttribute("Error");
}
if(Moneyerror!=null){
	out.println("<script>alert('很抱歉，您没有这么多钱！')</script>");
	session.removeAttribute("Moneyerror");
}
if(AccountError!=null){
	out.println("<script>alert('查无此人，请输入正确的用户')</script>");
	session.removeAttribute("AccountError");
}
%>
<form action="transferr.do" method="post">
请输入金额：<input type="text" name="much"><br>
 请再次输入：<input type="text" name="reMuch"><br>
 请输入转入人的姓名<input type="text" name="toWhom"><br>
 <input type="submit" value="确定">
 </form>
</body>
</html>