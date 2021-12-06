
  package spring_annotation_scpe;
 
  import org.junit.Test;
  import org.springframework.context.annotation.AnnotationConfigApplicationContext;
  import com.annotation.config.MainConfig1;
  import com.annotation.bean.Person;
  
  public class IOCTest {
	
	@Test
	public void test01()
	{ //���������ഴ��ioc����
	 AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig1.class);
	 
	System.out.println("===================ioc�����������=============================");
	Object bean1 = applicationContext.getBean("person01");
	Object bean2 = applicationContext.getBean("person01");
	Person person = applicationContext.getBean(Person.class);
	System.out.println(bean1 == bean2);
	System.out.println(bean1 == person);
	System.out.println(person);
	  }
  
    }
