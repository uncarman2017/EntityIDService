package com.fadada.econtracthr.syncservice.host.business;

import com.fadada.econtracthr.syncservice.host.entity.EntityIdConfPO;

import java.util.List;

public interface IdentityService {
    List<EntityIdConfPO> selectList();

    String getCurrenDate();
}
