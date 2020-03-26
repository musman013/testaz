package com.nfinity.ll.testaz;

import com.nfinity.ll.testaz.application.authorization.user.IUserAppService;
import com.nfinity.ll.testaz.security.SecurityUtils;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.irepository.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	public UserDetailsServiceImpl(IUserAppService userAppService) {
	}

	@Autowired
	private IUserRepository usersRepository;
	
	@Autowired
    private SecurityUtils securityUtils;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserEntity applicationUser = usersRepository.findByUserName(username);

	if (applicationUser == null) {
		throw new UsernameNotFoundException(username);
	}

	List<String> permissions = securityUtils.getAllPermissionsFromUserAndRole(applicationUser);
	String[] groupsArray = new String[permissions.size()];
   	List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissions.toArray(groupsArray));

    return new User(applicationUser.getUserName(), applicationUser.getPassword(), authorities); // User class implements UserDetails Interface
	}


}
