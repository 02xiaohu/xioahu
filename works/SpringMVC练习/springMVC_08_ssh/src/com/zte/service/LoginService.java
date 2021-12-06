package com.zte.service;
import javax.annotation.Resource;
import org.springframework.web.servlet.ModelAndView;
import com.zte.dao.UserDao;
import com.zte.model.User;
public class LoginService {
@Resource(name ="userDao") 
private UserDao userDao;

public void add(User user){
	System.out.println("UserService.add()");
	//userDao=new UserDao();
	userDao.add(user);
}

//public UserDao getUserDao() {
//	return userDao;
//}
//
//public void setUserDao(UserDao userDao) {
//	this.userDao = userDao;
//}


 
}
