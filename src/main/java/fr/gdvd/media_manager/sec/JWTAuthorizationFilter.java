package fr.gdvd.media_manager.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Log4j2
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin",
                "*");// CORS policy
        response.addHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, authorization");
        response.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
        response.addHeader("Access-Control-Allow-Methods",
                "GET,POST,PUT,DELETE,PATCH");
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else if (request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response);
        } else {
            String jwt = request.getHeader(SecurityParams.JWT_HEADER);
            if (jwt == null || !jwt.startsWith(SecurityParams.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            List<String> roles = new ArrayList<>();
            String username = "";
            try {
                JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.PRIVATE_SECRET)).build();
                DecodedJWT decodeJWT = verifier.verify(jwt.substring(SecurityParams.TOKEN_PREFIX.length()));
                username = decodeJWT.getSubject();
                roles = decodeJWT.getClaims().get("roles").asList(String.class);
            } catch (Exception e) {
                log.info(e.getMessage());
                if (e.getMessage().contains("The Token has expired on")) {
                    log.info("JWTerror : "+e.getMessage());
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(rn -> {
                authorities.add(new SimpleGrantedAuthority(rn));
            });
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            // Update JWT
            if(request.getRequestURL().indexOf("/infoJWT")>0){
                log.info("Request on infoJWT");
                jwt = JWT.create()
//                        .withIssuer(request.getRequestURI())
                        .withIssuer("/login")
                        .withSubject(username)
                        .withArrayClaim("roles", roles.toArray(new String[roles.size()]))
                        .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION))
                        .sign(Algorithm.HMAC256(SecurityParams.PRIVATE_SECRET));
                response.addHeader(SecurityParams.JWT_HEADER, jwt);
            }
            filterChain.doFilter(request, response);
        }

    }

}
