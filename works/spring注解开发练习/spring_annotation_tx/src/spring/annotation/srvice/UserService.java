
  package spring.annotation.srvice;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;
  import spring.annotation.dao.UserDao;
  
  @Service
  public class UserService {
	    
	  @Autowired
		private UserDao userDao;
	    /**织入事物传播特性(Before Advice)
		 * @Transactional(propagation=Propagation.REQUIRED) ：
		 * 如果有事务, 那么事务管理器加入事务, 没有的话事务管理器新建一个事物(默认情况下)
		 * @param user
		 */
		@Transactional
		public void insertUser(){
			userDao.insert();
			System.out.println("插入完成...");
			//int i = 10/0;
		}
  }
