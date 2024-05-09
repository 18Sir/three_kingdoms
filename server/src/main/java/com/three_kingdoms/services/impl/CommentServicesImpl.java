package com.three_kingdoms.services.impl;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.dao.CommentDao;
import com.three_kingdoms.dao.JuBaoCommentDao;
import com.three_kingdoms.dao.MsgDao;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.domain.Msg;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.services.CommentServices;
import com.three_kingdoms.util.JWTUtil;
import com.three_kingdoms.util.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommentServicesImpl implements CommentServices {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MsgDao msgDao;

    @Autowired
    private Verify verify;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JuBaoCommentDao juBaoCommentDao;

    @Override
    public Result<String> save(String token, Comment c) {
        Long uid = JWTUtil.getTokenUid(token);
        if(!verify.isBlocked(uid)){
            c.setUid(uid);
            Msg msg = msgDao.selectById(c.getMid());
            if(msg.getStatus() != 1){
                return Result.saveError("审核未通过的帖暂时不能发布评论");
            }
            Integer i = commentDao.insert(c);
            if(i > 0){
                return Result.success(ResultCode.SAVE_OK,"评论发表成功",null);
            }else{
                return Result.error(ResultCode.SAVE_ERR,"评论发表失败");
            }
        }else{
            return Result.saveError("您正处于封禁中，暂时不可发布任何内容");
        }

    }

    @Override
    public Result<String> reply(String token, Comment c) {
        Long uid = JWTUtil.getTokenUid(token);
        if(!verify.isBlocked(uid)){
            c.setUid(uid);
            Msg msg = msgDao.selectById(c.getMid());
            if(msg.getStatus() != 1){
                return Result.saveError("审核未通过的帖暂时不能发布评论");
            }
            Comment beC = commentDao.selectById(c.getBeCid());
            Integer i = commentDao.insert(c);
            if(i > 0){
                String replyKey = "three-kingdoms:comment:"+c.getBeCid()+":reply";
                redisTemplate.opsForSet().add(replyKey,c.getCid());
                beC.setReplyNum(redisTemplate.opsForSet().size(replyKey));
                commentDao.updateById(setCommentHot(beC));
                return Result.success(ResultCode.SAVE_OK,"评论回复成功",null);
            }else{
                return Result.error(ResultCode.SAVE_ERR,"评论回复失败");
            }
        }else{
            return Result.saveError("您正处于封禁中，暂时不可发布任何内容");
        }

    }

    @Override
    public Result<String> delete(String token, Long cid) {
        Long uid1 = JWTUtil.getTokenUid(token);
        Comment comment = commentDao.selectById(cid);
        Long uid2 = comment.getUid();
        Long msgUid = msgDao.selectById(comment.getMid()).getUid();
        if(uid1.equals(uid2) || uid1.equals(msgUid) || verify.isAdmin(token)){
            int i = commentDao.deleteById(cid);
            if(i > 0){
                String replyKey = "three-kingdoms:comment:"+cid+":reply";
                String beRplyKey = "three-kingdoms:comment:"+comment.getBeCid()+":reply";
                if(redisTemplate.opsForSet().isMember(beRplyKey,cid)){
                    redisTemplate.opsForSet().remove(beRplyKey,cid);
                }
                redisTemplate.delete(replyKey);
                juBaoCommentDao.deleteById(cid);
                return Result.deleteSuccess();
            }else{
                return Result.deleteError();
            }
        }else{
            return Result.error(ResultCode.DELETE_ERR,"您没有权限操作");
        }
    }

    @Override
    public Integer update(Comment c) {
        return commentDao.updateById(c);
    }

    @Override
    public Comment findById(Long cid) {
        Comment c = commentDao.selectById(cid);
        User u = userDao.selectById(c.getUid());
        c.setUname(u.getUname());
        c.setAvatar(u.getAvatar());
        return commentDao.selectById(cid);
    }

    //点赞评论
    @Override
    public Result<Long> likes(String token, Long cid, Boolean b) {
        Long uid = JWTUtil.getTokenUid(token);
        String likesKey = "three-kingdoms:comment:"+cid+":likes";
        String unLikesKey = "three-kingdoms:comment:"+cid+":unLikes";
        String msg = "";
        if(b){
            redisTemplate.opsForSet().add(likesKey,uid);
            //如果点过踩了取消踩更新点踩数
            if(redisTemplate.opsForSet().isMember(unLikesKey,uid)){
                redisTemplate.opsForSet().remove(unLikesKey,uid);
                Comment newComment = commentDao.selectById(cid);
                newComment.setUnLikes(redisTemplate.opsForSet().size(unLikesKey));
                commentDao.updateById(newComment);
            }
            msg = "点赞收到啦！";
        }else{
            if(redisTemplate.opsForSet().isMember(likesKey,uid)){
                redisTemplate.opsForSet().remove(likesKey,uid);
                msg = "取消点赞";
            }
        }
        Long likes = redisTemplate.opsForSet().size(likesKey);
        Comment newComment = commentDao.selectById(cid);
        newComment.setLikes(likes);
        commentDao.updateById(setCommentHot(newComment));
        return Result.success(ResultCode.UPDATE_OK,msg,likes);
    }

    //点踩评论
    @Override
    public Result<Long> unLikes(String token, Long cid, Boolean b) {
        Long uid = JWTUtil.getTokenUid(token);
        String unLikesKey = "three-kingdoms:comment:"+cid+":unLikes";
        String likesKey = "three-kingdoms:comment:"+cid+":likes";
        String msg = "";
        if(b){
            redisTemplate.opsForSet().add(unLikesKey,uid);
            //如果点过赞了则取消点赞并更新点赞数
            if(redisTemplate.opsForSet().isMember(likesKey,uid)){
                redisTemplate.opsForSet().remove(likesKey,uid);
                Comment newComment = commentDao.selectById(cid);
                newComment.setLikes(redisTemplate.opsForSet().size(likesKey));
                commentDao.updateById(newComment);
            }
            msg = "踩！";
        }else{
            if(redisTemplate.opsForSet().isMember(unLikesKey,uid)){
                redisTemplate.opsForSet().remove(unLikesKey,uid);
                msg = "取消踩";
            }
        }
        Long unLikes = redisTemplate.opsForSet().size(unLikesKey);
        Comment newComment = commentDao.selectById(cid);
        newComment.setUnLikes(unLikes);
        commentDao.updateById(setCommentHot(newComment));
        return Result.success(ResultCode.UPDATE_OK,msg,redisTemplate.opsForSet().size(likesKey));
    }

    //设置热度
    private Comment setCommentHot(Comment c){
        Long newHot = c.getLikes() + c.getReplyNum() - c.getUnLikes();
        c.setHot(newHot);
        return c;
    }
}
