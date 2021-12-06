  
  package spring.annotation.aop;

  import org.junit.Test;
  import org.springframework.context.annotation.AnnotationConfigApplicationContext;

  public class IOCTest_AOP {
	  @Test
		public void test01(){
			AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);
			
			MathCalculator mathCalculator=(MathCalculator)applicationContext.getBean("calculator");
			
			mathCalculator.divCalculator(10, 10);
			
			//applicationContext.close();
		}
  }
