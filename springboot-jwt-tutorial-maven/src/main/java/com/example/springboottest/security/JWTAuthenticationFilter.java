package com.example.springboottest.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.example.springboottest.constant.SecurityConstants;
import com.example.springboottest.model.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		try {
			ApplicationUser creds = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
			
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
					    creds.getUsername(),
					    creds.getPassword(),
					    new ArrayList<>()
					)
			);
		} catch(IOException  e) {
			throw new RuntimeException(e);
		}
		
		
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
			FilterChain chain, Authentication authResult) throws IOException, ServletException {

//		( (User)authResult.getPrincipal() ).getUsername()
		
		String token = JWT.create()
					       .withSubject( ( (User)authResult.getPrincipal() ).getUsername() )
					       .withExpiresAt( new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME) )
					       .sign( HMAC512( SecurityConstants.SECRET.getBytes() ) );
		
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
	
	}

}
