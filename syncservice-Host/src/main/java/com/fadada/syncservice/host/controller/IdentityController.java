package com.fadada.syncservice.host.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fadada.syncservice.host.business.IdentityService;
import com.fadada.syncservice.host.entity.EntityIdConfPO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 实体id服务
 *
 * @author songjiangtao
 */
@Api
@RestController
@RequestMapping("/identity")
public class IdentityController {
    @Autowired
    private IdentityService identityService;

    /**
     * 获得实体编码的时间戳
     *
     * @return 返回年月日：yyyyMMdd
     */
    @GetMapping("/test")
    public void test() throws JsonProcessingException {
        identityService.sync2Redis();
    }
    /**
     * 获得实体编码的时间戳
     *
     * @return 返回年月日：yyyyMMdd
     */
    @GetMapping("/currentDate")
    public String getCurrentDate() throws JsonProcessingException {
        return identityService.getCurrenDate();
    }

}