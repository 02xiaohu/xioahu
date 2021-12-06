
  package com.annotation.config;

  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.ComponentScan;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.context.annotation.Import;
  import org.springframework.context.annotation.Lazy;
  import org.springframework.context.annotation.Scope;
  import com.annotation.bean.Color;
  import com.annotation.bean.Person;

  //������--�����ļ� ����Spring����һ��������
  @Configuration
  @Import(value=Color.class)//�����л��Զ�ע����������idĬ����ȫ����
  //��ɨ��  value:ָ��Ҫɨ��İ�   Ĭ��ɨ���ע@Controller/@Service/@Repository/@Componentע������ ע�ᵽ������
  @ComponentScan(value="com.annotation") 
  public class MainConfig1{
	
	@Bean("person")//��������ע��һ��Bean;����Ϊ����ֵ�����ͣ�idĬ�����÷�������Ϊid
	public Person person01(){
		System.out.println("�����������Person....");
		return new Person("tom", 20,"tom");
	 }

  }
