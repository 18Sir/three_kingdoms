package com.three_kingdoms.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.dao.AddrDao;
import com.three_kingdoms.dao.EventDao;
import com.three_kingdoms.domain.Addr;
import com.three_kingdoms.domain.Event;
import com.three_kingdoms.services.AddrServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddrServicesImpl implements AddrServices {
    @Autowired
    private AddrDao addrDao;

    @Autowired
    private EventDao eventDao;

    @Override
    public IPage<Addr> findAll(Page p) {
        IPage page = new Page(p.getCurrent(), p.getSize());
        return addrDao.selectPage(page, null);
    }

    @Override
    public Addr findById(Long addrId) {
        Addr addr = addrDao.selectById(addrId);
        if (addr != null) {
            String eid = addr.getEids();
            if (eid != null) {
                String[] eids = eid.split(",");
                List<Event> eventList = new ArrayList<>();
                for (int i = 0; i < eids.length; i++) {
                    eventList.add(eventDao.selectById(eids[i]));
                }
                addr.setEvents(eventList);
            }
        }
        return addr;
    }

    @Override
    public List<Addr> findAllByState(String state) {
        QueryWrapper<Addr> qw = new QueryWrapper();
        qw.eq("state",state);
        List<Addr> addrs = addrDao.selectList(qw);
        return addrs;
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
}
