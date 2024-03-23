package com.three_kingdoms.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Addr;

import java.util.List;

public interface AddrServices {
    //查询所有地点
    public IPage<Addr> findAll(Page page);

    //按州查询所有郡
    public List<Addr> findAllByState(String state);

    //按id查询单个地点的详细信息
    public Addr findById(Long addrId);

    //增加地点
    public Integer save(Addr addr);

    //修改地点
    public Integer update(Addr addr);

    //删除地点
    public Integer delete(Long addrId);
}
