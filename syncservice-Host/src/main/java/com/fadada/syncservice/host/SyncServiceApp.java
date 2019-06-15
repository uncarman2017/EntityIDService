package com.fadada.syncservice.host;

import com.fadada.syncservice.host.listener.ApplicationStartup;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by songjt on 2018/01/01.
 */
@EnableTransactionManagement
//@EnableFeignClients("com.fadada.syncservice.api")
@SpringBootApplication(excludeName = "*Service",
        scanBasePackages = {"com.fadada.syncservice"})
@MapperScan("com.fadada.syncservice.host.dao")
@EnableDiscoveryClient
@EnableScheduling
public class SyncServiceApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SyncServiceApp.class);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
//        ApplicationContext app = SpringApplication.run(SyncServiceApp.class, args);
//        SpringContextUtil.setApplicationContext(app);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SyncServiceApp.class);
    }
}
