package com.three_kingdoms.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Actor;
import com.three_kingdoms.domain.Addr;
import com.three_kingdoms.domain.Event;

import java.util.List;

public interface ActorServices {
    //分页查询所有人物
    public IPage findAllToPage(Long current,Long size);
    //按名或字分页查询所有人物
    public IPage findAllByName(String name, Page page);
    //按id查询人物
    public Actor findById(Long aid);
    //查找人物参与的历史事件
    public List<Event> findEvents(Long aid);
    //查找人物的出生地
    public Addr findActorAddr(Long aid);
    //根据势力范围查找
    public IPage findAllByPower(IPage p,String name,String[] powers);
    //增加人物
    public Integer save(Actor actor);
    //删除人物
    public Integer delete(Long aid);
    //修改人物
    public Integer update(Actor actor);
}
