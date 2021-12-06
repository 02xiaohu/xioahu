
  package com.annotation.config;
  
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.context.annotation.Import;
  import org.springframework.context.annotation.Lazy;
  import org.springframework.context.annotation.Scope;
  import com.annotation.bean.Person;

  //配置类--配置文件 告诉Spring这是一个配置类
   
  @Configuration
  public class MainConfig1 {
	/**@Scope:调整作用域 默认是单实例的
	 * prototype：多实例的：ioc容器启动并不会去调用方法创建对象放在容器中。
	 * 每次获取的时候才会调用方法创建对象；
	 * singleton：单实例的（默认值）：ioc容器启动会调用方法创建对象放到ioc容器中。
	 * 以后每次获取就是直接从容器（map.get()）中拿，
	 * request：同一次请求创建一个实例
	 * session：同一个session创建一个实例懒加载：
	 * 单实例bean：默认在容器启动的时候创建对象；
	 * 懒加载：@Lazy容器启动不创建对象。第一次使用(获取)Bean创建对象，并初始化；
	 * 
	 */
	//@Lazy
	@Scope("prototype")
	@Bean//给容器中注册一个Bean;类型为返回值的类型，id默认是用方法名作为id
	public Person person01(){
		System.out.println("给容器中添加Person....");
		return new Person("tom", 20,"tom");
	 }

  }
