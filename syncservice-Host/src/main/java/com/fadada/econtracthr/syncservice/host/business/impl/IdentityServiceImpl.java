package com.fadada.econtracthr.syncservice.host.business.impl;

import com.fadada.econtracthr.syncservice.host.business.IdentityService;
import com.fadada.econtracthr.syncservice.host.dao.EntityIdConfMapper;
import com.fadada.econtracthr.syncservice.host.entity.EntityIdConfPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdentityServiceImpl implements IdentityService {
    @Autowired
    EntityIdConfMapper entityIdConfMapper;

    @Autowired
    @Qualifier("redisConfigOfTranscript")
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String getCurrenDate() {
        return "20190614";
    }

    @Override
    public List<EntityIdConfPO> selectList() {
        stringRedisTemplate.opsForValue().set("dd","aa");
        return entityIdConfMapper.selectList();
    }

}
