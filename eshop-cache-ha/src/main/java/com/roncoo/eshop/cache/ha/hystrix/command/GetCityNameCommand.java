package com.roncoo.eshop.cache.ha.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.roncoo.eshop.cache.ha.cache.LocationCache;

/**
 * 信号量隔离技术
 * @Author: yangyl
 * @Date: 2019/11/2 18:42
 */
public class GetCityNameCommand extends HystrixCommand<String>{
    private Long cityId;

    public GetCityNameCommand(Long cityId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetCityNameCommandGroup"))
                     .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE))
        );
        this.cityId = cityId;
    }

    /**
     * 默认是10个信号量
     * @return
     * @throws Exception
     */
    @Override
    protected String run() throws Exception {
        return LocationCache.getCityName(cityId);
    }
}
