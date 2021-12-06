
  package spring_annotation_ioc;

   
  import org.junit.Test;
  import org.springframework.context.annotation.AnnotationConfigApplicationContext;
  import com.annotation.config.MainConfig1;
  import com.annotation.controller.BookController;
  import com.annotation.dao.BookDao;
  import com.annotation.service.BookService;

  public class IOCTest {
	
	@Test
	public void test01()
	{ //根据配置类创建ioc容器
	 AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig1.class);
	 //取得ioc容器创建的所有对象的id
	String[] definitionNames = applicationContext.getBeanDefinitionNames();
	for (String name : definitionNames) {
	    System.out.println(name);
	}
	System.out.println("===================ioc容器创建完成=============================");
	Object bean1 = applicationContext.getBean("person");
	System.out.println(bean1);
	BookController bookController=(BookController)applicationContext.getBean("bookController");
	System.out.println(bookController);
	BookService bookService=bookController.getBookService1();
	System.out.println(bookService);
	BookDao bookDao=bookService.getBookDao();
	System.out.println(bookDao);
	}
  
    }
