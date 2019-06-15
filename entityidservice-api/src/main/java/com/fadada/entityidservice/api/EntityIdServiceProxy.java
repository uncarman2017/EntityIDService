package com.fadada.entityidservice.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 实体id服务接口
 * @author songjiangtao
 */
@FeignClient("common-entityid-service")
@RequestMapping("/api")
public interface EntityIdServiceProxy {
    /**
     * 获得一个实体id
     * @param idCode id类型
     * @return 返回id
     */
    @GetMapping("/entityId/{idCode}")
    String getEntityId(@PathVariable("idCode") String idCode);

    /**
     * 获得一批实体id
     * @param idCode Id类型
     * @param reqCount 需求数量
     * @return id列表
     */
    @GetMapping("/entityIds/{idCode}/{reqCount}")
    List<String> getEntityIds(@PathVariable("idCode") String idCode, @PathVariable("reqCount") Integer reqCount);

    /**
     * 获得一个带业务编码的实体id
     * @param idCode
     * @param bizCode
     * @return id
     */
    @GetMapping("/entityId/{idCode}/{bizCode}")
    String getEntityId(@PathVariable("idCode") String idCode, @PathVariable("bizCode") String bizCode);

    /**
     * 获得一批带业务编码的实体id
     * @param idCode
     * @param bizCode
     * @param reqCount
     * @return Id列表
     */
    @GetMapping("/entityIds/{idCode}/{bizCode}/{reqCount}")
    List<String> getEntityIds(@PathVariable("idCode") String idCode, @PathVariable("bizCode") String bizCode, @PathVariable("reqCount") Integer reqCount);
}
