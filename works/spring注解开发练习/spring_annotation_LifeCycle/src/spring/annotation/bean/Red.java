  
  package spring.annotation.bean;

  import org.springframework.beans.BeansException;
  import org.springframework.beans.factory.BeanNameAware;
  import org.springframework.context.ApplicationContext;
  import org.springframework.context.ApplicationContextAware;
  import org.springframework.context.EmbeddedValueResolverAware;
  import org.springframework.stereotype.Component;
  import org.springframework.util.StringValueResolver;
  
   @Component
   public class Red implements ApplicationContextAware, BeanNameAware
		 {
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		System.out.println("传入的ioc："+applicationContext);
		this.applicationContext = applicationContext;

	}

	@Override
	public void setBeanName(String name) {
		System.out.println("当前bean的名字："+name);

	}

	public Red() {
		System.out.println("Red constructor...");
	}

//	@Override
//	public void setEmbeddedValueResolver(StringValueResolver resolver) {
//		String resolveStringValue = resolver.resolveStringValue("你好 ${os.name} 我是 #{20*18}");
//		System.out.println("解析的字符串："+resolveStringValue);
//
//	}

  }
