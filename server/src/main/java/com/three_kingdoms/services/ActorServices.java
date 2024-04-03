package com.three_kingdoms.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Actor;
import com.three_kingdoms.domain.Addr;
import com.three_kingdoms.domain.Event;

import java.util.List;

public interface ActorServices {
    //分页查询所有人物
    IPage<Actor> findAllToPage(Long current,Long size);
    //直接查询全部人物
    List<Actor> findAllActor();
    //按名或字分页查询所有人物
    IPage<Actor> findAllByName(String name, Page<Actor> page);
    //按id查询人物
    Actor findById(Long aid);
    //查找人物参与的历史事件
    List<Event> findEvents(Long aid);
    //查找人物的出生地
    Addr findActorAddr(Long aid);
    //根据势力范围查找
    IPage<Actor> findAllByPower(IPage<Actor> p,String name,String[] powers);
    //增加人物
    Integer save(Actor actor);
    //删除人物
    Integer delete(Long aid);
    //删除更多
    Integer deleteMore(List<Long> aids);
    //修改人物
    Integer update(Actor actor);
}
