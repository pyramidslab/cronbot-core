package io.outofbox.cronbot.security.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.security.auth.login.FailedLoginException;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyStore;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Ali Helmy
 */
@Component
public class TokenProvider implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String USER_ID = "UserID";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";

    private int tokenValidity;
    private Key key;

    public TokenProvider(@Value("${cronbot.security.jwt.access-token-validity:30}") int tokenValidity){
        this.tokenValidity = tokenValidity ;
    }

    public String getUsernameFromToken(String token) throws FailedLoginException {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) throws FailedLoginException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws FailedLoginException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws FailedLoginException {
        token = token.replace(TOKEN_PREFIX, "");
        return Jwts.parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isSameUser(String token, String id) throws FailedLoginException {
        Claims claims = getAllClaimsFromToken(token);
        String dbID = (String) claims.get(USER_ID);
        return dbID.equals(id);
    }

    private Boolean isTokenExpired(String token) throws FailedLoginException {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    //..........................................

    public String generateToken(Authentication authentication, String userID)  throws FailedLoginException{
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Calendar expiry = Calendar.getInstance();
        expiry.setTimeInMillis(System.currentTimeMillis());
        expiry.add(Calendar.MINUTE, tokenValidity);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setHeaderParam("typ","jwt")
                ////////////////////////
                .claim(AUTHORITIES_KEY, authorities).claim(USER_ID, userID)
                .signWith(SignatureAlgorithm.RS256, getKey())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiry.getTime())
                .compact();
    }


    private Key getKey() throws FailedLoginException {
        try {
            if (key == null) {
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                ClassLoader classLoader = getClass().getClassLoader();
                keystore.load(classLoader.getResourceAsStream("jwtauth.jks"), "perfect-gift".toCharArray());
                key = keystore.getKey("mykey", "perfect-gift".toCharArray());
            }
            return key;
        } catch (Exception e) {
            throw new FailedLoginException("Invalid security token provided");
        }
    }


    public Boolean validateToken(String token) throws FailedLoginException {
        final String username = getUsernameFromToken(token);
        return (username !=null && !isTokenExpired(token));
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication existingAuth) throws FailedLoginException {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(getKey());

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<SimpleGrantedAuthority> authorities;
        if(claims.get(AUTHORITIES_KEY).toString().isEmpty()){
            authorities = new LinkedList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }else {
            authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
        }

        return new UsernamePasswordAuthenticationToken(claimsJws.getBody().getSubject(), "", authorities);
    }
}