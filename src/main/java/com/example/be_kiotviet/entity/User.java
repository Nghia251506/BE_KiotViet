package com.example.be_kiotviet.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_shop_id", columnList = "shop_id"),
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_users_email", columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_id"))
    private Role role;

    @Column(name = "username", length = 255, nullable = false)
    private String username;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password; // đã được hash bằng BCrypt

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "phone", length = 255)
    private String phone;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "status", length = 20)
    private String status; // ACTIVE, INACTIVE, LOCKED...

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "created_at", updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at")
    @ColumnDefault("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant updatedAt;

    @Column(name = "isActive")
    private Boolean isActive = true; // bit(1) → Boolean trong Java

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permission",
            joinColumns = @JoinColumn(name = "user_id"),               // FK tới users.id
            inverseJoinColumns = @JoinColumn(name = "permission_id")   // FK tới permissions.id
    )
    private Set<Permission> permissions = new HashSet<>();

    // Tự động set createdAt và updatedAt
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // toString, equals, hashCode (tùy chọn dùng Lombok @ToString, @EqualsAndHashCode)
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", shopId=" + shopId +
                ", status='" + status + '\'' +
                '}';
    }
}