package com.fadada.entityidservice.host.controller;


import com.fadada.entityidservice.host.business.EntityIdService;
import com.fadada.entityidservice.host.util.Result;
import com.fadada.entityidservice.host.util.ResultGenerator;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//import com.ciicsh.gto.entityidservice.id.EntityIdManager;


/**
 * 实体id服务
 *
 * @author songjiangtao
 */
@Api
@RestController
@RequestMapping("/old/api")
public class EntityIdController {


    @Value("${app.id}")
    private String appId;

    @Autowired
    EntityIdService entityIdService;

    /**
     * 对实体Id配置进行偏移
     *
     * @param idCode id类型；offsetValue 偏移量
     * @return 返回Result<Boolean>
     */
    @GetMapping("/entityId/{idCode}/offset/{offsetValue}")
    Result<Boolean> offsetEntityId(@PathVariable("idCode") String idCode, @PathVariable("offsetValue") Integer offsetValue) {
        try {
            entityIdService.offsetEntityId(idCode, offsetValue);
            return ResultGenerator.genSuccessResult(true);
        } catch (Exception e) {
//            logServiceProxy.errorAsync(LogDTO.of().setAppId(appId).setContent(e.toString()));
            return ResultGenerator.genClientFailResult("偏移失败，请检查！");
        }
    }

    /**
     * 获得一个实体id
     *
     * @param idCode id类型
     * @return 返回id
     */
    @GetMapping("/entityId/{idCode}")
    public String getEntityId(@PathVariable("idCode") String idCode) {
        return entityIdService.getNextEntityId(idCode, "");
    }

    /**
     * 获得一批实体id
     *
     * @param idCode   Id类型
     * @param reqCount 需求数量
     * @return id列表
     */
    @GetMapping("/entityIds/{idCode}/{reqCount}")
    public List<String> getEntityIds(@PathVariable("idCode") String idCode, @PathVariable("reqCount") Integer reqCount) {
        return entityIdService.getNextEntityIds(idCode, "", reqCount);
    }

    /**
     * 获得一个带业务编码的实体id
     *
     * @param idCode
     * @param bizCode
     * @return id
     */
    @GetMapping("/entityId/{idCode}/{bizCode}")
    public String getEntityId(@PathVariable("idCode") String idCode, @PathVariable("bizCode") String bizCode) {
        return entityIdService.getNextEntityId(idCode, bizCode);
    }

    /**
     * 获得一批带业务编码的实体id
     *
     * @param idCode
     * @param bizCode
     * @param reqCount
     * @return Id列表
     */
    @GetMapping("/entityIds/{idCode}/{bizCode}/{reqCount}")
    public List<String> getEntityIds(@PathVariable("idCode") String idCode, @PathVariable("bizCode") String bizCode, @PathVariable("reqCount") Integer reqCount) {
        return entityIdService.getNextEntityIds(idCode, bizCode, reqCount);
/*
        threadTest();
        return new ArrayList<>();
*/
    }

    private void threadTest() {
        RunnableA ra1 = new RunnableA(entityIdService);
        new Thread(ra1, "线程A1").start();

        RunnableA ra2 = new RunnableA(entityIdService);
        new Thread(ra2, "线程A2").start();

        RunnableA ra3 = new RunnableA(entityIdService);
        new Thread(ra3, "线程A3").start();

        RunnableA ra4 = new RunnableA(entityIdService);
        new Thread(ra4, "线程A4").start();

        RunnableB rb1 = new RunnableB(entityIdService);
        new Thread(rb1, "线程B1").start();

        RunnableB rb2 = new RunnableB(entityIdService);
        new Thread(rb2, "线程B2").start();

        RunnableB rb3 = new RunnableB(entityIdService);
        new Thread(rb3, "线程B3").start();

        RunnableB rb4 = new RunnableB(entityIdService);
        new Thread(rb4, "线程B4").start();
    }
}

class RunnableA implements Runnable {
    private static Log log = LogFactory.getLog(RunnableA.class);
    EntityIdService entityIdService;

    public RunnableA(EntityIdService entityIdService) {
        super();
        this.entityIdService = entityIdService;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            entityIdService.getNextEntityIds("AP", "", 20);
        }
    }
}

class RunnableB implements Runnable {
    private static Log log = LogFactory.getLog(RunnableB.class);
    EntityIdService entityIdService;

    public RunnableB(EntityIdService entityIdService) {
        super();
        this.entityIdService = entityIdService;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            entityIdService.getNextEntityIds("FR", "XXX", 20);
        }
    }
}
