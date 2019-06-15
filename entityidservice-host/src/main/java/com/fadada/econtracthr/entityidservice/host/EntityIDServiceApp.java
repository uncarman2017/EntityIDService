package com.fadada.econtracthr.entityidservice.host;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by songjt on 2018/01/01.
 */
@EnableTransactionManagement
@SpringBootApplication(excludeName = "*Service",
        scanBasePackages = {"com.fadada.econtracthr.entityidservice.host"})
@MapperScan("com.fadada.econtracthr.entityidservice.host.dao")
@EnableConfigurationProperties
@EnableDiscoveryClient
@EnableFeignClients("com.fadada.econtracthr.syncservice.api")
public class EntityIDServiceApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EntityIDServiceApp.class, args);

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EntityIDServiceApp.class);
    }
}
