struts标签的配置和使用

配置：
	* 在struts-config.xml文件中加入
	<message-resources parameter="MessageResources" />
	* 拷贝MessageResources.properties文件到src下
使用：
	* 采用taglib指令引入
	<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>  	
	<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%> 