package com.tmall.test;
 
  

  import java.util.ArrayList;
  import java.util.Iterator;
  import java.util.List;

  import org.apache.ibatis.session.SqlSession;
  import org.springframework.context.ApplicationContext;
  import org.springframework.context.support.ClassPathXmlApplicationContext;

 
  import com.tmall.bean.User;
 
import com.tmall.inter.OperationMapper;

  public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
		OperationMapper userMapper = (OperationMapper)context.getBean("Mapper");
		//��������
//		User user=new User();
//        user.setUsername("lklk");
//        user.setPassword("12346");
//		userMapper.addUser(user); 
	   
		//����id��������
//		 User element=userMapper.selectUserByID(14);
//		 System.out.print (element.getUsername());
//         System.out.println(element.getPassword());
//	    
  //       System.out.println("���ȫ��ѯ�б�");
//	        List<User> result=new ArrayList<User>();
//	        result = userMapper.selectUsers();
//	        for (Iterator<User> iter = result.iterator(); iter.hasNext();) {
//	            User element = (User) iter.next();
//	            System.out.print (element.getUsername());
//	            System.out.println(element.getPassword());
//	        }
	
         
             
                 User element=userMapper.selectUserByID(10);
             
                 System.out.println("����ǰ���û� idΪ:"+element.getId());
                 System.out.println("����ǰ���û�NameΪ:"+element.getUsername());
                 System.out.println("����ǰ���û�passwordΪ:"+element.getPassword());
                
                 element.setUsername("�ֶ�����԰��");
                 element.setPassword("12345678");
                 userMapper.updateUser(element);
                
                 
                 System.out.println("���º���û� idΪ:"+element.getId());
                 System.out.println("���º���û�NameΪ:"+element.getUsername());
                 System.out.println("���º���û�passwordΪ:"+element.getPassword());
            
             
             
	
	}

}


