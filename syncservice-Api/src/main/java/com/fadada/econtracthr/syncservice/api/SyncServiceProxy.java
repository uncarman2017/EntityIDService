package com.fadada.econtracthr.syncservice.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 实体id服务接口
 * @author songjiangtao
 */
@FeignClient("entityid-sync-service")
@RequestMapping("/api")
public interface SyncServiceProxy {
    /**
     * 获得实体编码的时间戳
     * @return 返回年月日：yyyyMMdd
     */
    @GetMapping("/sync/date")
    String getEntityDate();
}