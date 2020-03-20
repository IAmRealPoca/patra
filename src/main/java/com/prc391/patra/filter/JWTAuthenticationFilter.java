package com.prc391.patra.filter;

import com.prc391.patra.config.security.PatraUserPrincipal;
import com.prc391.patra.config.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JWTAuthenticationFilter extends GenericFilterBean {

    private final Logger logger = Logger.getLogger("JWTAuthenticationFilter");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = this.getAuthentication((HttpServletRequest) request);

//        if (authentication != null) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        try {
            if (token != null) {
                Jws<Claims> claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET)
                        .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
                Claims body = claims.getBody();
                String user = body.getSubject();
                List<String> authorities = body.get(SecurityConstants.JWT_CLAIMS_AUTHORITY, List.class);
                String currMemberId = body.get(SecurityConstants.JWT_CLAIMS_CURR_MEMBER_ID, String.class);

                //passed random password, because the User superclass does not allow null or empty password
                PatraUserPrincipal principal = new PatraUserPrincipal(user, "a", currMemberId, getGrantedAuthorities(authorities));
                return user != null ?
                        new UsernamePasswordAuthenticationToken(principal, null, getGrantedAuthorities(authorities)) : null;
            } else {
                //TODO: throw new Token Null Exception here
            }
        } catch (ExpiredJwtException ex) {
            logger.log(Level.INFO, "Header: " + ex.getHeader() + " Claims: " + ex.getClaims() + "Token expired: " + ex.getMessage());
//            throw new ExpiredJwtException(ex.getHeader() ,ex.getClaims(), "Token expired: " + ex.getMessage());
        }
        //TODO: throw exception
        return null;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

//    private List<GrantedAuthority> getGrantedAuthorities(List<Map<String, Object>> permissions) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (Map permission : permissions) {
//            authorities.add(new SimpleGrantedAuthority((String) permission.get("authority")));
//        }
//        return authorities;
//    }
}