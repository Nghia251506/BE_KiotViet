package com.example.be_kiotviet.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers",
        indexes = {
                @Index(name = "idx_code", columnList = "code"),
                @Index(name = "idx_name", columnList = "name"),
                @Index(name = "idx_phone", columnList = "phone"),
                @Index(name = "idx_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mã nhà cung cấp (unique toàn hệ thống)
     * Ví dụ: NCC001, GHTK, VINAMILK...
     */
    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    /**
     * Tên nhà cung cấp
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Email liên hệ
     */
    @Column(name = "email", length = 255, unique = true)
    private String email;

    /**
     * Số điện thoại
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * Địa chỉ
     */
    @Column(name = "address", length = 500)
    private String address;

    /**
     * Người liên hệ (tên)
     */
    @Column(name = "contact_person", length = 255)
    private String contactPerson;

    /**
     * Ghi chú (hợp đồng, điều khoản thanh toán, thời gian giao hàng...)
     */
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    /**
     * Trạng thái hoạt động
     */
    @Column(name = "status", length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE

    /**
     * Thời gian tạo
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Thời gian cập nhật
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Quan hệ ngược với ProductSupplier (trung gian)
    // Không cần mappedBy ở đây vì đã có ở ProductSupplier
    // Nếu muốn lấy danh sách sản phẩm từ supplier → dùng query riêng
}
