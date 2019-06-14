package com.fadada.econtracthr.syncservice.host.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ciicsh.gto.entityidservice.business.EntityIdConfService;
import com.ciicsh.gto.entityidservice.business.EntityIdService;
import com.ciicsh.gto.entityidservice.entity.po.EntityIdConfPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/page")
public class PageControler {
    @Autowired
    private EntityIdConfService entityIdConfService;

    @RequestMapping("/offset")
    public String index(String idCode,Model model) {
        Wrapper<EntityIdConfPO> tWrapper = new EntityWrapper<>();
        idCode = ((idCode == null)?"":idCode.trim());
        tWrapper.where("id_code like '%" + idCode + "%' and is_valid = 1");
        List<EntityIdConfPO> entityIdConfPOS = entityIdConfService.selectList(tWrapper);
        model.addAttribute("entityIdConfPOS",entityIdConfPOS);
        return "offset";
    }
}