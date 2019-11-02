package com.roncoo.eshop.cache.ha.controller;

import com.netflix.hystrix.HystrixCommand;
import com.roncoo.eshop.cache.ha.hystrix.command.GetProductInfoCommand;
import com.roncoo.eshop.cache.ha.hystrix.command.GetProductInfosCommand;
import com.roncoo.eshop.cache.ha.model.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangyl
 * @Date: 2019/11/2 16:38
 */
@RestController
@Slf4j
public class CacheController {
    
    /**
     * 模拟接收到 mq 变更消息，调用查询商品服务，获取最新商品信息
     * @param productId
     * @return
     */
    @RequestMapping("/change/product")
    public String changeProduct(Long productId){
        return null;
    }

    /**
     * 模拟调用商品服务的查询接口
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInfo")
    public ProductInfo getProductInfo(Long productId) {
        HystrixCommand<ProductInfo> getProductInfoCommand = new GetProductInfoCommand(productId);

        ProductInfo productInfo = getProductInfoCommand.execute();
        System.out.println(productInfo);
        return productInfo;
    }

    /**
     * 模拟调用商品服务的批量查询接口
     * @param productIds
     * @return
     */
    @RequestMapping("/getProductInfos")
    public String getProductInfos(String productIds) {
        List<ProductInfo> list = new ArrayList<>();

        GetProductInfosCommand observableCommand = new GetProductInfosCommand(productIds.split(","));
        Observable observe = observableCommand.observe();
        
        observe.subscribe(new Observer<ProductInfo>() {
            @Override
            public void onCompleted() {
                System.out.println(Thread.currentThread().getName()+"聚合完了所有的查询请求");
                System.out.println(list);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onNext(ProductInfo productInfo) {
                System.out.println(Thread.currentThread().getName()+"-----");
                list.add(productInfo);
            }
        });
        return "success";
    }
}
