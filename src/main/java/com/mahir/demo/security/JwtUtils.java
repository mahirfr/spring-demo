package com.mahir.demo.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtils {
    public String generateJwt(MyUserDetails myUserDetails) {

        Map<String, Object> data = new HashMap<>();
        data.put("firstName", myUserDetails.getUser().getFirstName());
        data.put("lastName" , myUserDetails.getUser().getLastName ());
        data.put("role"     , myUserDetails.getUser().getRole     ().getName());

        return Jwts.builder()
                .setClaims(data)
                .setSubject(myUserDetails.getUsername())
                .signWith(SignatureAlgorithm.HS256, "azerty")
                .compact();
    }

    public Claims getData(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey("azerty")
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (MalformedJwtException e) {
            return null;
        }
    }

    public boolean isTokenValid(String jwt) {
        try {
            Claims data = getData(jwt);
        } catch (SignatureException e) {
            return false;
        }
        return true;
    }

}
