1、struts国际化的配置
	* 在struts-config.xml文件中加入：<message-resources parameter="MessageResources" />
	
2、提供不同版本的国际化资源文件，中文需要采用native2ascii转换成unicode

3、在jsp中采用<bean:message>标签来读取国际化消息文本

4、了解利用struts默认将locale放到session中的特性，完成采用编程的方式切换语言设置
	* 参见：ChangeLanguageAction.java
	
5、消息文本的国际化处理，共有三个步骤：
	* 创建国际化消息
	* 传递国际化消息
	* 显示国际化消息
	
如何创建国际化消息？
	理解ActionMessage和ActionMessages两个对象的区别
	
如何传递国际化消息？
	* 调用saveMessage()传递普通消息，调用saveErrors传递错误消息
	
如何显示国际化消息？
	通过<html:messages>标签显示消息（可以显示普通消息和错误消息）
	通过<html:errors>显示消息（只能显示错误消息）		