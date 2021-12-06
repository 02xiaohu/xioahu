
  package spring.annotation.bean;

   
  import org.junit.Test;
  import org.springframework.context.annotation.AnnotationConfigApplicationContext;
  import spring.annotation.config.MainConfigOfLifeCycle;

  public class IOCTest_LifeCycle {
	  
	  @Test
	  public void test01(){
			//1������ioc����
			AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
			System.out.println("�����������...");
			Person person=applicationContext.getBean(Person.class);
			System.out.println(person.getAge());
			System.out.println(person.getName());
			System.out.println(person.getNickName());
			//�ر�����
			applicationContext.close();
		}
  }
