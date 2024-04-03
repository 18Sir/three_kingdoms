package com.three_kingdoms.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.dao.CommentDao;
import com.three_kingdoms.dao.MsgDao;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.domain.Msg;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.services.MsgServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsgServicesImpl implements MsgServices {
    @Autowired
    private MsgDao msgDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;
    @Override
    public Map<String,Object> findByMid(Long mid) {
        Map<String,Object> map = new HashMap<>();
        Msg msg = msgDao.selectById(mid);
        if(msg == null){
            return null;
        }
        msg.setCommentNum(getMsgComment(mid).size());
        User u = userDao.selectById(msg.getUid());
        User user = new User();
        user.setUid(u.getUid());
        user.setUname(u.getUname());
        user.setAvatar(u.getAvatar());
        int msgNum = findByUid(u.getUid()).size();
        user.setMsgNum(msgNum);
        map.put("user",user);
        map.put("msg",msg);
        return map;
    }

    public Msg findByMidToMsg(Long mid){
        return msgDao.selectById(mid);
    }


    @Override
    public List<Msg> findByUid(Long uid) {
        QueryWrapper<Msg> qw = new QueryWrapper();
        qw.eq("uid",uid);
        List<Msg> msgList = msgDao.selectList(qw);
        User user = getUser(uid);
        for (int i = 0; i < msgList.size(); i++) {
            Msg msg = msgList.get(i);
            msg.setUserAvatar(user.getAvatar());
            msg.setUserName(user.getUname());
            msgList.set(i,msg);
        }
        return msgList;
    }

    @Override
    public IPage<Msg> findByTitle(String title,Page p) {
        QueryWrapper<Msg> qw = new QueryWrapper();
        qw.like("title",title);
        IPage p2 = new Page(p.getCurrent(),p.getSize());
        IPage page = msgDao.selectPage(p2, qw);
        List<Msg> records = page.getRecords();
        for (int i = 0; i < records.size(); i++) {
            User user = getUser(records.get(i).getUid());
            Msg msg = records.get(i);
            msg.setUserAvatar(user.getAvatar());
            msg.setUserName(user.getUname());
            msg.setCommentNum(getMsgComment(msg.getMid()).size());
            records.set(i,msg);
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public User findMsgUser(Long mid) {
        Msg msg = msgDao.selectById(mid);
        Long uid = msg.getUid();
        User user = userDao.selectById(uid);
        return user;
    }

    @Override
    public IPage findByType(Page p, Integer type) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("type",type);
        IPage p2 = new Page(p.getCurrent(),p.getSize());
        IPage page = msgDao.selectPage(p2, qw);
        List<Msg> records = page.getRecords();
        for (int i = 0; i < records.size(); i++) {
            User user = getUser(records.get(i).getUid());
            Msg msg = records.get(i);
            msg.setUserAvatar(user.getAvatar());
            msg.setUserName(user.getUname());
            msg.setCommentNum(getMsgComment(msg.getMid()).size());
            records.set(i,msg);
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public List<Comment> getMsgComment(Long mid) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("mid",mid);
        List<Comment> list = commentDao.selectList(qw);
        for (int i = 0; i < list.size(); i++) {
            Comment c = list.get(i);
            User u = userDao.selectById(c.getUid());

            c.setUname(u.getUname());
            c.setAvatar(u.getAvatar());
            list.set(i,c);
        }
        return list;
    }

    @Override
    public IPage<Msg> findAll(Page p) {
        IPage page = msgDao.selectPage(p, null);
        List<Msg> records = page.getRecords();
        for (int i = 0; i < records.size(); i++) {
            User user = getUser(records.get(i).getUid());
            Msg msg = records.get(i);
            msg.setUserAvatar(user.getAvatar());
            msg.setUserName(user.getUname());
            msg.setCommentNum(getMsgComment(msg.getMid()).size());
            records.set(i,msg);
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public Result<List<Msg>> findAll() {
        List<Msg> msgList = msgDao.selectList(null);
        for (Msg msg : msgList) {
            User user = getUser(msg.getUid());
            msg.setUserName(user.getUname());
            msg.setUserAvatar(user.getAvatar());
            msg.setCommentNum(getMsgComment(msg.getMid()).size());
        }
        return Result.selectSuccess(msgList);
    }

    @Override
    public Integer save(Msg msg) {
        msg.setVisits(0);
        msg.setLikes(0);
        return msgDao.insert(msg);
    }

    @Override
    public Integer delete(Long mid) {
        return msgDao.deleteById(mid);
    }

    @Override
    public Result<Integer> deleteMore(List<Long> mids) {
        int i = msgDao.deleteBatchIds(mids);
        if(i == mids.size()){
            return Result.deleteSuccess();
        }else{
            return Result.deleteError("似乎有未删除的元素");
        }
    }

    @Override
    public Integer update(Msg msg) {
        return msgDao.updateById(msg);
    }

    //获取用户
    private User getUser(Long uid){
        User user = userDao.selectById(uid);
        return user;
    }


}
