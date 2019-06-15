package com.fadada.econtracthr.syncservice.host.controller;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.fadada.econtracthr.syncservice.api.SyncServiceProxy;
import com.fadada.econtracthr.syncservice.host.business.IdentityService;
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
public class IdentityController implements SyncServiceProxy {
    @Autowired
    private IdentityService identityService;

    /**
     * 获得实体编码的时间戳
     *
     * @return 返回年月日：yyyyMMdd
     */
    @GetMapping("/test")
    public void test() throws JsonProcessingException {
        identityService.loadOnStart();
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

//
//    @RequestMapping("/offset")
//    public String index(String idCode,Model model) {
//        Wrapper<EntityIdConfPO> tWrapper = new EntityWrapper<>();
//        idCode = ((idCode == null)?"":idCode.trim());
//        tWrapper.where("id_code like '%" + idCode + "%' and is_valid = 1");
//        List<EntityIdConfPO> entityIdConfPOS = entityIdConfService.selectList(tWrapper);
//        model.addAttribute("entityIdConfPOS",entityIdConfPOS);
//        return "offset";
//    }
}