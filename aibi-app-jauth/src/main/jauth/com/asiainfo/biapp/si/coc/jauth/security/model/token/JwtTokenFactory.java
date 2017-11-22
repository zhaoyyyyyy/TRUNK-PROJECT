package com.asiainfo.biapp.si.coc.jauth.security.model.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.asiainfo.biapp.si.coc.jauth.security.config.JwtSettings;
import com.asiainfo.biapp.si.coc.jauth.security.model.Scopes;
import com.asiainfo.biapp.si.coc.jauth.security.model.UserContext;

/**
 * Factory class that should be always used to create {@link JwtToken}.
 * 
 * @author vladimir.stankovic
 *
 * May 31, 2016
 */
@Component
public class JwtTokenFactory {
    private final JwtSettings settings;

    @Autowired
    public JwtTokenFactory(JwtSettings settings) {
        this.settings = settings;
    }

    /**
     * Factory method for issuing new JWT Tokens.
     * 
     * @param username
     * @param roles
     * @return
     */
    public AccessJwtToken createAccessJwtToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) 
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) 
            throw new IllegalArgumentException("User doesn't have any privileges");

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
//        claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
        List<String> gs = new ArrayList<>();
        for(GrantedAuthority g : userContext.getAuthorities()){
            gs.add(g.toString());
        }
        claims.put("scopes", gs);
        claims.put("userId", userContext.getUserId());
//        LocalDateTime currentTime = LocalDateTime.now();
        Date currentTime = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        c.add(Calendar.MINUTE, settings.getTokenExpirationTime());
        Date time = c.getTime();
        
        String token = Jwts.builder()
          .setClaims(claims)
          .setIssuer(settings.getTokenIssuer())
//          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
          .setIssuedAt(currentTime)
//          .setExpiration(Date.from(currentTime
//              .plusMinutes(settings.getTokenExpirationTime())
//              .atZone(ZoneId.systemDefault()).toInstant()))
          .setExpiration(time)
          .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

//        LocalDateTime currentTime = LocalDateTime.now();
        Date currentTime = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        c.add(Calendar.MINUTE, settings.getTokenExpirationTime());
        Date time = c.getTime();

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("userId", userContext.getUserId());
        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
        
        String token = Jwts.builder()
          .setClaims(claims)
          .setIssuer(settings.getTokenIssuer())
          .setId(UUID.randomUUID().toString())
//          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
          .setIssuedAt(currentTime)
//          .setExpiration(Date.from(currentTime
//              .plusMinutes(settings.getRefreshTokenExpTime())
//              .atZone(ZoneId.systemDefault()).toInstant()))
          .setExpiration(time)
          .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();

        return new AccessJwtToken(token, claims);
    }
}
