package com.three_kingdoms.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.dao.ActorDao;
import com.three_kingdoms.dao.AddrDao;
import com.three_kingdoms.dao.EventDao;
import com.three_kingdoms.domain.Actor;
import com.three_kingdoms.domain.Addr;
import com.three_kingdoms.domain.Event;
import com.three_kingdoms.services.EventServices;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServicesImpl implements EventServices {
    @Resource
    private EventDao eventDao;
    @Resource
    private AddrDao addrDao;
    @Resource
    private ActorDao actorDao;

    public List<Event> findAll() {
        List<Event> eventList = eventDao.selectList(null);
        return eventFormat(eventList);
    }

    @Override
    public IPage<Event> findAllToPage(Page p) {
        QueryWrapper<Event> qw = new QueryWrapper<>();
        qw.select("eid,addrs,aids,e_name,e_time");
        Page<Event> page = eventDao.selectPage(p, qw);
        page.setRecords(eventFormat(page.getRecords()));
        return page;
    }

    public List<Event> eventFormat(List<Event> eventList) {
        if (!eventList.isEmpty()) {
            for (Event event : eventList) {
                event.setAddrList(getEventAddrList(event.getAddrs()));
                event.setActorList(getEventActorList(event.getAids()));
            }
        }
        return eventList;
    }

    public List<Addr> getEventAddrList(String addrs){
        List<Addr> addrList = new ArrayList<>();
        if (addrs != null) {
            String[] selectAddrs = addrs.split(",");
            addrList = addrDao.selectList(new LambdaQueryWrapper<Addr>().
                    in(Addr::getAddrId, selectAddrs));
            for (Addr addr : addrList) {
                if(addr.getState() == null){
                    addr.setChildren(addrDao.selectList(new QueryWrapper<Addr>().
                            eq("state",addr.getAddrName())));
                }
            }
        }
        return addrList;
    }
    public List<Actor> getEventActorList(String aids){
        List<Actor> actorList = new ArrayList<>();
        if (aids != null) {
            String[] selectActors = aids.split(",");
            actorList = actorDao.selectList(new LambdaQueryWrapper<Actor>().
                    in(Actor::getAid, selectActors).
                    select(Actor::getAid,Actor::getAFname,Actor::getATname));
        }
        return actorList;
    }

    @Override
    public IPage<Event> findAllByName(String name, Page p) {
        LambdaQueryWrapper<Event> lqw = new LambdaQueryWrapper<>();
        lqw.like(Event::getEName, name).select(Event::getEid, Event::getAddrs,
                Event::getAids, Event::getEName, Event::getETime);
        Page<Event> page = eventDao.selectPage(p, lqw);
        page.setRecords(eventFormat(page.getRecords()));
        return page;
    }

    @Override
    public Event findById(Long eid) {
        Event event = eventDao.selectById(eid);
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        event = eventFormat(eventList).get(0);

        return event;
    }

    @Override
    public Integer save(Event event) {
        return eventDao.insert(event);
    }

    @Override
    public Integer update(Event event) {
        return eventDao.updateById(event);
    }

    @Override
    public Integer delete(Long eid) {
        return eventDao.deleteById(eid);
    }

    public Integer deleteMore(List<Long> eids) {
        return eventDao.deleteBatchIds(eids);
    }

}
