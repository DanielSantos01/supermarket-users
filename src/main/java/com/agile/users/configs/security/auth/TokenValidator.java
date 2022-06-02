package com.agile.users.configs.security.auth;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class TokenValidator extends BasicAuthenticationFilter {
  private final String headerKey;

  private final String headerPrefix;

  private final TokenGenerator tokenGenerator;

  public TokenValidator(
      AuthenticationManager authenticationManager,
      TokenGenerator tokenGenerator,
      String headerKey,
      String headerPrefix
    ) {
    super(authenticationManager);
    this.tokenGenerator = tokenGenerator;
    this.headerKey = headerKey;
    this.headerPrefix = headerPrefix;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
    ) throws IOException, ServletException {
    String authorizationValue = request.getHeader(this.headerKey);

    if (authorizationValue == null || !authorizationValue.startsWith(this.headerPrefix)) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken auth = this.getAuthToken(authorizationValue.replace(this.headerPrefix, ""));
    SecurityContextHolder.getContext().setAuthentication(auth);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthToken(String token) {
    String username = JWT.require(Algorithm.HMAC512(this.tokenGenerator.getTokenPassword()))
      .build()
      .verify(token)
      .getSubject();

    if (username == null) return null;

    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
  }
}
