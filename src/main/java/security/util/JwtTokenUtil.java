package security.util;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.util.stream.Collectors.toList;
import static security.constants.JWTConstants.TOKEN_PREFIX;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import model.entity.Role;
import model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 * */
@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;
    private static final String ROLES = "roles";

    private String secret = "someSecret";

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //retrieve roles from jwt token
    public List getRolesFromToken(String token) {
        return (ArrayList) getAllClaimsFromToken(token).get(ROLES);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //validate token
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    //generate token for user
    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES, userDetails.getRoles().stream().map(Role::getAuthority).collect(toList()));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        if (token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");
        }
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //while creating the token -
//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//2. Sign the JWT using the HS512 algorithm and secret key.
//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(HS512, secret).compact();
    }
}
