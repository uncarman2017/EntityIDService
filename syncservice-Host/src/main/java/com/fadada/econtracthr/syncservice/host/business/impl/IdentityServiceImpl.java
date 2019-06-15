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
    //String currentDate = "20190614";
    Date currentDate = null;

    {
        currentDate = new Date();
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
    public void loadOnStart() throws JsonProcessingException {
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
    /* db定时更新到redis*/
    @Override
    @Scheduled(cron = "0 58 23 ? * *")//   每天上午23:58触发
    public void sync4DateChange() throws JsonProcessingException {
        RLock lock = RedissLockUtil.getLock("lock");
        lock.lock();

        Date newDate = new Date();
        Long difSecond = (newDate.getTime() - currentDate.getTime());
        if (difSecond > (24*60*(60-1)*100)) {
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DAY_OF_MONTH, 1);
            //更新时间
            Date tempDate = c.getTime();

            Integer currentYear = currentDate.getYear();
            Integer currentMonth = currentDate.getMonth();
            Integer currentDay = currentDate.getDay();

            Integer tempYear = tempDate.getYear();
            Integer tempMonth = tempDate.getMonth();
            Integer tempDay = tempDate.getDay();

            Integer changedFlag = 0;
            changedFlag = (tempYear == currentYear) ? 0 : 1;
            changedFlag = (tempMonth == currentMonth) ? changedFlag : 2;
            changedFlag = (tempDay == currentDay) ? changedFlag : 3;

            List<EntityIdConfPO> pos = selectList();
            for (EntityIdConfPO po : pos) {
                if (changedFlag == 1) {
                    if (po.getDatePattern().equalsIgnoreCase("yy")
                            || po.getDatePattern().equalsIgnoreCase("yyMM")
                            || po.getDatePattern().equalsIgnoreCase("yyMMdd")) {
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
                    if (po.getDatePattern().equalsIgnoreCase("yyMMdd")) {
                        po.setNextBatchStartValue(1L);//这里暂时不用步长
                        entityIdConfMapper.updateById(po);
                    }
                }
            }
            currentDate = tempDate;
            loadOnStart();
        }

        lock.unlock();
//        //String hourTime,dayTime,monthTime,yearTime;
//        Calendar c2 = Calendar.getInstance();
//        c2.set( year, month, day, 0, 0, 0);
//        Date newDate =c2.getTime();
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

}
