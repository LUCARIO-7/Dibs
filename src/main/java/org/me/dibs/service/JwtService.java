package org.me.dibs.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    public String generateToken(String username){
        Map<String,Object> claims= new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public Key getKey(){
        byte[] bytes= Decoders.BASE64.decode("234324324234asdsadadwasdasdasdasdcasdawasdasdcweqdasdweawdsdadad");
        return Keys.hmacShaKeyFor(bytes);
    }
}
