package com.three_kingdoms.services;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.JuBaoMsg;

import java.util.List;

public interface JuBaoMsgService {
    //查询全部举报信息
    Result<List<JuBaoMsg>> findAll();
    //添加举报信息
    Result<String> save(String token, JuBaoMsg juBaoMsg);
    //修改举报信息
    Result<String> updateById(String token, JuBaoMsg juBaoMsg);
    //删除举报信息
    Result<String> deleteById(String token, Long jid);
    //删除多个举报信息
    Result<Integer> deleteMore(List<Long> jids);
}
