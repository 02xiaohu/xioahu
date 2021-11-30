<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
        <%@page import="java.util.*"%>
         <%@page import="com.SSSP.bean.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>聊天室</title>
</head>
<body>
<h1 >公聊信息</h1>
<hr>
<%
Map map=(Map)application.getAttribute("PUBLIC_MSG");
Iterator iter=map.entrySet().iterator();
while(iter.hasNext()){
	Map.Entry set=(Map.Entry)iter.next();
    Object o=set.getKey();
    if(o instanceof String){
    	String msg=(String)o;
    	if(msg.equals("public")){
    		List say=(List)set.getValue();
    		Iterator iterr=say.iterator();
    		while(iterr.hasNext()){
    			String sss=(String)iterr.next();
    			out.println(sss);
    			%>
    			<br>
    			<% 
    		}
    	
    	}
    }
}
%>
<hr>
<h1>私聊信息</h1>
<hr>
<% 
Map allmap=(Map)application.getAttribute("allmap");
Iterator iter2=allmap.entrySet().iterator();
while(iter2.hasNext()){
	Map.Entry entry=(Map.Entry)iter2.next();
	Object o=(Object)entry.getKey();
	if(o instanceof UserBean){
		UserBean user=(UserBean)o;
	UserBean me=(UserBean)session.getAttribute("UserBean");
	if(user.getName().equals(me.getName())){
		List receive=(List)entry.getValue();
		Iterator itera=receive.iterator();
		while(itera.hasNext()){
			String value=(String)itera.next();
			out.println(value);
			%>
			<br>
			<%
		}
		}
	}
}
%>

<hr>
</body>
</html>