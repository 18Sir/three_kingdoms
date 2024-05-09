package com.three_kingdoms.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.dao.AddrDao;
import com.three_kingdoms.dao.EventDao;
import com.three_kingdoms.domain.Addr;
import com.three_kingdoms.domain.Event;
import com.three_kingdoms.services.AddrServices;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddrServicesImpl implements AddrServices {
    @Resource
    private AddrDao addrDao;

    @Resource
    private EventDao eventDao;

    @Override
    public IPage<Addr> findAllToPage(Page<Addr> p) {
        IPage<Addr> page = new Page<>(p.getCurrent(), p.getSize());
        return addrDao.selectPage(page, null);
    }

    @Override
    public Result<List<Addr>> findAll() {
        List<Addr> addrList = addrDao.selectList(null);
        for (Addr addr : addrList) {
            addr.setEventList(getAddrEventList(addr.getEids()));
        }
        return Result.selectSuccess(addrList);
    }

    @Override
    public Addr findById(Long addrId) {
        Addr addr = addrDao.selectById(addrId);
        if (addr != null) {
            addr.setEventList(getAddrEventList(addr.getEids()));
        }
        return addr;
    }

    //根据名称查询单个地点的详细信息
    @Override
    public Result<Addr> findByName(String name) {
        LambdaQueryWrapper<Addr> lqw = new LambdaQueryWrapper<>();
        name = "荆州 魏".equals(name) ? "荆州" : name;
        lqw.eq(Addr::getAddrName,name);
        Addr addr = addrDao.selectOne(lqw);
        if (addr != null) {
            addr.setEventList(getAddrEventList(addr.getEids()));
        }
        return Result.selectSuccess(addrDao.selectOne(lqw));
    }

    //获取地点涉及的事件
    public List<Event> getAddrEventList(String eids) {
        List<Event> eventList = new ArrayList<>();
        if (eids != null && eids.length() >= 1) {
            String[] eids2 = eids.split(",");
            for (String s : eids2) {
                eventList.add(eventDao.selectById(s));
            }
            for (Event event : eventList) {
                event.setEContent("此接口不展示事件内容");
            }
        }
        return eventList;
    }

    @Override
    public List<Addr> findAllCountryByState(String state) {
        QueryWrapper<Addr> qw = new QueryWrapper<>();
        qw.eq("state", state);
        return addrDao.selectList(qw);
    }

    @Override
    public Result<List<Addr>> findAllState() {
        LambdaQueryWrapper<Addr> lqw = new LambdaQueryWrapper<>();
        lqw.isNull(Addr::getState).isNull(Addr::getCountry);
        addrDao.selectList(lqw);
        return Result.selectSuccess(addrDao.selectList(lqw));
    }

    @Override
    public Result<List<Addr>> findAllStateToCascade() {
        List<Addr> allState = findAllState().getData();
        for (Addr addr : allState) {
            addr.setChildren(findAllCountryByState(addr.getAddrName()));
        }
        return Result.selectSuccess(allState);
    }

    @Override
    public Integer save(Addr addr) {
        return addrDao.insert(addr);
    }

    @Override
    public Integer update(Addr addr) {
        return addrDao.updateById(addr);
    }

    @Override
    public Integer delete(Long addrId) {
        return addrDao.deleteById(addrId);
    }

    @Override
    public Result<Integer> deleteMore(List<Long> addrIds) {
        if (addrIds.isEmpty()) {
            return Result.error(ResultCode.BUSINESS_ERR, "未提供要删除的元素");
        }
        Integer i = addrDao.deleteBatchIds(addrIds);

        if (i == addrIds.size()) {
            return Result.deleteSuccess("全部删除成功");
        } else {
            return Result.deleteError("似乎有未删除的元素");
        }
    }
}
