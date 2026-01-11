package com.example.be_kiotviet.controller;

import com.example.be_kiotviet.dto.Auth.CheckShopRequest;
import com.example.be_kiotviet.dto.Auth.LoginRequest;
import com.example.be_kiotviet.dto.ShopResponse;
import com.example.be_kiotviet.dto.User.UserDto;
import com.example.be_kiotviet.security.JwtTokenProvider;
import com.example.be_kiotviet.service.Shop.ShopService;
import com.example.be_kiotviet.service.User.UserServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ShopService shopService;
    private final Environment environment;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // 1. Kiểm tra input bắt buộc
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.status(400).body("Tên đăng nhập và mật khẩu không được để trống");
            }

            if (loginRequest.getShopCode() == null || loginRequest.getShopCode().isBlank()) {
                return ResponseEntity.status(400).body("Mã gian hàng không được để trống");
            }

            // 2. Xác thực username + password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username = authentication.getName();

            // 3. Lấy user từ DB
            com.example.be_kiotviet.entity.User userEntity = userService.findByUsername(username);
            if (userEntity == null) {
                return ResponseEntity.status(404).body("User không tồn tại");
            }

            // 4. **KIỂM TRA USER CÓ THUỘC SHOP ĐÓ KHÔNG**
            if (!userEntity.getShop().getCode().equalsIgnoreCase(loginRequest.getShopCode())) {
                return ResponseEntity.status(403).body("Tài khoản không thuộc gian hàng này");
            }

            // 5. Tạo JWT token (có thể thêm shopId vào claims nếu cần)
            String jwtToken = jwtTokenProvider.generateToken(userEntity);

            // 6. Set HttpOnly cookie
            Cookie cookie = new Cookie("access_token", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // true nếu dùng HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setAttribute("SameSite", "None");
            cookie.setAttribute("Partitioned", "true");
            response.addCookie(cookie);

            // 7. Trả về UserDto (có thể thêm shop info)
            UserDto userDto = userService.getByUsername(username);
            // Có thể thêm shop vào response nếu FE cần
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("user", userDto);

            return ResponseEntity.ok(responseBody);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Sai tên đăng nhập hoặc mật khẩu");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi hệ thống: " + e.getMessage());
        }
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserDto userDto = userService.getByUsername(username);
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/check-shop")
    public ResponseEntity<ShopResponse> checkShop(@RequestBody CheckShopRequest request) {
        ShopResponse shop = shopService.getByCode(request.getCode());
        return ResponseEntity.ok(shop);
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Xóa Authentication trong SecurityContext
        SecurityContextHolder.clearContext();

        // Tạo cookie với cùng tên, cùng attribute như khi set, nhưng value null và maxAge = 0
        Cookie cookie = new Cookie("access_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // BẮT BUỘC true nếu bạn set Secure=true khi login (đặc biệt khi dùng HTTPS hoặc localhost test)
        cookie.setPath("/");    // Phải giống hệt path khi set cookie
        cookie.setMaxAge(0);    // Xóa ngay lập tức

        // Quan trọng với Chrome: SameSite=None phải đi kèm Secure
        cookie.setAttribute("SameSite", "None");

        // Một số browser cần Partitioned nếu bạn dùng CHIPS (Cookie Having Independent Partitioned State)
        // Nếu bạn có dòng này khi login thì phải có khi logout
         cookie.setAttribute("Partitioned", "true");

        response.addCookie(cookie);

        return ResponseEntity.ok("Đăng xuất thành công");
    }
}