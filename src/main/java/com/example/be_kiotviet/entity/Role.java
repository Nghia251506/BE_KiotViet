package com.example.be_kiotviet.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_shop_role", columnNames = {"shop_id", "name"})
        },
        indexes = {
                @Index(name = "idx_shop_id", columnList = "shop_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    // Nếu bạn muốn quan hệ thật với Shop (khuyến khích để dễ query)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "roles_ibfk_1"))
    private Shops shop;

    @Column(name = "name", length = 100, nullable = false)
    private String name; // Quản lý, Thu ngân, Thủ kho, Nhân viên bán hàng...

    @Column(name = "description", length = 255)
    private String description;

    /**
     * Lưu danh sách permission dưới dạng JSON array string
     * Ví dụ: ["product:create", "product:read", "order:delete", "report:view"]
     * Dùng Set<String> + Hibernate Types hoặc converter đều được
     */
    @ManyToMany(fetch = FetchType.EAGER) // hoặc LAZY tùy bạn
    @JoinTable(
            name = "permission_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    // Cách 1: Dùng Hibernate Types (khuyên dùng - sạch nhất)
    // Thêm dependency: implementation 'com.vladmihalcea:hibernate-types-52:2.21.1'
    // @Type(type = "json")
    // private Set<String> permissions;

    // Cách 2: Dùng JsonSetConverter (tự viết - nhẹ hơn)
    // @Convert(converter = JsonSetConverter.class)

    @Column(name = "is_system_role", nullable = false)
    @ColumnDefault("0")
    private boolean systemRole = false; // true = không cho xóa/sửa (ADMIN, OWNER...)

    @Column(name = "created_at", updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at")
    @ColumnDefault("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant updatedAt;

    // Tự động set thời gian
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                ", systemRole=" + systemRole +
                '}';
    }
}