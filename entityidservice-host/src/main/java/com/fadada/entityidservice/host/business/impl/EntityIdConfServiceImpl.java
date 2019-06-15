package com.fadada.entityidservice.host.business.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fadada.entityidservice.host.business.EntityIdConfService;
import com.fadada.entityidservice.host.dao.EntityIdConfMapper;
import com.fadada.entityidservice.host.entity.EntityIdConfPO;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 宋江涛
 * @since 2018-01-03
 */
@Service
public class EntityIdConfServiceImpl extends ServiceImpl<EntityIdConfMapper, EntityIdConfPO> implements EntityIdConfService {

}
