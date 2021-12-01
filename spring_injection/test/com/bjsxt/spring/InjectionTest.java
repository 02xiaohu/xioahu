
 package com.bjsxt.spring;

 import org.springframework.beans.factory.BeanFactory;
 import org.springframework.context.support.ClassPathXmlApplicationContext;
 import junit.framework.TestCase;

 public class InjectionTest extends TestCase
 {
	
	private BeanFactory factory;
	
	@Override
	protected void setUp() throws Exception {
		System.out.println("测试前的准备");
		factory = new ClassPathXmlApplicationContext("applicationContext-*.xml");	
	}

	public void testInjection1() {
		
		Bean1 bean11 = (Bean1)factory.getBean("bean1");
	    Bean1 bean12= (Bean1)factory.getBean("bean1");
		System.out.println(bean11==bean12);
	    System.out.println("bean1.strValue=" + bean11.getStrValue());
		System.out.println("bean1.intValue=" + bean11.getIntValue());
		System.out.println("bean1.listValue=" + bean11.getListValue());
		System.out.println("bean1.setValue=" + bean11.getSetValue());
		System.out.println("bean1.arrayValue=" + bean11.getArrayValue());
		System.out.println("bean1.mapValue=" + bean11.getMapValue());
		System.out.println("bean1.dateValue=" + bean11.getDateValue());
	}
	
	public void testInjection2() {
		Bean2 bean2 = (Bean2)factory.getBean("bean2");
		
		System.out.println("bean2.bean3.id=" + bean2.getBean3().getId());
		System.out.println("bean2.bean3.name=" + bean2.getBean3().getName());
		System.out.println("bean2.bean3.password=" + bean2.getBean3().getPassword());
		System.out.println("bean2.bean4.id=" + bean2.getBean4().getId());
		System.out.println("bean2.bean4.name=" + bean2.getBean4().getName());
		System.out.println("bean2.bean5.age=" + bean2.getBean5().getAge());
	}
	
}
