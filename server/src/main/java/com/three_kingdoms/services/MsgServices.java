package com.three_kingdoms.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.domain.Msg;
import com.three_kingdoms.domain.User;

import java.util.List;
import java.util.Map;

public interface MsgServices {
    //根据id查找帖子
    public Map<String,Object> findByMid(Long mid);
    //根据用户名查找帖子
    public List<Msg> findByUid(Long uid);
    //查找帖子所属用户
    public User findMsgUser(Long mid);
    //查找帖子的评论
    public List<Comment> getMsgComment(Long mid);
    //根据帖子标题查找帖子
    public IPage findByTitle(String title,Page p);
    //查找所有帖子
    public IPage<Msg> findAll(Page p);
    //根据类型筛选帖子
    public IPage findByType(Page page,Integer type);
    //添加帖子
    public Integer save(Msg msg);
    //删除帖子
    public Integer delete(Long mid);
    //更新帖子
    public Integer update(Msg msg);
}
