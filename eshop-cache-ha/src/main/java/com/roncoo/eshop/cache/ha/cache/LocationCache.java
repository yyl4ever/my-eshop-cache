package com.roncoo.eshop.cache.ha.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yangyl
 * @Date: 2019/11/2 18:46
 */
public class LocationCache {
    private static Map<Long, String> cityMap = new HashMap<>();
    static {
        cityMap.put(1L, "北京");
    }
    public static String getCityName(Long cityId) {
        return cityMap.get(cityId);
    }
}
