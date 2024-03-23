package com.three_kingdoms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.three_kingdoms.domain.Power;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Powerdao extends BaseMapper<Power> {
}
