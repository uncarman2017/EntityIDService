package com.fadada.entityidservice.host.business;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 宋江涛
 * @since 2018-01-04
 */
public interface EntityIdService {

    List<String> getNextEntityIds(String idCode, String bizCode, Integer reqCount);

    String getNextEntityId(String idCode, String bizCode);

    void offsetEntityId(String idCode, Integer offsetValue);
}
