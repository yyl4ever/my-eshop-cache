package com.roncoo.eshop.cache.ha.hystrix.command;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.roncoo.eshop.cache.ha.model.ProductInfo;
import lombok.Getter;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 批量获取多个商品数据
 * <p>
 *     线程池隔离技术.
 *     Observable:可被观察的，可被感知的
 * </p>
 * @Author: yangyl
 * @Date: 2019/11/2 16:57
 */
public class GetProductInfosCommand extends HystrixObservableCommand{
    // 连同包名一起引入
    @lombok.Setter
    @Getter
    private String[] productIds;
    
    public GetProductInfosCommand(String[] productIds) {
        super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoCommandGroup"));
        this.productIds = productIds;
    }
    @Override
    protected Observable construct() {
        System.out.println(Thread.currentThread().getName() + "is running...");
        return Observable.create(new Observable.OnSubscribe<ProductInfo>() {
            @Override
            public void call(Subscriber<? super ProductInfo> subscriber) {
                try {
                    // yangyl:订阅
                    if (!subscriber.isUnsubscribed()) {
                        System.out.println(Thread.currentThread().getName()+"***********");
                        // TODO 断点调试时为何抛time-out异常
                        for (String productId : productIds) {
                            String url = "http://localhost:8082/getProductInfo?productId=" + productId;;
                            String response = HttpUtil.get(url);
                            ProductInfo productInfo = JSON.parseObject(response, ProductInfo.class);
                            // 提供用户的回调方法
                            subscriber.onNext(productInfo);
                        }
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * fallback 的写法，当调用出现异常，会调用该降级方法
     * @return
     * 测试方法：让商品服务宕掉
     */
    @Override
    protected Observable resumeWithFallback() {
        return Observable.create(new Observable.OnSubscribe<ProductInfo>() {
            @Override
            public void call(Subscriber<? super ProductInfo> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        ProductInfo p = new ProductInfo();
                        p.setName("某某");
                        p.setCityId(1L);
                        subscriber.onNext(p);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
