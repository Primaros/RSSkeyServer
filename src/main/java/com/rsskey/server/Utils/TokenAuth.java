package com.rsskey.server.Utils;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.User;
import com.rsskey.server.Repository.UserRepository;
import io.jsonwebtoken.*;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

/*
    Our simple static class that demonstrates how to create and decode JWTs.
 */
public class TokenAuth {

    // The secret key. This should be in a property file NOT under source
    // control and not hard coded in real life. We're putting it here for
    // simplicity.
    private static String SECRET_KEY = "ABCD1234";

    //Sample method to construct a JWT
    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static boolean TokenIsValid(String token) {
        Boolean isValid = true;

        try {
            decodeJWT(token);

        } catch (Exception e) {
               isValid = false;
        }
        return isValid;
    }

    public static User getUserByToken(String token) {
        if (token == null) {
            return null;
        }
        UserRepository repo = DAOFactory.getInstance().getUserRepository();
        if (!TokenAuth.TokenIsValid(token)) {
            return null;
        }
        return repo.findByToken(token);
    }

    public static Claims decodeJWT(String token) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token).getBody();
        return claims;
    }

}

