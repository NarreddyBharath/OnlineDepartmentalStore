package com.training.service;


import java.sql.SQLException;

import com.training.dao.AdminValidation;
import com.training.dao.UserValidation;

public class UserService {
	UserValidation uv;
	AdminValidation av;
	public UserService() {
		uv = new UserValidation();
		av = new AdminValidation();
		}
	public void acceptuser() throws SQLException {
		uv.register();
	}
	public void valid(String username, String pass) throws SQLException {
		uv.valid(username,pass);
	}
	public void validadmin() throws SQLException {
		av.adminValid();
	}
}
