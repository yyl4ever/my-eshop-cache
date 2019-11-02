package com.roncoo.eshop.cache.ha.hystrix.command;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.roncoo.eshop.cache.ha.model.ProductInfo;

/**
 * 获取单个商品信息
 * <p>
 *     线程池隔离技术
 * </p>
 * @Author: yangyl
 * @Date: 2019/11/2 15:09
 */
public class GetProductInfoCommand extends HystrixCommand<ProductInfo>{
    
    private Long productId;

    public GetProductInfoCommand(Long productId) {
        // 对应一个线程池
        super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoCommandGroup"));
        this.productId = productId;
    }
    
    @Override
    protected ProductInfo run() throws Exception {
        System.out.println("调用接口查询商品数据，productId=" + productId);
        String url = "http://localhost:8082/getProductInfo?prodcutId=" + productId;
        String response = HttpUtil.get(url);
        return JSON.parseObject(response, ProductInfo.class);
    }
}
