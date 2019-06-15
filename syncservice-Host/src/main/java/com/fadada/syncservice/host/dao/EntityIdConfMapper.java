package com.fadada.syncservice.host.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fadada.syncservice.host.entity.EntityIdConfPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 宋江涛
 * @since 2018-01-03
 */
@Mapper
@Repository
public interface EntityIdConfMapper extends BaseMapper<EntityIdConfPO> {

    @Select("select * from entity_id_conf")
    List<EntityIdConfPO> selectList();
}
