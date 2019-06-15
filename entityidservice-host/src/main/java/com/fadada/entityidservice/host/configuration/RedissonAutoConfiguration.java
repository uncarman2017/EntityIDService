package com.fadada.entityidservice.host.configuration;

import com.fadada.entityidservice.host.util.RedissLockUtil;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author libo
 * @ClassName RedissonAutoConfiguration
 * @date 2018/11/16 17:25
 */
@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    @Autowired
    private RedissonProperties redissonProperties;

    /***
     * @Author libo
     * @Description //单机模式自动装配
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "redisson.address")
    RedissonClient redissonSingle(){
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redissonProperties.getAddress())
                .setTimeout(redissonProperties.getTimeout())
                .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
        if(StringUtils.isNotBlank(redissonProperties.getPassword())) {
            serverConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 装配locker类，并将实例注入到RedissonLockUtil中
     * @return
     */
    @Bean
    RedissLockUtil redissLockUtil(@Qualifier("redissonSingle") RedissonClient redissonClient){
        RedissLockUtil redissLockUtil = new RedissLockUtil();
        redissLockUtil.setRedissonClient(redissonClient);
        return  redissLockUtil;
    }


}
