package com.fadada.econtracthr.syncservice.host.business.impl;

import com.fadada.econtracthr.syncservice.host.business.IdentityService;
import com.fadada.econtracthr.syncservice.host.dao.EntityIdConfMapper;
import com.fadada.econtracthr.syncservice.host.entity.EntityIdConfPO;
import com.fadada.econtracthr.syncservice.host.util.RedissLockUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class IdentityServiceImpl implements IdentityService {
    private Date currentDate = null;
    {   Date tempDate = new Date();
        System.out.println(tempDate.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tempDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        currentDate = calendar.getTime();
        System.out.println(currentDate.toString());
    }
    @Autowired
    RedissLockUtil redissLockUtil;
    @Autowired
    EntityIdConfMapper entityIdConfMapper;
    @Autowired
    @Qualifier("transcriptRedisTemplate")
    RedisTemplate transStringRedisTemplate;
    @Autowired
    @Qualifier("counterRedisTemplate")
    RedisTemplate counterStringRedisTemplate;
    @Override
    public List<EntityIdConfPO> selectList() {
        transStringRedisTemplate.opsForValue().set("dd","aa");
        return entityIdConfMapper.selectList();
    }

    @Override
    public String getCurrenDate() {
        Format f = new SimpleDateFormat("yyyyMMdd");
        return f.format(currentDate);
    }

    @Override
    public void sync2Redis() throws JsonProcessingException {
        RLock lock = RedissLockUtil.getLock("lock");
        lock.lock();

        List<EntityIdConfPO> pos = entityIdConfMapper.selectList();
        for(EntityIdConfPO po: pos){
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(po);
            transStringRedisTemplate.opsForValue().set(po.getIdCode(),json);
        }

        lock.unlock();
    }
    /* 从redis更新到db*/
    @Override
    @Scheduled(cron = "0 */5 * * * ?")
    public void sync2db() throws IOException {
        List<EntityIdConfPO> pos = selectList();
        for (EntityIdConfPO po : pos) {
            String json = (String)transStringRedisTemplate.opsForValue().get(po.getIdCode());// todo
            ObjectMapper mapper = new ObjectMapper();
            EntityIdConfPO udPO = mapper.readValue(json, EntityIdConfPO.class);
            entityIdConfMapper.updateById(udPO);
        }
    }
    /* db定时更新到redis*/
    @Override
    @Scheduled(cron = "0 56 23 ? * *")//   每天上午23:58触发
    public void resetStartNum4DateChange() throws JsonProcessingException {
        RLock lock = RedissLockUtil.getLock("lock");
        lock.lock();

        Date newDate = new Date();
        Long difSecond = (newDate.getTime() - currentDate.getTime());
        if ( difSecond > ((24*60-2)*60)*1000) {
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DAY_OF_MONTH, 1);
            //更新时间
            Date tempDate = c.getTime();
            Integer changedFlag = 1;
            changedFlag = (tempDate.getMonth() == currentDate.getMonth()) ? changedFlag : 2;
            changedFlag = (tempDate.getYear() == currentDate.getYear()) ? changedFlag : 3;

            List<EntityIdConfPO> pos = selectList();
            for (EntityIdConfPO po : pos) {
                if (changedFlag == 1) {
                    if (po.getDatePattern().equalsIgnoreCase("yyMMdd")) {
                        po.setNextBatchStartValue(1L);//这里暂时不用步长
                        entityIdConfMapper.updateById(po);
                    }
                } else if (changedFlag == 2) {
                    if (po.getDatePattern().equalsIgnoreCase("yyMM")
                            || po.getDatePattern().equalsIgnoreCase("yyMMdd")) {
                        po.setNextBatchStartValue(1L);//这里暂时不用步长
                        entityIdConfMapper.updateById(po);
                    }
                } else if (changedFlag == 3) {
                    if (po.getDatePattern().equalsIgnoreCase("yy")
                            || po.getDatePattern().equalsIgnoreCase("yyMM")
                            || po.getDatePattern().equalsIgnoreCase("yyMMdd")) {
                        po.setNextBatchStartValue(1L);//这里暂时不用步长
                        entityIdConfMapper.updateById(po);
                    }
                }
            }
            currentDate = tempDate;
            sync2Redis();
        }
        lock.unlock();
    }
}
