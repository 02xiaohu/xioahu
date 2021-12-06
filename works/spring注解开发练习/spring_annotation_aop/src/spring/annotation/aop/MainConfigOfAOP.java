  
  package spring.annotation.aop;

  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.context.annotation.EnableAspectJAutoProxy;
  
  @EnableAspectJAutoProxy
  @Configuration
  public class MainConfigOfAOP {
	//ҵ���߼������������
		@Bean
		public MathCalculator calculator(){
			return new MathCalculator();
		}

		//��������뵽������
		@Bean
		public LogAspects logAspects(){
			return new LogAspects();
		}
  }
