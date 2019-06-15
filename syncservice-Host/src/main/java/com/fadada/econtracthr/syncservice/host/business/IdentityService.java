package com.fadada.econtracthr.syncservice.host.business;

import com.fadada.econtracthr.syncservice.host.entity.EntityIdConfPO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface IdentityService {
    List<EntityIdConfPO> selectList();

    String getCurrenDate();

    void loadOnStart() throws JsonProcessingException;

    void sync4DateChange() throws JsonProcessingException;

    void sync2db() throws IOException;
}
