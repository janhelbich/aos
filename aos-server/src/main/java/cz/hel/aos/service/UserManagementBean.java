package cz.hel.aos.service;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.hel.aos.entity.Role;
import cz.hel.aos.entity.User;
import cz.hel.aos.util.HashProvider;

@Stateless
public class UserManagementBean implements UserManagement {
	
	private static final Logger logger = LoggerFactory.getLogger(UserManagementBean.class);
	
	@PersistenceContext
	private EntityManager em;

	// TODO optimalizace, hehe
	@Override
	public boolean isUserAllowed(String userName, String password,
			Set<String> neededRoles) {
		
		User user = checkCredentials(userName, password);
		
		List<Role> roles = user.getRoles();
		for (String neededRole : neededRoles) {
			boolean found = false;
			for (Role role : roles) {
				if (role.getRoleName().equals(neededRole)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public User checkCredentials(String userName, String password) {
		TypedQuery<User> q = em.createNamedQuery("User.findByLogin", User.class);
		q.setParameter("userName", userName);
		User user = null;
		try {
			user = q.getSingleResult();
		} catch (NoResultException e) {
			logger.warn("No user found for name {0}", userName);
			return null;
		}
		
		if (!HashProvider.hashesMatch(user.getPassword(), password)) {
			return null;
		}
		return user;
	}

	
}
