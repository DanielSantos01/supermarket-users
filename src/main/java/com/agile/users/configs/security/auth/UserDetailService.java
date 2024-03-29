package com.agile.users.configs.security.auth;

import java.util.Optional;

import com.agile.users.configs.security.data.AuthUserDetails;
import com.agile.users.entities.User;
import com.agile.users.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
  @Autowired
  private UserService userServices;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = this.userServices.findByName(username);
    if (!user.isPresent()) {
      throw new UsernameNotFoundException("No user was found with name " + username);
    }
    return new AuthUserDetails(user.get());
  }
}
