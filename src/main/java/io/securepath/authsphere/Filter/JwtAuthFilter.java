package io.securepath.authsphere.Filter;

import io.jsonwebtoken.Claims;
import io.securepath.authsphere.IOJwt.JWTClaim;
import io.securepath.authsphere.IOJwt.JwtUtility;
import io.securepath.authsphere.bo.UserBo;
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
            System.out.println("lBaseURL: " + getEndpoint(lBaseURL));

            String lJWT = JwtUtility.getToken(request.getHeader("Authorization"));


            String gTokenStatus = JwtUtility.validateToken(lJWT,getEndpoint(lBaseURL));
            if (!gTokenStatus.equals("SUCCESS")) {
                filterChain.doFilter(request, response);
            }
            Claims claims = JwtUtility.getClaimsFromToken(lJWT);
           Long lUserId = claims.get(JWTClaim.USERID, Long.class);
           Users User = gUserDao.getUser(lUserId);




            String username = "Prathmesh";
            // String role = gJwtService.extractRole(token);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            User.getUserName(),
                            null,
                            List.of(new SimpleGrantedAuthority("USER"))
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);


            filterChain.doFilter(request, response);
        } catch (Exception e) {

        }

    }


    public static String getEndpoint(String url) {
        URI uri = URI.create(url);
        String URL=  uri.getPath();

        String[] parts = URL.split("/");
        return  parts[parts.length - 1]; // returns "/login"// returns "/authserver/login"
    }


}
