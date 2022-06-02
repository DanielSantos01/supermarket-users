package com.agile.users.configs.security;

import javax.servlet.http.HttpServletResponse;

import com.agile.users.configs.security.auth.TokenGenerator;
import com.agile.users.configs.security.auth.TokenValidator;
import com.agile.users.configs.security.auth.UserDetailService;
import com.agile.users.services.EncryptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserDetailService userDetailService;

  @Autowired
  private EncryptService encryptServices;

  @Value("${security.jwt.expiration-time}")
  private long tokenExpirationTime;

  @Value("${security.jwt.password}")
  private String tokenPassword;

  @Value("${secutiry.request.header-key}")
  private String tokenHeaderKey;

  @Value("${secutiry.request.header-prefix}")
  private String tokenHeaderPrefix;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(this.userDetailService).passwordEncoder(this.encryptServices.getEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    TokenGenerator tokenGenerator = this.getTokenGenerator();
    TokenValidator tokenValidator = this.getTokenValidator(tokenGenerator);
    http.csrf().disable().cors().disable().authorizeRequests()
      .antMatchers("/login")
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .addFilter(tokenGenerator)
      .addFilter(tokenValidator)
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .exceptionHandling()
      .authenticationEntryPoint((request, response, ex) -> {
        response.sendError(
            HttpServletResponse.SC_UNAUTHORIZED,
            ex.getMessage()
        );
    });
  }

  private TokenGenerator getTokenGenerator() throws Exception {
    return new TokenGenerator(this.authenticationManager(), this.tokenPassword, this.tokenExpirationTime);
  }

  private TokenValidator getTokenValidator(TokenGenerator tokenGenerator) throws Exception {
    TokenValidator tokenValidator = new TokenValidator(
      this.authenticationManager(),
      tokenGenerator,
      this.tokenHeaderKey,
      this.tokenHeaderPrefix
    );
    return tokenValidator;
  }
}
