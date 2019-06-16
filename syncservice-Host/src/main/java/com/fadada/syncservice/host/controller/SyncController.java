package com.fadada.syncservice.host.controller;


import com.fadada.syncservice.host.business.SyncService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 实体id服务
 *
 * @author songjiangtao
 */
@Api
@RestController
@RequestMapping("/entity")
public class SyncController {
    @Autowired
    private SyncService syncService;

    /**
     * 获得实体编码的时间戳
     *
     * @return 返回年月日：yyyyMMdd
     */
    @GetMapping("/test")
    public void test() throws JsonProcessingException {
        syncService.sync2Redis();
    }
    /**
     * 获得实体编码的时间戳
     *
     * @return 返回年月日：yyyyMMdd
     */
    @GetMapping("/currentDate")
    public String getCurrentDate() throws JsonProcessingException {
        return syncService.getCurrenDate();
    }

}