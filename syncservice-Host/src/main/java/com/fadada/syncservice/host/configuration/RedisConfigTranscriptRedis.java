package com.fadada.syncservice.host.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;

@Configuration
public class RedisConfigTranscriptRedis {
    @Value("${spring.redis.transcriptredis.host}")
    private String host;
    @Value("${spring.redis.transcriptredis.port}")
    private int port;
    @Value("${spring.redis.transcriptredis.timeout}")
    private int timeout;
    @Value("${spring.redis.transcriptredis.password}")
    private String passowrd;

    @Bean
    public RedisConnectionFactory transcriptRedisRedisConnectionFactory() {
        return createJedisConnectionFactory(host, port, passowrd, timeout);

    }

    /**
     * 创建redis连接工厂
     *
     * @param host
     * @param port
     * @param password
     * @param timeout
     * @return
     */
    public JedisConnectionFactory createJedisConnectionFactory(String host, int port, String password, int timeout) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPassword(password);
        factory.setTimeout(timeout); // 设置连接超时时间
        return factory;

    }

    @Bean
    public KeyGenerator transcriptkeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    @Bean(name = "transcriptRedisTemplate")
    public RedisTemplate<String, String> redisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(transcriptRedisRedisConnectionFactory());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
