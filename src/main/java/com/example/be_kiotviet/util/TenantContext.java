package com.example.be_kiotviet.util;

public class TenantContext {

    // ThreadLocal để lưu shopId riêng biệt cho từng request (thread)
    private static final ThreadLocal<Long> currentShopId = new ThreadLocal<>();

    public static void setCurrentShopId(Long shopId) {
        currentShopId.set(shopId);
    }

    public static Long getCurrentShopId() {
        Long shopId = currentShopId.get();
        if (shopId == null) {
            throw new IllegalStateException("ShopId không tồn tại trong context. Có thể request chưa qua JWT filter");
        }
        return shopId;
    }

    public static void clear() {
        currentShopId.remove();
    }
}