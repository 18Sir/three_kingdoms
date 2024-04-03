package com.three_kingdoms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.three_kingdoms.domain.MsgType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MsgTypeDao extends BaseMapper<MsgType> {
}
