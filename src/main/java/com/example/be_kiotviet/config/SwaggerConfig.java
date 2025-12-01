package com.example.be_kiotviet.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Mini KiotViet API",
                version = "1.0.0",
                description = """
            API hệ thống quản lý bán hàng đa cửa hàng (multi-tenant) giống KiotViet 98%
            Đồ án tốt nghiệp 2025 - Nhóm 10 điểm
            """,
                contact = @Contact(
                        name = "Nghia251506",
                        email = "ntn8530@gmail.com",
                        url = "https://github.com/Nghia251506/BE_KiotViet"
                ),
                license = @License(name = "MIT License")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development"),
                @Server(url = "https://api-mini-kiotviet.up.railway.app", description = "Production (nếu deploy)")
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Nhập JWT token sau khi đăng nhập. Ví dụ: Bearer eyJhbGciOiJIUzI1NiIs..."
)
@Configuration
public class SwaggerConfig {
    // Với Spring Boot 3.5.8 + springdoc-openapi → chỉ cần class này là đủ!
    // Không cần bean gì thêm, tự động scan toàn bộ @RestController
}
