
  package com.annotation.config;

  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.ComponentScan;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.context.annotation.Import;
  import org.springframework.context.annotation.Lazy;
  import org.springframework.context.annotation.Scope;
  import com.annotation.bean.Color;
  import com.annotation.bean.Person;

  //配置类--配置文件 告诉Spring这是一个配置类
  @Configuration
  @Import(value=Color.class)//容器中会自动注册这个组件，id默认是全类名
  //包扫描  value:指定要扫描的包   默认扫描标注@Controller/@Service/@Repository/@Component注解的组件 注册到容器中
  @ComponentScan(value="com.annotation") 
  public class MainConfig1{
	
	@Bean("person")//给容器中注册一个Bean;类型为返回值的类型，id默认是用方法名作为id
	public Person person01(){
		System.out.println("给容器中添加Person....");
		return new Person("tom", 20,"tom");
	 }

  }
