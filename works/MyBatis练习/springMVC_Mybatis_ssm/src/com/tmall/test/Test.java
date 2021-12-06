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
		//插入数据
//		User user=new User();
//        user.setUsername("lklk");
//        user.setPassword("12346");
//		userMapper.addUser(user); 
	   
		//根据id查找数据
//		 User element=userMapper.selectUserByID(14);
//		 System.out.print (element.getUsername());
//         System.out.println(element.getPassword());
//	    
  //       System.out.println("获得全查询列表");
//	        List<User> result=new ArrayList<User>();
//	        result = userMapper.selectUsers();
//	        for (Iterator<User> iter = result.iterator(); iter.hasNext();) {
//	            User element = (User) iter.next();
//	            System.out.print (element.getUsername());
//	            System.out.println(element.getPassword());
//	        }
	
         
             
                 User element=userMapper.selectUserByID(10);
             
                 System.out.println("更新前的用户 id为:"+element.getId());
                 System.out.println("更新前的用户Name为:"+element.getUsername());
                 System.out.println("更新前的用户password为:"+element.getPassword());
                
                 element.setUsername("浦东创新园区");
                 element.setPassword("12345678");
                 userMapper.updateUser(element);
                
                 
                 System.out.println("更新后的用户 id为:"+element.getId());
                 System.out.println("更新后的用户Name为:"+element.getUsername());
                 System.out.println("更新后的用户password为:"+element.getPassword());
            
             
             
	
	}

}


