
  package com.annotation.config;
  
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.context.annotation.Import;
  import org.springframework.context.annotation.Lazy;
  import org.springframework.context.annotation.Scope;
  import com.annotation.bean.Person;

  //������--�����ļ� ����Spring����һ��������
   
  @Configuration
  public class MainConfig1 {
	/**@Scope:���������� Ĭ���ǵ�ʵ����
	 * prototype����ʵ���ģ�ioc��������������ȥ���÷�������������������С�
	 * ÿ�λ�ȡ��ʱ��Ż���÷�����������
	 * singleton����ʵ���ģ�Ĭ��ֵ����ioc������������÷�����������ŵ�ioc�����С�
	 * �Ժ�ÿ�λ�ȡ����ֱ�Ӵ�������map.get()�����ã�
	 * request��ͬһ�����󴴽�һ��ʵ��
	 * session��ͬһ��session����һ��ʵ�������أ�
	 * ��ʵ��bean��Ĭ��������������ʱ�򴴽�����
	 * �����أ�@Lazy�����������������󡣵�һ��ʹ��(��ȡ)Bean�������󣬲���ʼ����
	 * 
	 */
	//@Lazy
	@Scope("prototype")
	@Bean//��������ע��һ��Bean;����Ϊ����ֵ�����ͣ�idĬ�����÷�������Ϊid
	public Person person01(){
		System.out.println("�����������Person....");
		return new Person("tom", 20,"tom");
	 }

  }
