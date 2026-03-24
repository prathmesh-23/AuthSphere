package io.securepath.authsphere.Filter;

import io.jsonwebtoken.Claims;
import io.securepath.authsphere.IOJwt.JWTClaim;
import io.securepath.authsphere.IOJwt.JwtUtility;
import io.securepath.authsphere.constants.ErrorConstant;
import io.securepath.authsphere.models.UserRole;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepo gUserDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String lBaseURL = request.getRequestURL().toString();
            String URLEndpoint = getEndpoint(lBaseURL);
            System.out.println("Endpoint: " + URLEndpoint);

            String authHeader = request.getHeader("Authorization");
            String lJWT = getToken(authHeader); // strips "Bearer "
            if (lJWT == null) {
                filterChain.doFilter(request, response);
                return;
            }
            String gTokenStatus = JwtUtility.validateToken(lJWT, URLEndpoint);
            if (!ErrorConstant.SUCCESS.equals(gTokenStatus)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Skip login and forgotpassword endpoints
            if (!URLEndpoint.equalsIgnoreCase("login") &&
                    !URLEndpoint.equalsIgnoreCase("forgotpassword")) {

                Claims claims = JwtUtility.getClaimsFromToken(lJWT);
                Long lUserId = claims.get(JWTClaim.USERID, Long.class);
                UserRole lUser = gUserDao.getUserRole(lUserId);

                System.out.println(lUser.toString());
                if (lUser != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    lUser.getUserName(),
                                    null,
                                    List.of(new SimpleGrantedAuthority(lUser.getRole_name())
                            ));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

    public static String getEndpoint(String url) {
        URI uri = URI.create(url);
        String path = uri.getPath();
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    public static String getToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}