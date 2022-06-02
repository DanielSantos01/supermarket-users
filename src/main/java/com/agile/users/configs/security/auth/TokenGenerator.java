package com.agile.users.configs.security.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agile.users.configs.security.data.AuthUserDetails;
import com.agile.users.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class TokenGenerator extends UsernamePasswordAuthenticationFilter {
  private final long tokenExpirationTime;

  private final String tokenPassword;
  
  private final AuthenticationManager authenticationManager;

  public TokenGenerator(AuthenticationManager authenticationManager, String password, long expiration) {
    this.authenticationManager = authenticationManager;
    this.tokenPassword = password;
    this.tokenExpirationTime = expiration;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
    try {
      User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
      return this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword(), new ArrayList<>())
      );
    } catch (IOException e) {
      throw new RuntimeException("Unable to authenticate user");
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult
    ) throws IOException, ServletException {
    AuthUserDetails userDetails = (AuthUserDetails) authResult.getPrincipal();

    String token = JWT
      .create()
      .withSubject(userDetails.getUsername())
      .withExpiresAt(new Date(System.currentTimeMillis() + this.tokenExpirationTime))
      .sign(Algorithm.HMAC512(this.tokenPassword));

    response.getWriter().write(token);
    response.getWriter().flush();
  }

  protected String getTokenPassword() {
    return this.tokenPassword;
  }

  protected long getTokenExpirationTime() {
    return this.tokenExpirationTime;
  }
}
