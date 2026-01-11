package com.example.be_kiotviet.service.Category;

import com.example.be_kiotviet.dto.category.CategoryCreateDto;
import com.example.be_kiotviet.dto.category.CategoryResponseDto;
import com.example.be_kiotviet.dto.category.CategoryUpdateDto;
import com.example.be_kiotviet.entity.Category;
import com.example.be_kiotviet.mapper.CategoryMapper;
import com.example.be_kiotviet.repository.CategoryRepository;
import com.example.be_kiotviet.service.Category.CategoryService;
import com.example.be_kiotviet.util.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private Long getCurrentShopId() {
        return TenantContext.getCurrentShopId();
    }

    @Override
    public List<CategoryResponseDto> getCategoryTree() {
        Long shopId = getCurrentShopId();
        List<Category> categories = categoryRepository.findByShopIdOrderBySortOrderAsc(shopId);

        Map<Long, CategoryResponseDto> dtoMap = new HashMap<>();
        List<CategoryResponseDto> roots = new ArrayList<>();

        for (Category entity : categories) {
            CategoryResponseDto dto = CategoryMapper.toResponseDto(entity);
            dtoMap.put(entity.getId(), dto);

            if (entity.getParent() == null) {
                roots.add(dto);
            } else {
                CategoryResponseDto parentDto = dtoMap.get(entity.getParent().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            }
        }

        sortChildrenRecursively(roots);
        return roots;
    }

    private void sortChildrenRecursively(List<CategoryResponseDto> nodes) {
        if (nodes == null || nodes.isEmpty()) return;

        nodes.sort(Comparator.comparing(
                CategoryResponseDto::getSortOrder,
                Comparator.nullsLast(Integer::compareTo)));

        nodes.forEach(node -> sortChildrenRecursively(node.getChildren()));
    }

    @Override
    public Page<CategoryResponseDto> getCategoryList(String search, Pageable pageable) {
        Long shopId = getCurrentShopId();
        Page<Category> page;

        if (search == null || search.trim().isEmpty()) {
            page = categoryRepository.findByShopId(shopId, pageable);
        } else {
            page = categoryRepository.findByShopIdAndNameContainingIgnoreCase(shopId, search.trim(), pageable);
        }

        return page.map(CategoryMapper::toResponseDto);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = findByIdAndShop(id);
        return CategoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateDto dto) {
        Long shopId = getCurrentShopId();

        validateNameNotDuplicate(dto.getName(), dto.getParentId(), shopId);

        Category entity = new Category();
        entity.setShopId(shopId);
        entity.setName(dto.getName().trim());
        entity.setDescription(dto.getDescription());
        entity.setImageUrl(dto.getImageUrl());
        entity.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);

        if (dto.getParentId() != null) {
            Category parent = findByIdAndShop(dto.getParentId());
            entity.setParent(parent);
        }

        Category saved = categoryRepository.save(entity);
        return CategoryMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryUpdateDto dto) {
        Category entity = findByIdAndShop(id);

        if (dto.getName() != null && !dto.getName().trim().equals(entity.getName())) {
            validateNameNotDuplicate(dto.getName().trim(), entity.getParent() != null ? entity.getParent().getId() : null, entity.getShopId());
            entity.setName(dto.getName().trim());
        }

        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        if (dto.getImageUrl() != null) {
            entity.setImageUrl(dto.getImageUrl());
        }

        if (dto.getSortOrder() != null) {
            entity.setSortOrder(dto.getSortOrder());
        }

        if (dto.getParentId() != null) {
            if (dto.getParentId().equals(id)) {
                throw new IllegalArgumentException("Danh mục không thể là cha của chính nó");
            }
            Category newParent = findByIdAndShop(dto.getParentId());
            entity.setParent(newParent);
        } else if (dto.getParentId() == null && entity.getParent() != null) {
            entity.setParent(null);
        }

        Category updated = categoryRepository.save(entity);
        return CategoryMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = findByIdAndShop(id);

        if (!category.getChildren().isEmpty()) {
            throw new IllegalArgumentException("Không thể xóa danh mục có danh mục con");
        }

        // TODO: Sau này kiểm tra có sản phẩm không
        // if (productRepository.existsByCategoryId(id)) { ... }

        categoryRepository.delete(category);
    }

    // Helper methods
    private Category findByIdAndShop(Long id) {
        Long shopId = getCurrentShopId();
        return categoryRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại hoặc không thuộc shop của bạn"));
    }

    private void validateNameNotDuplicate(String name, Long parentId, Long shopId) {
        boolean exists;
        if (parentId == null) {
            exists = categoryRepository.existsByNameAndShopIdAndParentIsNull(name, shopId);
        } else {
            Category parent = findByIdAndShop(parentId);
            exists = categoryRepository.existsByNameAndShopIdAndParent(name, shopId, parent);
        }

        if (exists) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại trong nhóm này");
        }
    }
}