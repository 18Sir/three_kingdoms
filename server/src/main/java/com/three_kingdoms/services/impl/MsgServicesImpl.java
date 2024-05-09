package com.three_kingdoms.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.dao.CommentDao;
import com.three_kingdoms.dao.MsgDao;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.domain.Msg;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.services.MsgServices;
import com.three_kingdoms.util.JWTUtil;
import com.three_kingdoms.util.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MsgServicesImpl implements MsgServices {
    @Autowired
    private MsgDao msgDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private Verify verify;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> findByMid(Long mid) {
        Map<String, Object> map = new HashMap<>();
        Msg msg = msgDao.selectById(mid);
        if (msg == null) {
            return new HashMap<>();
        }
        msg.setCommentNum(getMsgComment(mid).size());
        User u = userDao.selectById(msg.getUid());
        if(u == null){
            u = new User();
            u.setUname("用户已注销");

        }
        User user = new User();
        user.setUid(u.getUid());
        user.setUname(u.getUname());
        user.setAvatar(u.getAvatar());
        int msgNum = findByUid(u.getUid()).size();
        user.setMsgNum(msgNum);
        map.put("user", user);
        map.put("msg", msg);
        return map;
    }

    public Msg findByMidToMsg(Long mid) {
        return msgDao.selectById(mid);
    }


    @Override
    public List<Msg> findByUid(Long uid) {
        if(uid == null){
            return new ArrayList<>();
        }
        QueryWrapper<Msg> qw = new QueryWrapper();
        qw.eq("uid", uid);
        List<Msg> msgList = msgDao.selectList(qw);
        User user = getUser(uid);
        for (Msg msg : msgList) {
            msg.setUserAvatar(user.getAvatar());
            msg.setUserName(user.getUname());
        }
        return msgList;
    }

    @Override
    public IPage<Msg> findByTitle(String title, Page p) {
        QueryWrapper<Msg> qw = new QueryWrapper();
        qw.like("title", title);
        IPage p2 = new Page(p.getCurrent(), p.getSize());
        IPage page = msgDao.selectPage(p2, qw);
        List<Msg> records = page.getRecords();
        for (Msg record : records) {
            User user = getUser(record.getUid());
            record.setUserAvatar(user.getAvatar());
            record.setUserName(user.getUname());
            record.setCommentNum(getMsgComment(record.getMid()).size());
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public User findMsgUser(Long mid) {
        Msg msg = msgDao.selectById(mid);
        Long uid = msg.getUid();
        User user = userDao.selectById(uid);
        return user == null ? new User() : user;
    }

    @Override
    public IPage findByType(Page p, Integer type) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("type", type);
        IPage p2 = new Page(p.getCurrent(), p.getSize());
        IPage page = msgDao.selectPage(p2, qw);
        List<Msg> records = page.getRecords();
        for (Msg record : records) {
            User user = getUser(record.getUid());
            record.setUserAvatar(user.getAvatar());
            record.setUserName(user.getUname());
            record.setCommentNum(getMsgComment(record.getMid()).size());
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public List<Comment> getMsgComment(Long mid) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("mid", mid);
        List<Comment> list = commentDao.selectList(qw);
        for (int i = 0; i < list.size(); i++) {
            Comment c = list.get(i);
            User u = userDao.selectById(c.getUid());
            if(u == null){
                u= new User();
                u.setUname("用户已注销");
            }
            String replyKey = "three-kingdoms:comment:" + c.getCid() + ":reply";
            Set<Comment> commentIds = redisTemplate.opsForSet().members(replyKey);
            if (!commentIds.isEmpty()) {
                LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
                lqw.in(Comment::getCid, commentIds);
                c.setCommentList(commentDao.selectList(lqw));
            }
            c.setUname(u.getUname());
            c.setAvatar(u.getAvatar());
            list.set(i, c);
        }
        return list;
    }

    //返回给用户端的评论数据
    @Override
    public Result<List<Comment>> getMsgCommentForUser(String token, Long mid, String sort) {
        Long uid = JWTUtil.getTokenUid(token);
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getMid, mid).eq(Comment::getHeight, 0);
        if ("time".equals(sort)) {
            lqw.orderByDesc(Comment::getCreateTime);
        } else {
            lqw.orderByDesc(Comment::getHot);
        }
        List<Comment> list = packComment(uid, commentDao.selectList(lqw));
        for (Comment c : list) {
            String replyKey = "three-kingdoms:comment:" + c.getCid() + ":reply";
            Set<Comment> commentIds = redisTemplate.opsForSet().members(replyKey);
            if (!commentIds.isEmpty()) {
                LambdaQueryWrapper<Comment> lqw2 = new LambdaQueryWrapper<>();
                lqw2.in(Comment::getCid, commentIds);
                lqw2.orderByDesc(Comment::getHot);
                List<Comment> commentList = packComment(uid, commentDao.selectList(lqw2));
                c.setCommentList(commentList);
            }
        }
        return Result.selectSuccess(list);
    }

    //包装返回的评论数据
    private List<Comment> packComment(Long uid, List<Comment> list) {
        for (Comment comment : list) {
            User u = userDao.selectById(comment.getUid());
            if(u == null){
                comment.setUname("用户已注销");
                comment.setAvatar("");
            }else{
                comment.setUname(u.getUname());
                comment.setAvatar(u.getAvatar());
            }
            //获取评论的点赞列表，然后判断用户是否点过赞或点过踩
            String likesKey = "three-kingdoms:comment:" + comment.getCid() + ":likes";
            String unLikesKey = "three-kingdoms:comment:" + comment.getCid() + ":unLikes";
            Boolean isLiked = redisTemplate.opsForSet().isMember(likesKey, uid);
            Boolean isUnLiked = redisTemplate.opsForSet().isMember(unLikesKey, uid);
            comment.setIsLiked(isLiked);
            comment.setIsUnLiked(isUnLiked);
            User replyUser = comment.getReplyUid() != null ?
                    userDao.selectById(comment.getReplyUid()) :
                    null;
            comment.setReplyUser(replyUser);
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
            records.set(i, msg);
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public Result<List<Msg>> findAll() {
        List<Msg> msgList = msgDao.selectList(null);
        for (Msg msg : msgList) {
            User user = getUser(msg.getUid());
            List<Comment> commentList = getMsgComment(msg.getMid());
            msg.setUserName(user.getUname());
            msg.setUserAvatar(user.getAvatar());
            msg.setCommentList(commentList);
            msg.setCommentNum(getMsgComment(msg.getMid()).size());
        }
        return Result.selectSuccess(msgList);
    }

    @Override
    public Result<String> save(String token,Msg msg) {
        Long uid = JWTUtil.getTokenUid(token);
        //判断是否被封禁
        if(!verify.isBlocked(uid)){
            msg.setUid(uid);
            Integer i = msgDao.insert(msg);
            if (i > 0) {
                return Result.saveSuccess("发布成功！");
            } else
                return Result.saveError("发布失败！");
        }else{
            return Result.saveError("您正处于封禁中，暂时不可发布任何内容");
        }

    }

    @Override
    public Integer delete(Long mid) {
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getMid,mid);
        List<Comment> comments = commentDao.selectList(lqw);
        if(!comments.isEmpty()){
            for (Comment comment : comments) {
                commentDao.deleteById(comment);
            }
        }
        return msgDao.deleteById(mid);
    }

    @Override
    public Result<Integer> deleteMore(List<Long> mids) {
        int i = msgDao.deleteBatchIds(mids);
        if (i == mids.size()) {
            return Result.deleteSuccess();
        } else {
            return Result.deleteError("似乎有未删除的元素");
        }
    }

    @Override
    public Integer update(Msg msg) {
        msg.setStatus(0);
        return msgDao.updateById(msg);
    }

    //获取用户
    private User getUser(Long uid) {
        User user = userDao.selectById(uid);
        return user == null ? new User() : user;
    }

    //    封装返回的帖子数据
    private List<Msg> RecordsFormat(List<Msg> records) {
        for (Msg msg : records) {
            User user = getUser(msg.getUid());
            msg.setUserAvatar(user.getAvatar());
            msg.setUserName(user.getUname());
            msg.setCommentNum(getMsgComment(msg.getMid()).size());
        }
        return records;
    }

    //分页获取所有已审核的帖子
    @Override
    public Result<IPage<Msg>> findAllCheckedMsg(IPage<Msg> page, String sort) {
        LambdaQueryWrapper<Msg> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Msg::getStatus, 1);
        if ("time".equals(sort)) {
            lqw.orderByDesc(Msg::getTime);
        } else {
            lqw.orderByDesc(Msg::getHot);
        }
        IPage<Msg> msgPage = msgDao.selectPage(page, lqw);
        msgPage.setRecords(RecordsFormat(msgPage.getRecords()));
        return Result.selectSuccess(msgPage);
    }

    //根据类型筛选所有已审核的帖子
    @Override
    public Result<IPage<Msg>> findAllCheckedByType(IPage<Msg> page, Integer type, String sort) {
        LambdaQueryWrapper<Msg> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Msg::getStatus, 1).eq(Msg::getType, type);
        if ("time".equals(sort)) {
            lqw.orderByDesc(Msg::getTime);
        } else {
            lqw.orderByDesc(Msg::getHot);
        }
        IPage<Msg> msgPage = msgDao.selectPage(page, lqw);
        msgPage.setRecords(RecordsFormat(msgPage.getRecords()));
        return Result.selectSuccess(msgPage);
    }

    //根据标题名查找所有已审核的帖子
    @Override
    public Result<IPage<Msg>> findAllCheckedByTitle(IPage<Msg> page, String title, String sort) {
        LambdaQueryWrapper<Msg> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Msg::getStatus, 1).like(Msg::getTitle, title);
        if ("time".equals(sort)) {
            lqw.orderByDesc(Msg::getTime);
        } else {
            lqw.orderByDesc(Msg::getHot);
        }
        IPage<Msg> msgPage = msgDao.selectPage(page, lqw);
        msgPage.setRecords(RecordsFormat(msgPage.getRecords()));
        return Result.selectSuccess(msgPage);
    }

    @Override
    public Result<String> changeMsgStatus(Msg msg) {
        UpdateWrapper<Msg> uw = new UpdateWrapper<>();
        uw.eq("mid", msg.getMid());
        uw.set("status", msg.getStatus());
        int i = msgDao.update(uw);
        if (i > 0) {
            return Result.updateSuccess("操作成功");
        } else {
            return Result.updateSuccess("操作失败");
        }
    }

    //封装单个帖子数据
    public Map<String, Object> msgFormatByMsg(Msg msg) {
        if (msg == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        msg.setCommentNum(getMsgComment(msg.getMid()).size());
        User u = userDao.selectById(msg.getUid());
        User user = new User();
        user.setUid(u.getUid());
        user.setUname(u.getUname());
        user.setAvatar(u.getAvatar());
        int msgNum = findByUid(u.getUid()).size();
        user.setMsgNum(msgNum);
        map.put("user", user);
        map.put("msg", msg);
        return map;
    }

    //增加访问量同时返回帖子
    @Override
    public Result<Map> findByIdWithViews(String token, Long mid) {
        Long uid = JWTUtil.getTokenUid(token);
        Msg msg = msgDao.selectById(mid);
        //帖子的访问用户key
        String visitsKey = "three-kingdoms:msg:" + mid + ":visits";
        //访问的用户的历史记录key
        String historyKey = "three-kingdoms:user:" + uid + ":historyMsg";
        //获取系统当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //将用户的访问记录保存下来
        redisTemplate.opsForHash().put(historyKey, mid + "", format.format(new Date().getTime()));
        redisTemplate.expire(historyKey, 30, TimeUnit.DAYS);
        //判断访问帖子的用户是否访问过该帖
        Boolean b = redisTemplate.opsForSet().isMember(visitsKey, uid);
        Msg newMsg = msgDao.selectById(mid);
        if (!b) {
            //将该用户添加进帖子的访问记录内
            redisTemplate.opsForSet().add(visitsKey, uid);
            Long length = redisTemplate.opsForSet().size(visitsKey);
            newMsg.setVisits(length);
            msgDao.updateById(setMsgHot(newMsg));
        }
        //先获取帖子的点赞列表，然后判断用户是否点过赞
        String likesKey = "three-kingdoms:msg:" + mid + ":likes";
        String unLikesKey = "three-kingdoms:msg:" + mid + ":unLikes";
        Boolean isLikes = redisTemplate.opsForSet().isMember(likesKey, uid);
        Boolean isUnLikes = redisTemplate.opsForSet().isMember(unLikesKey, uid);
        Map<String, Object> map = msgFormatByMsg(newMsg);
        map.put("isLikes", isLikes);
        map.put("isUnLikes", isUnLikes);
        return Result.selectSuccess(map);
    }

    //点赞
    @Override
    public Result<Long> likes(String token, Long mid, Boolean b) {
        Long uid = JWTUtil.getTokenUid(token);
        String likesKey = "three-kingdoms:msg:" + mid + ":likes";
        String unLikesKey = "three-kingdoms:msg:" + mid + ":unLikes";
        String msg = "";
        if (b) {
            redisTemplate.opsForSet().add(likesKey, uid);
            //如果点过踩了则取消点踩并更新点踩数
            if (redisTemplate.opsForSet().isMember(unLikesKey, uid)) {
                redisTemplate.opsForSet().remove(unLikesKey, uid);
                Msg newMsg = msgDao.selectById(mid);
                newMsg.setUnLikes(redisTemplate.opsForSet().size(unLikesKey));
                msgDao.updateById(newMsg);
            }
            msg = "点赞收到啦！";
        } else {
            if (redisTemplate.opsForSet().isMember(likesKey, uid)) {
                redisTemplate.opsForSet().remove(likesKey, uid);
                msg = "取消点赞";
            }
        }
        Long likes = redisTemplate.opsForSet().size(likesKey);
        Msg newMsg = msgDao.selectById(mid);
        newMsg.setLikes(likes);
        msgDao.updateById(setMsgHot(newMsg));
        return Result.success(ResultCode.UPDATE_OK, msg, likes);
    }

    //点踩
    @Override
    public Result<Long> unLikes(String token, Long mid, Boolean b) {
        Long uid = JWTUtil.getTokenUid(token);
        String likesKey = "three-kingdoms:msg:" + mid + ":likes";
        String unLikesKey = "three-kingdoms:msg:" + mid + ":unLikes";
        String msg = "";
        if (b) {
            redisTemplate.opsForSet().add(unLikesKey, uid);
            //如果点过赞了则取消点赞并更新点赞数
            if (redisTemplate.opsForSet().isMember(likesKey, uid)) {
                redisTemplate.opsForSet().remove(likesKey, uid);
                Msg newMsg = msgDao.selectById(mid);
                newMsg.setUnLikes(redisTemplate.opsForSet().size(likesKey));
                msgDao.updateById(newMsg);
            }
            msg = "踩！";
        } else {
            if (redisTemplate.opsForSet().isMember(unLikesKey, uid)) {
                redisTemplate.opsForSet().remove(unLikesKey, uid);
                msg = "取消点踩";
            }
        }
        Long unLikes = redisTemplate.opsForSet().size(unLikesKey);
        Msg newMsg = msgDao.selectById(mid);
        newMsg.setLikes(unLikes);
        msgDao.updateById(setMsgHot(newMsg));
        return Result.success(ResultCode.UPDATE_OK, msg, redisTemplate.opsForSet().size(likesKey));
    }

    @Override
    public Result<Long> share(String token, Long mid) {
        Long uid = JWTUtil.getTokenUid(token);
        String sharesKey = "three-kingdoms:msg:" + mid + ":shares";

        redisTemplate.opsForSet().add(sharesKey, uid);
        Long shares = redisTemplate.opsForSet().size(sharesKey);
        Msg newMsg = msgDao.selectById(mid);
        newMsg.setShare(shares);
        msgDao.updateById(setMsgHot(newMsg));
        return Result.success(ResultCode.UPDATE_OK, "分享成功", shares);
    }

    //设置热度
    private Msg setMsgHot(Msg msg) {
        Long newHot = msg.getVisits() + msg.getLikes() + msg.getShare() - msg.getUnLikes();
        msg.setHot(newHot);
        return msg;
    }


}
