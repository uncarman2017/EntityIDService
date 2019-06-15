package com.fadada.entityidservice.host.controller;

import com.fadada.syncservice.api.SyncServiceProxy;
import com.fadada.entityidservice.host.entity.EntityIdConfPO;
import com.fadada.entityidservice.host.util.RedissLockUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api
@RestController
@RequestMapping("/api")
public class IdConsumerController {
    public static final String PAD_STR = "0";
    public static final String DEFAULT_CURRENT_DATE_FORMATION = "yyyyMMdd";
    public static final String DEFAULT_FAILURE_STATUS = "-1";
    public static final long DEFAULT_COUNTER_LIMIT = 99999L;
    public static final String FAILURE_MSG = "获取EntityId失败！";

    @Autowired
    @Resource(name = "counterRedisTemplate")
    private RedisTemplate counterRedis;
    @Autowired
    @Resource(name = "transcriptRedisTemplate")
    private RedisTemplate transcriptRedis;
    @Autowired
    private SyncServiceProxy syncServiceProxy;

    @GetMapping("/get/{idCode}")
    public String getNextEntityId(@PathVariable("idCode") String idCode) throws IOException {
        Long increment = counterRedis.opsForValue().increment(idCode);
        if (null == increment || increment >= DEFAULT_COUNTER_LIMIT) {
            return FAILURE_MSG;
        }
        EntityIdConfPO entityIdConfPO = syncTranscriptRedis(idCode, increment);
        String currentDate = syncServiceProxy.getCurrentDate();
        return assembleEntityId(idCode, increment, entityIdConfPO, currentDate);
    }

    private String assembleEntityId(@PathVariable("idCode") String idCode, Long increment, EntityIdConfPO entityIdConfPO, String currentDate) {
        return idCode.trim()
                + translateCurrentDate(currentDate, entityIdConfPO).trim()
                + StringUtils.leftPad(String.valueOf(increment), entityIdConfPO.getNumDigit(), PAD_STR);
    }

    private EntityIdConfPO syncTranscriptRedis(@PathVariable("idCode") String idCode, Long increment) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EntityIdConfPO entityIdConfPO = mapper.readValue(String.valueOf(transcriptRedis.opsForValue().get(idCode)), EntityIdConfPO.class);
        entityIdConfPO.setNextBatchStartValue(increment);
        RLock lock = RedissLockUtil.getLock("lock");
        lock.lock();
        transcriptRedis.opsForValue().set(idCode, mapper.writeValueAsString(entityIdConfPO));
        lock.unlock();
        return entityIdConfPO;
    }

    private String translateCurrentDate(String currentDate, EntityIdConfPO entityIdConfPO) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_CURRENT_DATE_FORMATION);
            formatter.setLenient(false);
            Date newDate = formatter.parse(currentDate);
            formatter = new SimpleDateFormat(entityIdConfPO.getDatePattern());
            return formatter.format(newDate);
        } catch (ParseException e) {
            return DEFAULT_FAILURE_STATUS;
        }
    }

}
