package com.upipaysystem.payroll.filters;

import com.upipaysystem.payroll.service.MyUserDetailsService;
import com.upipaysystem.payroll.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        //Checking if the Authorization header has any values id so also checking if it starts with Bearer
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            email = jwtUtil.extractEmail(token);
        }

        //checking username is null or not and if the request is authenticated or not.
        //If not Authenticated
        //--> Validate token
        //--> Generate Authentication Object( UsernamePasswordAuthenticationToken )
        if(email != null  && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(email);

            if(jwtUtil.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
