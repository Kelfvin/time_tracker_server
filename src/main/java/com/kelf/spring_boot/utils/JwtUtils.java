package com.kelf.spring_boot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.Date;

public class JwtUtils {
    private static final Long EXPIRE = 1000 * 60 * 60 * 24 * 7L; // 7 days
    private static final String sceret = "kelf";

    // 生成token
    public static String generateToken(String username) {
        Date now = new Date(System.currentTimeMillis());
        Date expire = new Date(now.getTime() + EXPIRE);
        String token;
        token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS256, sceret)
                .compact();
        return token;

    }

    // 检查token
    public static boolean checkToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(sceret)
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 解析token
    public static Claims getClaimsByToken(String token) {
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(sceret)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}
