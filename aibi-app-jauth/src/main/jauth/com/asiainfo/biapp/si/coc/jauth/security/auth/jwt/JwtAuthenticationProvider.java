package com.asiainfo.biapp.si.coc.jauth.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.asiainfo.biapp.si.coc.jauth.security.auth.JwtAuthenticationToken;
import com.asiainfo.biapp.si.coc.jauth.security.config.JwtSettings;
import com.asiainfo.biapp.si.coc.jauth.security.model.UserContext;
import com.asiainfo.biapp.si.coc.jauth.security.model.token.JwtToken;
import com.asiainfo.biapp.si.coc.jauth.security.model.token.RawAccessJwtToken;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link JwtToken} to perform authentication.
 * 
 * @author vladimir.stankovic
 *
 * Aug 5, 2016
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;
    
    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        String userId = jwsClaims.getBody().get("userId", String.class);
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
//        List<GrantedAuthority> authorities = scopes.stream()
//                .map(authority -> new SimpleGrantedAuthority(authority))
//                .collect(Collectors.toList());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String s : scopes){
            authorities.add(new SimpleGrantedAuthority(s));
        }
        
        UserContext context = UserContext.create(userId,subject, authorities);
        
        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
