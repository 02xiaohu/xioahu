 <%@ page language="java" contentType="text/html; charset=GBK"  
   pageEncoding="GBK"%>  
 <%@ page import="java.lang.Exception"%>  
 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
 <html>  
 <head>  
 <meta http-equiv="Content-Type" content="text/html; charset=GBK">  
 <title>����ҳ��</title>  
 </head>  
 <body>  
   ��¼ʧ��<br>
 
 <%   
 Exception e = (Exception)request.getAttribute("exception");   
 out.print(e.getMessage());   
 %> <br>
 ${exception}
 <br> 
 
</body>  
 </html>  