package com.project.schoolmanagment.security.jwt;

import com.project.schoolmanagment.security.service.UserDetailServiceImpl;
import com.project.schoolmanagment.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

   @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserDetailServiceImpl userDetailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try{
            // 1) from every request we will get JWT
            String jwt = parseJwt(request);

            // 2) validate JWT
            if (jwt != null && jwtUtils.validateJwtToken(jwt)){

                // 3) we need username for to get data
                String userName = jwtUtils.getUsernameFromJwtToken(jwt);

                // 4) check DB and find the user and upgrade it with userDetails
                UserDetails userDetails = userDetailService.loadUserByUsername(userName);

                // 5) we are setting attribute prop with username
                request.setAttribute("username",userName);

                // 6) we have (user details) object then we have to send this information to SECURITY CONTEXT
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7) now Spring context know who is logged in.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (UsernameNotFoundException error){
            LOGGER.error("Cannot set user authentication:",error);
        }
        filterChain.doFilter(request,response);
    }


    // Take from Header JWT token of User
    private String parseJwt(HttpServletRequest request){

        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }
        return null;
    }
}
