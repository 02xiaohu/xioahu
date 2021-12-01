jstl标签库的配置
          从jakarta-struts-1.2.2\dist\contrib\struts-el\lib目录下
	* 将jstl.jar和standard.jar拷贝到WEB-INF/lib下（如果使用el表达式，不用拷贝这两个jar）
	
	注意：jstl必须在能够支持j2ee1.4/servlet2.4/jsp2.0版本上的容器才能运行，这个环境
	     是目前较为常用的环境

	     
标签库的使用
	* 采用taglib指令引入
	<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>  
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
	
		
自定义函数库：
	1、定义类和方法（方法必须是public static）	
	2、编写自定义tld文件，并且将此文件放到WEB-INF或WEB-INF任意子目录下
	3、在jsp中采用taglib指令引入自定义函数库
	4、采用 前缀+冒号+函数名 调用即可	