package com.fadada.syncservice.host.listener;

import com.fadada.syncservice.host.business.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        IdentityService service = contextRefreshedEvent.getApplicationContext().getBean(IdentityService.class);
        try {
            service.sysStart();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ;
    }
}
