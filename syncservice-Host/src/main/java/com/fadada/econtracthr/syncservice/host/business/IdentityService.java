package com.fadada.econtracthr.syncservice.host.business;

import com.fadada.econtracthr.syncservice.host.entity.EntityIdConfPO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;

public interface IdentityService {
    List<EntityIdConfPO> selectList();

    String getCurrenDate();

    void sync2Redis() throws JsonProcessingException;

    /* 从redis更新到db*/
    @Scheduled(cron = "0 */5 * * * ?")
    void sync2db() throws IOException;

    /* db定时更新到redis*/
    @Scheduled(cron = "0 56 23 ? * *")//   每天上午23:58触发
    void resetStartNum4DateChange() throws JsonProcessingException;
}
