
  package spring.annotation.srvice;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;
  import spring.annotation.dao.UserDao;
  
  @Service
  public class UserService {
	    
	  @Autowired
		private UserDao userDao;
	    /**֯�����ﴫ������(Before Advice)
		 * @Transactional(propagation=Propagation.REQUIRED) ��
		 * ���������, ��ô�����������������, û�еĻ�����������½�һ������(Ĭ�������)
		 * @param user
		 */
		@Transactional
		public void insertUser(){
			userDao.insert();
			System.out.println("�������...");
			//int i = 10/0;
		}
  }
