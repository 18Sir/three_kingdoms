package com.three_kingdoms.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.domain.Msg;
import com.three_kingdoms.domain.User;

import java.util.List;
import java.util.Map;

public interface MsgServices {
    //根据id查找帖子
    Map<String,Object> findByMid(Long mid);
    //根据用户名查找帖子
    List<Msg> findByUid(Long uid);
    //查找帖子所属用户
    User findMsgUser(Long mid);
    //查找帖子的评论
    List<Comment> getMsgComment(Long mid);
    //用户端查找帖子的评论
    Result<List<Comment>> getMsgCommentForUser(String token,Long mid,String sort);
    //根据帖子标题查找帖子
    IPage findByTitle(String title,Page p);
    //分页查找所有帖子
    IPage<Msg> findAll(Page p);
    //直接查找所有帖子
    Result<List<Msg>> findAll();
    //根据类型筛选帖子
    IPage findByType(Page page,Integer type);
    //添加帖子
    Result<String> save(String token,Msg msg);
    //删除帖子
    Integer delete(Long mid);
    //删除更多
    Result<Integer> deleteMore(List<Long> mids);
    //更新帖子
    Integer update(Msg msg);

    Msg findByMidToMsg(Long mid);

    //分页获取所有已审核的帖子
    Result<IPage<Msg>> findAllCheckedMsg(IPage<Msg> page,String sort);

    //根据类型筛选所有已审核的帖子
    Result<IPage<Msg>> findAllCheckedByType(IPage<Msg> page,Integer type,String sort);

    //根据标题名查找所有已审核的帖子
    Result<IPage<Msg>> findAllCheckedByTitle(IPage<Msg> page,String title,String sort);

    //更改审核状态
    Result<String> changeMsgStatus(Msg msg);

    //增加访问量同时返回帖子
    Result<Map> findByIdWithViews(String token,Long mid);

    //点赞逻辑
    Result<Long> likes(String token,Long mid,Boolean b);

    //点踩逻辑
    Result<Long> unLikes(String token,Long mid,Boolean b);

    //分享
    Result<Long> share(String token,Long mid);

}
