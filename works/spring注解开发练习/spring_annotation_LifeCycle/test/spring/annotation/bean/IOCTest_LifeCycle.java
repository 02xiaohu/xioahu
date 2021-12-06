
  package spring.annotation.bean;

   
  import org.junit.Test;
  import org.springframework.context.annotation.AnnotationConfigApplicationContext;
  import spring.annotation.config.MainConfigOfLifeCycle;

  public class IOCTest_LifeCycle {
	  
	  @Test
	  public void test01(){
			//1、创建ioc容器
			AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
			System.out.println("容器创建完成...");
			Person person=applicationContext.getBean(Person.class);
			System.out.println(person.getAge());
			System.out.println(person.getName());
			System.out.println(person.getNickName());
			//关闭容器
			applicationContext.close();
		}
  }
