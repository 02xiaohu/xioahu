
  package spring.annotation.config;

  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.ComponentScan;
  import org.springframework.context.annotation.Configuration;
  import spring.annotation.bean.Car;
  
  @ComponentScan("spring.annotation")
  @Configuration
  public class MainConfigOfLifeCycle {
	//@Scope("prototype")
	    @Bean(initMethod="init",destroyMethod="detory")
		public Car car(){
			return new Car();
		}
  
  }
