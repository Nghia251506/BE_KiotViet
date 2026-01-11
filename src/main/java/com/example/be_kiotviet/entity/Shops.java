package com.example.be_kiotviet.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shops", uniqueConstraints = @UniqueConstraint(columnNames = "slug"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Shops {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;
    @Column(nullable = false, length = 255)
    private String code;

    @Column(nullable = false, length = 255, unique = true)
    private String slug;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String domain;

    @Column(name = "package_type", length = 20)
    private String packageType = "starter";

    @Column(length = 20)
    private String status = "active";

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "created_at", updatable = false)
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
    // BỔ SUNG FIELD MÔ HÌNH KINH DOANH
    @Enumerated(EnumType.STRING) // Lưu tên enum vào DB (CAFE, FASHION...)
    @Column(name = "business_type", nullable = false)
    private BusinessType businessType;
    @Column(name = "business_description", length = 500)
    private String businessDescription;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer; // nullable = true cho khách lẻ

    @Column(name = "is_headquarter", nullable = false)
    private boolean isHeadquarterFlag = false; // true nếu là trụ sở chính
    // Nếu là chi nhánh → có thể thêm
    @ManyToOne
    @JoinColumn(name = "headquarter_id")
    private Shops parentHeadquarter; // trỏ trực tiếp đến trụ sở (dễ query)
    @Column(name = "verification_token", length = 255)
    private String verificationToken;

    @Column(name = "verification_token_expiry")
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime verificationTokenExpiry;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "status_confirm", length = 20)
    private String statusConfirm = "PENDING_VERIFICATION";

    private boolean setEmailVerified;
}
