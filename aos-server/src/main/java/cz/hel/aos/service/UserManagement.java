package cz.hel.aos.service;

import java.util.Set;

import javax.ejb.Local;

import cz.hel.aos.entity.User;

@Local
public interface UserManagement {

	public boolean isUserAllowed(String userName, String password, 
			Set<String> neededRoles);

	public User checkCredentials(String userName, String password);
	
}
