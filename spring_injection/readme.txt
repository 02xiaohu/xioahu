1、spring的普通属性注入	
	参见：spring文档3.3章节
	
什么是属性编辑器，作用？
	* 自定义属性编辑器，spring配置文件中的字符串转换成相应的对象进行注入
	spring已经有内置的属性编辑器，我们可以根据需求自己定义属性编辑器
	
	* 如何定义属性编辑器？
		* 继承PropertyEditorSupport类，覆写setAsText()方法，参见：UtilDatePropertyEditor.java
		* 将属性编辑器注册到spring中，参见：applicationContext-editor.xml
		
依赖对象的注入方式，可以采用：
	* ref属性
	* <ref>标签
	* 内部<bean>来定义
	
如何将公共的注入定义描述出来？
	* 通过<bean>标签定义公共的属性，指定abstract=true
	* 具有相同属性的类在<bean>标签中指定其parent属性
	
	参见：applicationContext-other.xml
				
		
			