package com.three_kingdoms.services;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.JuBaoComment;

import java.util.List;

public interface JuBaoCommentService {
    //查询全部举报信息
    Result<List<JuBaoComment>> findAll();
    //添加举报信息
    Result<String> save(String token, JuBaoComment juBaoComment);
    //修改举报信息
    Result<String> updateById(String token, JuBaoComment juBaoComment);
    //删除举报信息
    Result<String> deleteById(String token, Long jid);
    //删除多个举报信息
    Result<Integer> deleteMore(List<Long> jids);
}
