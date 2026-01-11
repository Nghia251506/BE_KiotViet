package com.example.be_kiotviet.security;

import com.example.be_kiotviet.util.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("========== JWT FILTER START ==========");
        System.out.println("URI: " + request.getRequestURI() + " | Method: " + request.getMethod());

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                System.out.println("JWT extracted: " + jwt.substring(0, Math.min(20, jwt.length())) + "...");

                if (jwtTokenProvider.validateToken(jwt)) {
                    System.out.println("Token valid: true");

                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("Authentication set successfully for user: " + authentication.getName());

                        // ===== SET TENANT CONTEXT (SHOP ID) =====
                        Long shopId = jwtTokenProvider.getShopIdFromToken(jwt);
                        if (shopId != null) {
                            TenantContext.setCurrentShopId(shopId);
                            System.out.println(">>> TenantContext set shopId = " + shopId);
                        } else {
                            System.err.println(">>> WARNING: shopId is NULL in JWT claims!");
                        }
                        // =======================================
                    } else {
                        System.out.println("Authentication is NULL despite valid token");
                    }
                } else {
                    System.out.println("Token valid: false");
                }
            } else {
                System.out.println("No JWT token found in request");
            }
            System.out.println("========== JWT FILTER END ==========");
            filterChain.doFilter(request, response); // Đảm bảo gọi tiếp chain
        } catch (Exception ex) {
            System.err.println("ERROR in JWT Filter: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // ===== LUÔN CLEAR TENANT CONTEXT SAU KHI XỬ LÝ REQUEST =====
            TenantContext.clear();
            System.out.println(">>> TenantContext cleared");
            // ==========================================================
        }


    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // 1. Header Authorization: Bearer <token>
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            System.out.println("Found JWT in Authorization header");
            return token;
        }

        // 2. Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName()) ||
                        "jwt".equals(cookie.getName()) ||
                        "token".equals(cookie.getName())) {
                    System.out.println("Found JWT in cookie: " + cookie.getName());
                    return cookie.getValue();
                }
            }
        }

        // 3. Query parameter
        String paramToken = request.getParameter("access_token");
        if (StringUtils.hasText(paramToken)) {
            System.out.println("Found JWT in query param");
            return paramToken;
        }

        // 4. Custom header
        String customHeader = request.getHeader("X-Auth-Token");
        if (StringUtils.hasText(customHeader)) {
            System.out.println("Found JWT in X-Auth-Token header");
            return customHeader;
        }

        System.out.println("No JWT found in any location");
        return null;
    }
}