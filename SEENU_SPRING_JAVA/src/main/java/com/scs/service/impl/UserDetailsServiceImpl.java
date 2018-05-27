package com.scs.service.impl;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scs.entity.model.UserInfo;
import com.scs.exception.ApiException;
import com.scs.service.UserDbServices;
import com.scs.util.Utility;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = Logger.getLogger(UserDbSevicesImpl.class);

	@Autowired
	private UserDbServices userDbServices;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserInfo activeUserInfo = new UserInfo();
		try {
			activeUserInfo = userDbServices.getUserByName(userName);
		} catch (ApiException ex) {
			logger.error(Utility.getExceptionMessage(ex));

		}
		if(activeUserInfo.getAccountLocked()==0L) {
			GrantedAuthority authority = new SimpleGrantedAuthority(activeUserInfo.getRole());
			return new User(activeUserInfo.getUserName(), activeUserInfo.getPassword(),
				Arrays.asList(authority));
		}else {
			GrantedAuthority authority = new SimpleGrantedAuthority(activeUserInfo.getRole());
			return new User("LockedUser","LockedPassword",Arrays.asList(authority));
		}
	}
}