package com.example.be_kiotviet.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop_suppliers",
        indexes = {
                @Index(name = "idx_shop_supplier", columnList = "shop_id, supplier_id", unique = true),
                @Index(name = "idx_shop_id", columnList = "shop_id"),
                @Index(name = "idx_supplier_id", columnList = "supplier_id")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "supplier_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class ShopSupplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shops shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    /**
     * Giá nhập hiện tại từ nhà cung cấp này (có thể thay đổi theo thời gian)
     */
    @Column(name = "current_cost_price", precision = 15, scale = 2)
    private BigDecimal currentCostPrice;

    /**
     * Công nợ hiện tại (nợ phải trả cho nhà cung cấp)
     * > 0: còn nợ
     * = 0: hòa
     * < 0: NCC nợ lại shop (trả hàng, hoàn tiền...)
     */
    @Column(name = "debt_balance", precision = 15, scale = 2)
    private BigDecimal debtBalance = BigDecimal.ZERO;

    /**
     * Hạn thanh toán (số ngày được nợ)
     * Ví dụ: 30 ngày, 45 ngày...
     */
    @Column(name = "payment_term_days")
    private Integer paymentTermDays = 30;

    /**
     * Ghi chú riêng cho quan hệ shop-supplier này
     */
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    /**
     * Trạng thái quan hệ
     * ACTIVE, INACTIVE, SUSPENDED...
     */
    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    /**
     * Ngày bắt đầu hợp tác
     */
    @Column(name = "cooperation_start_date", columnDefinition = "DATETIME NULL")
    private LocalDateTime cooperationStartDate;

    /**
     * Thời gian tạo
     */
    @Column(name = "created_at", columnDefinition = "DATETIME NULL")
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
}