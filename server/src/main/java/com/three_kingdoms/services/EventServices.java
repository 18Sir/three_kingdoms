package com.three_kingdoms.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Event;

public interface EventServices {
    //查询所有事件信息
    public IPage<Event> findAllToPage(Page p);

    //按事件名查询事件信息
    public IPage<Event> findAllByName(String name, Page p);

    //查询单个事件详细信息
    public Event findById(Long eid);

    //增加事件
    public Integer save(Event event);

    //修改事件
    public Integer update(Event event);

    //删除事件
    public Integer delete(Long eid);

}
