package com.cassiomolin.listela.auth;

import com.cassiomolin.listela.auth.service.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String authenticationToken = (String) authentication.getCredentials();
        AuthenticationTokenDetails authenticationTokenDetails = authenticationTokenService.parseToken(authenticationToken);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationTokenDetails.getUsername());

        return new JwtAuthenticationToken(userDetails, authenticationTokenDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}