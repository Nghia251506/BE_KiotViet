package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Lấy tất cả category của shop, sắp xếp theo sortOrder để build tree dễ dàng
    List<Category> findByShopIdOrderBySortOrderAsc(Long shopId);

    // Lấy phân trang danh sách (dùng cho list flat)
    Page<Category> findByShopId(Long shopId, Pageable pageable);

    // Tìm kiếm theo tên (ignore case)
    Page<Category> findByShopIdAndNameContainingIgnoreCase(
            Long shopId,
            String name,
            Pageable pageable);

    // Tìm category theo id và shopId (bảo mật multi-tenant)
    Optional<Category> findByIdAndShopId(Long id, Long shopId);

    // Kiểm tra trùng tên trong cùng parent và shop (dùng khi create/update)
    boolean existsByNameAndShopIdAndParent(String name, Long shopId, Category parent);

    boolean existsByNameAndShopIdAndParentIsNull(String name, Long shopId);
}