package com.three_kingdoms.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.dao.CommentDao;
import com.three_kingdoms.dao.JuBaoCommentDao;
import com.three_kingdoms.dao.MsgDao;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.*;
import com.three_kingdoms.services.CommentServices;
import com.three_kingdoms.services.JuBaoCommentService;
import com.three_kingdoms.util.JWTUtil;
import com.three_kingdoms.util.SendMessage;
import com.three_kingdoms.util.Verify;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuBaoCommentServiceImpl implements JuBaoCommentService {
    @Resource
    private JuBaoCommentDao juBaoCommentDao;

    @Resource
    private UserDao userDao;

    @Resource
    private MsgDao msgDao;

    @Resource
    private CommentDao commentDao;

    @Resource
    private Verify verify;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CommentServices commentServices;

    @Resource
    private SendMessage sendMessage;

    @Override
    public Result<List<JuBaoComment>> findAll() {
        List<JuBaoComment> list = juBaoCommentDao.selectList(null);
        return Result.selectSuccess(juBaoFormat(list));
    }

    @Override
    public Result<String> save(String token, JuBaoComment juBaoComment) {
        Long uid = JWTUtil.getTokenUid(token);
        juBaoComment.setUid(uid);
        LambdaQueryWrapper<JuBaoComment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(JuBaoComment::getUid,juBaoComment.getUid()).
                eq(JuBaoComment::getBeUid,juBaoComment.getBeUid()).
                eq(JuBaoComment::getMid,juBaoComment.getMid()).
                eq(JuBaoComment::getCid,juBaoComment.getCid());
        List<JuBaoComment> list = juBaoCommentDao.selectList(lqw);
        if(list.isEmpty()){
            int i = juBaoCommentDao.insert(juBaoComment);
            if(i > 0){
                return Result.saveSuccess("举报成功，请等待管理员处理");
            }else{
                return Result.saveError("举报失败，请稍后重试");
            }
        }else{
            juBaoComment.setJid(list.get(0).getJid());
            int i = juBaoCommentDao.updateById(juBaoComment);
            if(i > 0){
                return Result.updateSuccess("举报成功，请等待管理员处理");
            }else{
                return Result.updateError("举报失败，请稍后重试");
            }
        }
    }

    @Override
    public Result<String> updateById(String token, JuBaoComment juBaoComment) {
        Long uid = JWTUtil.getTokenUid(token);
        if(!verify.isAdmin(uid)){
            return Result.updateError("您没有这项权限");
        }
        int i = juBaoCommentDao.updateById(juBaoComment);
        if(i > 0){
            if(juBaoComment.getResult().contains("删除")){
                Long cid = juBaoComment.getCid();
                Comment comment = commentDao.selectById(cid);
                Result<String> result = commentServices.delete(token, cid);
                if(result.getCode().toString().endsWith("OK")){
                    result.setMsg("评论已删除");
                    if(!juBaoComment.getResult().contains("封禁")){
                        Msg msg = msgDao.selectById(comment.getMid());
                        //向举报用户发送反馈
                        String content = "你好，你在帖子【"+msg.getTitle()+"】下举报的评论【" +
                                comment.getContent() +
                                "】,理由："+juBaoComment.getReason()+",被确认违规，我们已删除该评论，" +
                                "感谢你对三国历史可视化展示平台社区做出的贡献";
                        sendMessage.sendSysMsgForJuBao(juBaoComment.getUid(), content);
                        //向被举报用户发送反馈
                        String beContent = "您好，您在帖子【"+msg.getTitle()+"】下发布的评论【" +
                                comment.getContent() +
                                "】,被举报确认违规，理由："+juBaoComment.getReason()+
                                "，为维护社区环境，我们已删除该评论，希望您遵守社区规则";
                        sendMessage.sendSysMsgForWeiGui(juBaoComment.getBeUid(),beContent);
                    }
                }else{
                    result.setMsg("评论删除失败，稍后重试");
                }
                return result;
            }
            return Result.updateSuccess("操作成功");
        }else{
            return Result.updateError("操作失败，请稍后重试");
        }
    }

    @Override
    public Result<String> deleteById(String token, Long jid) {
        if(verify.isAdmin(token)){
            int i = juBaoCommentDao.deleteById(jid);
            if(i > 0){
                return Result.deleteSuccess("处理成功");
            }else{
                return Result.deleteSuccess("处理失败，请稍后重试");
            }
        }
        return null;
    }

    @Override
    public Result<Integer> deleteMore(List<Long> jids) {
        int i = juBaoCommentDao.deleteBatchIds(jids);
        if(i == jids.size()){
            return Result.deleteSuccess();
        }else{
            return Result.deleteError("似乎有未删除的元素");
        }
    }

    private List<JuBaoComment> juBaoFormat(List<JuBaoComment> list){
        for (JuBaoComment juBaoComment : list) {
            User user = userDao.selectById(juBaoComment.getUid());
            User beUser = userDao.selectById(juBaoComment.getBeUid());
            Msg msg = msgDao.selectById(juBaoComment.getMid());
            Comment comment = commentDao.selectById(juBaoComment.getCid());
            if(comment == null){
                comment = new Comment();
                comment.setCid(juBaoComment.getCid());
                comment.setContent("<span style=\"color:red;\">该评论已删除，内容已不可见</span>");
            }
            if(user == null){
                user = new User();
                user.setUname("用户已注销");
            }
            if(beUser == null){
                beUser = new User();
                beUser.setUname("用户已注销");
            }
            if (msg == null) {
                msg = new Msg();
                msg.setTitle("<span style=\"color:red;\">该帖已被删除，标题已无法查看!</span>");
                msg.setContent("<span style=\"color:red;\">该帖已被删除，内容已无法查看!</span>");
            }
            juBaoComment.setUser(user);
            juBaoComment.setBeUser(beUser);
            juBaoComment.setMsg(msg);
            juBaoComment.setComment(comment);
        }
        return list;
    }
}
