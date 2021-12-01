package com.bjsxt.struts;

public class UserManager {

	private static UserManager instance = new UserManager();
	
	private UserManager() {}
	
	public static UserManager getInstance() {
		return instance;
	}
	
	public void login(String username, String password) {
		if (!"admin".equals(username)) {
			throw new UserNotFoundException();
		}
		if (!"admin".equals(password)) {
			throw new PasswordErrorException();
		}
	}
	
}
