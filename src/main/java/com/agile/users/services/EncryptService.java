package com.agile.users.services;

import com.agile.users.services.interfaces.IEncryptService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptService implements IEncryptService {
  public PasswordEncoder getEncoder() {
    return new BCryptPasswordEncoder();
  }
}
