package com.company.pawpet.Security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtConfig {
    private static final String SECRET = "ASQRiPcg940f1oXIua16rnwAEGzo7I1FIt0j8Hbw1YFrHyNsETlYy2W3OXP0I0ZAtgcQRxQLB6yUlNzZlac9qGAQ==";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

    public static SecretKey getSigningKey() {
        return KEY;
    }
}
