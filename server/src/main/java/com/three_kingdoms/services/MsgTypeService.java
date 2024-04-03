package com.three_kingdoms.services;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.MsgType;

import java.util.List;

public interface MsgTypeService {
//    查询所有的类型
    Result<List<MsgType>> findAllType();

//    增加类型
    Result<Integer> saveType(MsgType msgType);

//    删除类型
    Result<Integer> deleteType(Long tid);

//    删除更多
    Result<Integer> deleteMoreType(List<Long> tidList);

//    修改类型
    Result<Integer> updateType(MsgType msgType);
}
