package com.company.pawpet.Security;

import com.company.pawpet.Model.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

//DO NOT WRITE OR DELETE ANYTHING
//DO NOT WRITE OR DELETE ANYTHING
//DO NOT WRITE OR DELETE ANYTHING
//DO NOT WRITE OR DELETE ANYTHING
//DO NOT WRITE OR DELETE ANYTHING
//DO NOT WRITE OR DELETE ANYTHING


@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your_secret_key_which_should_be_at_least_32_bytes_long";
    private static final long EXPIRATION_TIME = 86400000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String generateToken(UserDetails userDetails) {
        AppUser user = (AppUser) userDetails;


        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("appUserId",user.getAppUserId())
               // .claim("firstname", user.getFirstname())
                //.claim("lasname", user.getLastname())
               // .claim("gender",user.getGender())
                .claim("role", userDetails.getAuthorities())
                //.claim("image",user.getImage().toString())
                //.claim("address", user.getAddress())
                //.claim("birthDate",user.getBirthDate().toString())
                //.claim("phone",user.getPhone())
                //.claim("username",user.getUsername())
               // .claim("password",user.getPassword())
                .setIssuedAt(new Date())  // Set issue time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}

