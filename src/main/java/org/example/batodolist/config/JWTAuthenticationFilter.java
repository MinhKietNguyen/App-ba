package org.example.batodolist.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.batodolist.service.JWTService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            String username = null;
            
            try {
                username = jwtService.extractUsername(jwt);
            } catch (Exception e) {
                // Token không hợp lệ hoặc đã hết hạn
                System.out.println("JWT Filter: Cannot extract username from token: " + e.getMessage());
                // Không set authentication, để Spring Security xử lý (sẽ trả về 403)
                filterChain.doFilter(request, response);
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        // Token không hợp lệ hoặc đã hết hạn
                        System.out.println("JWT Filter: Token is invalid or expired for user: " + username);
                        // Không set authentication, để Spring Security xử lý (sẽ trả về 403)
                    }
                } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
                    // User không tồn tại trong database
                    System.out.println("JWT Filter: User not found: " + username);
                    // Không set authentication, để Spring Security xử lý (sẽ trả về 403)
                } catch (Exception e) {
                    // Lỗi khác khi load user
                    System.out.println("JWT Filter: Error loading user: " + e.getMessage());
                    e.printStackTrace();
                    // Không set authentication, để Spring Security xử lý (sẽ trả về 403)
                }
            }
        } catch (Exception e) {
            // Lỗi khác khi xử lý token
            System.out.println("JWT Filter: Error processing token: " + e.getMessage());
            e.printStackTrace();
            // Không set authentication, để Spring Security xử lý (sẽ trả về 403)
        }

        filterChain.doFilter(request, response);
    }
}