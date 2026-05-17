package lt.teamProject.smartCarCosts.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "SmartCarCosts_Super_Secret_Key_For_JWT_Tokenization_2026";
    private static final String ISSUER = "SmartCarCostsApp";
    private static final long EXPIRATION_TIME = 86400000; // Токен живет 24 часа

    public static String generateToken(Long userId, String userName, String email) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("userId", userId)
                .withClaim("userName", userName)
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
        } catch (Exception e) {
            return null;
        }
    }
}