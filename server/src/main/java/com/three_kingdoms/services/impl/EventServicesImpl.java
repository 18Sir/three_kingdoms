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
        if (eventList.isEmpty()) {
            for (int i = 0; i < eventList.size(); i++) {
                //
                List<Addr> addrList = new ArrayList<>();
                List<Actor> actorList = new ArrayList<>();
                Event event = eventList.get(i);
                if (event.getAddrs() != null) {
                    String[] selectAddrs = eventList.get(i).getAddrs().split(",");
                    addrList = addrDao.selectList(new QueryWrapper<Addr>().in("addr_id", selectAddrs));
                }
                if (event.getAids() != null) {
                    String[] selectActors = eventList.get(i).getAids().split(",");
                    actorList = actorDao.selectList(new QueryWrapper<Actor>().in("aid", selectActors));
                }

                List<String> addrs = new ArrayList<>();
                for (int j = 0; j < addrList.size(); j++) {
                    addrs.add(addrList.get(j).getAddrName());
                }

                List<String> actors = new ArrayList<>();
                for (int j = 0; j < actorList.size(); j++) {
                    actors.add(actorList.get(j).getAFname());
                }
                Event e = eventList.get(i);
                e.setAddrList(addrs);
                e.setActorList(actors);

                eventList.set(i, e);
            }
        }
        return eventList;
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
        List<Addr> addrList = new ArrayList<>();
        List<Actor> actorList = new ArrayList<>();
        if (event != null) {
            String addrStr = event.getAddrs();
            String[] addrs = addrStr.split(",");
            for (int i = 0; i < addrs.length; i++) {
                Addr addr = addrDao.selectById(addrs[i]);
                addrList.add(addr);
            }
            String actorStr = event.getAids();
            String[] actors = actorStr.split(",");
            for (int i = 0; i < actors.length; i++) {
                Actor actor = actorDao.selectById(actors[i]);
                actor.setStory("考虑性能，此接口不展示人物传记内容");
                actorList.add(actor);
            }
            event.setAddrList(addrList);
            event.setActorList(actorList);
        }

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
