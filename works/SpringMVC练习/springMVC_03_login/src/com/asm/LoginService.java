package com.asm;

import org.springframework.web.servlet.ModelAndView;

public class LoginService {
private User user;

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}
boolean login(User user)
{ String username=user.getUsername();
  String password=user.getPassword();
  if (!"admin".equals(username) || !"admin".equals(password)) {
		return false;
	}
  return true;
}
}
