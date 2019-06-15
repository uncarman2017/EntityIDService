package com.fadada.entityidservice.host.business;


import com.baomidou.mybatisplus.service.IService;
import com.fadada.entityidservice.host.entity.EntityIdConfPO;

/**
 * 实体id管理服务类<></>
 * <p>基于这里spring的bean是单例的<br>
 * 避免EntityHandler再加入静态锁。
 * @author songjiangtao
 */
public interface EntityIdConfService extends IService<EntityIdConfPO> {

}
