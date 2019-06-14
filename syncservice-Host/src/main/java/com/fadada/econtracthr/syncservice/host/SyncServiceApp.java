package com.fadada.econtracthr.syncservice.host;

import com.fadada.econtracthr.syncservice.host.util.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by songjt on 2018/01/01.
 */
@EnableTransactionManagement
@EnableFeignClients("com.fadada.econtracthr.syncservice.api")
@SpringBootApplication(excludeName = "*Service",
        scanBasePackages = {"com.fadada.econtracthr.syncservice"})
@MapperScan("com.fadada.econtracthr.syncservice.host.dao")
@EnableDiscoveryClient
public class SyncServiceApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(SyncServiceApp.class, args);
        SpringContextUtil.setApplicationContext(app);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SyncServiceApp.class);
    }
}
