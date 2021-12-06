  
  package spring.annotation.bean;

  import org.springframework.beans.BeansException;
  import org.springframework.beans.factory.config.BeanPostProcessor;
  import org.springframework.context.EmbeddedValueResolverAware;
  import org.springframework.stereotype.Component;
  import org.springframework.util.StringValueResolver;
  
  /**
   * ���ô���������ʼ��ǰ����д�����
   * �����ô��������뵽������
   * @author admin
   */
   @Component
   public class MyBeanPostProcessor implements  BeanPostProcessor {

	  

	  @Override
	  public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		     System.out.println("postProcessAfterInitialization..."+beanName+"=>"+bean);
			return bean;
		     
	  }

	  @Override
	  public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		    System.out.println("postProcessBeforeInitialization..."+beanName+"=>"+bean);
			return bean;
	  }

   }
