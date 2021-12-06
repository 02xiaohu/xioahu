package com.asm;


public class UserManager {

	public UserManager() {}
	public boolean login(String username, String password) {
		 
		if ("admin".equals(username)&&"admin".equals(password)) {
			return true;
		} else {
			return false;
		}
	

	}
}
