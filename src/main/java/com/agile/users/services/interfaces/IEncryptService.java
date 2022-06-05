package com.agile.users.services.interfaces;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public interface IEncryptService {
  public PasswordEncoder getEncoder();
  public String encode(String value);
}
