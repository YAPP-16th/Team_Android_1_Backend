package com.eroom.erooja.features.auth.jwt;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.eroom.erooja.domain.model.MemberAuth;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private static final long tokenTTL = 30 * 60 * 1000;
//    private static final long refreshTokenTTL = 24 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(MemberAuth memberAuth) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("provider", memberAuth.getAuthProvider());

        return doGenerateToken(claims, memberAuth.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenTTL))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String generateRefreshToken(MemberAuth memberAuth) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("provider", memberAuth.getAuthProvider());
        claims.put("isRefreshToken", true);
        
        return doGenerateRefreshToken(claims, memberAuth.getUsername());
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 2000);

        return Jwts.builder()
                .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(c.getTime())
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, MemberAuth memberAuth) {
        final String username = getUsernameFromToken(token);
        return (username.equals(memberAuth.getUsername()) && !isTokenExpired(token));
    }

    public boolean isUnMatchUidWithToken(String uid, String header) {
        return !uid.equals(getTokenFromHeader(header));
    }

    public String getUidFromHeader(String header) {
        return getUsernameFromToken(getTokenFromHeader(header));
    }

    public String getTokenFromHeader(String header) {
        return header.replace("Bearer ", "");
    }
}