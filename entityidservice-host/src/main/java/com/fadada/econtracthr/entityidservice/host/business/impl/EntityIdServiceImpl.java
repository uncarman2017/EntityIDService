package com.fadada.econtracthr.entityidservice.host.business.impl;

import com.fadada.econtracthr.entityidservice.host.business.EntityIdConfService;
import com.fadada.econtracthr.entityidservice.host.business.EntityIdHandler;
import com.fadada.econtracthr.entityidservice.host.business.EntityIdService;
import com.fadada.econtracthr.entityidservice.host.entity.EntityIdConfPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实体id管理服务类<></>
 * <p>基于这里spring的bean是单例的<br>
 * 避免EntityHandler再加入静态锁。
 *
 * @author songjiangtao
 */
@Service
public class EntityIdServiceImpl implements EntityIdService {
    private final ReentrantLock lock = new ReentrantLock();
    private static Map<String, EntityIdHandler> entityIdHandlers = new HashMap<>(20);

    @Autowired
    private EntityIdConfService entityIdConfService;


    private EntityIdHandler getEntityHandler(String idCode) {
        // 注册到hashmap中。提高效率
        lock.lock();
        try {
            if (!entityIdHandlers.containsKey(idCode)) {
                //每个type只会在注册时执行一次，所以不影响性能。
                if (entityIdConfService.selectById(idCode) != null) {
                    EntityIdHandler entityHandler = new EntityIdHandler(idCode, entityIdConfService);
                    entityIdHandlers.put(idCode, entityHandler);
//                    logServiceProxy.infoAsync(LogDTO.of().setTitle("获取实体id")
//                            .setContent(idCode + "处理器注册")
//                            .putTag("thread", Thread.currentThread().getName()));
                } else {
//                    logServiceProxy.infoAsync(LogDTO.of().setTitle("获取实体id")
//                            .setContent("注册失败，不正确的idCode：" + idCode)
//                            .putTag("thread", Thread.currentThread().getName()));
                    return null;
                }
            }
            return entityIdHandlers.get(idCode);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, timeout = 36000, rollbackFor = Exception.class)
    public String getNextEntityId(String idCode, String bizCode) {
        EntityIdHandler entityIdHandler = null;
        entityIdHandler = getEntityHandler(idCode);
        if (entityIdHandler != null) {
            return getEntityHandler(idCode).getNextEntityId(bizCode.trim());
        } else {
            return "-1";
        }
    }

    @Override
    public List<String> getNextEntityIds(String idCode, String bizCode, Integer reqCount) {
        EntityIdHandler entityIdHandler = null;
        if (reqCount <= 50) {
            entityIdHandler = getEntityHandler(idCode);
            if (entityIdHandler != null) {
                return entityIdHandler.getNextEntityIds(bizCode.trim(), reqCount);
            } else {
                return new ArrayList<String>();
            }
        } else {
            return new ArrayList<String>();
        }
    }

    @Override
    public void offsetEntityId(String idCode, Integer offsetValue) {
        if (entityIdHandlers.containsKey(idCode)) {
            entityIdHandlers.remove(idCode);
        }
        EntityIdConfPO entityIdConf = entityIdConfService.selectById(idCode);
        entityIdConf.setNextBatchStartValue(entityIdConf.getNextBatchStartValue() + offsetValue);
        entityIdConfService.updateById(entityIdConf);
    }
}
