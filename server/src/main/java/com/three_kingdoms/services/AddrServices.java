package com.three_kingdoms.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.Addr;

import java.util.List;

public interface AddrServices {
    //分页查询所有地点
    IPage<Addr> findAllToPage(Page<Addr> page);

    //直接获取所有地点
    Result<List<Addr>> findAll();

    //按州查询所有郡
    List<Addr> findAllCountryByState(String state);

    //查询所有的州
    Result<List<Addr>> findAllState();

    //以级联的方式返回
    Result<List<Addr>> findAllStateToCascade();

    //按id查询单个地点的详细信息
    Addr findById(Long addrId);

    //根据名称查询单个地点的详细信息
    Result<Addr> findByName(String name);

    //增加地点
    Integer save(Addr addr);

    //修改地点
    Integer update(Addr addr);

    //删除地点
    Integer delete(Long addrId);

    //删除多个地点
    Result<Integer> deleteMore(List<Long> addrIds);
}
