package com.example.be_kiotviet.dto.Response.Shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopResponse {
    private Long id;
    private String name;
    private String slug;
    private String email;
    private String phone;
    private String domain;
    private String packageType;
    private String status;
    private LocalDateTime expiryDate;
    private LocalDateTime createdAt;
}
