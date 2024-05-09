package com.three_kingdoms.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.dao.JuBaoMsgDao;
import com.three_kingdoms.dao.MsgDao;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.JuBaoMsg;
import com.three_kingdoms.domain.Msg;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.services.JuBaoMsgService;
import com.three_kingdoms.services.MsgServices;
import com.three_kingdoms.util.JWTUtil;
import com.three_kingdoms.util.SendMessage;
import com.three_kingdoms.util.Verify;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JuBaoMsgServiceImpl implements JuBaoMsgService {
    @Resource
    private JuBaoMsgDao juBaoMsgDao;

    @Resource
    private UserDao userDao;

    @Resource
    private MsgDao msgDao;

    @Resource
    private MsgServices msgServices;

    @Resource
    private SendMessage sendMessage;

    @Resource
    private Verify verify;

    @Override
    public Result<List<JuBaoMsg>> findAll() {
        List<JuBaoMsg> list = juBaoMsgDao.selectList(null);
        return Result.selectSuccess(juBaoFormat(list));
    }

    @Override
    public Result<String> save(String token, JuBaoMsg juBaoMsg) {
        Long uid = JWTUtil.getTokenUid(token);
        juBaoMsg.setUid(uid);
        LambdaQueryWrapper<JuBaoMsg> lqw = new LambdaQueryWrapper<>();
        lqw.eq(JuBaoMsg::getUid, juBaoMsg.getUid()).
                eq(JuBaoMsg::getBeUid, juBaoMsg.getBeUid()).
                eq(JuBaoMsg::getMid, juBaoMsg.getMid());
        List<JuBaoMsg> list = juBaoMsgDao.selectList(lqw);
        if (list.isEmpty()) {
            int i = juBaoMsgDao.insert(juBaoMsg);
            if (i > 0) {
                return Result.saveSuccess("举报成功，请等待管理员处理");
            } else {
                return Result.saveError("举报失败，请稍后重试");
            }
        } else {
            juBaoMsg.setJid(list.get(0).getJid());
            int i = juBaoMsgDao.updateById(juBaoMsg);
            if (i > 0) {
                return Result.updateSuccess("举报成功，请等待管理员处理");
            } else {
                return Result.updateError("举报失败，请稍后重试");
            }
        }
    }

    @Override
    @Transactional
    public Result<String> updateById(String token, JuBaoMsg juBaoMsg) {
        Long uid = JWTUtil.getTokenUid(token);
        if (!verify.isAdmin(uid)) {
            return Result.updateError("您没有这项权限");
        }
        String result = juBaoMsg.getResult();
        Long mid = juBaoMsg.getMid();
        int i = juBaoMsgDao.updateById(juBaoMsg);
        String beContent = "";
        String content = "";
        Msg msg = msgDao.selectById(mid);
        if ("打回".equals(result)) {
            msg.setStatus(-1);
            //向被举报用户发送消息
            beContent = "您好，您的帖子<span style=\"color:blue;\">【" + msg.getTitle() +
                    "】</span>被举报涉嫌违规，经核实为" + juBaoMsg.getReason() + "的原因，" +
                    "我们已经将它暂时隐藏，请您及时修改后重新发布";
            sendMessage.sendSysMsgForDaHui(juBaoMsg.getBeUid(), beContent);
            //向举报用户发送消息
            content = "你好，你举报的<span style=\"color:blue;\">【" + msg.getTitle() +
                    "】</span>被确认违规，我们已将帖子打回，待该帖主修改重新发布并审核通过后可见，" +
                    "感谢你对三国历史可视化展示平台社区做出的贡献";
            sendMessage.sendSysMsgForJuBao(juBaoMsg.getUid(), content);
            return msgServices.changeMsgStatus(msg);
        } else if (result.contains("删帖")) {
            Integer delete = msgServices.delete(mid);
            if(!result.contains("封禁")){
                //向被举报用户发送消息
                beContent = "您好，您的帖子<span style=\"color:blue;\">【" + msg.getTitle() +
                        "】</span>被举报涉嫌违规，经核实为" + juBaoMsg.getReason() + "的原因，" +
                        "为维护社区环境，我们已删除该贴，希望您遵守社区规则";
                sendMessage.sendSysMsgForDaHui(juBaoMsg.getBeUid(), beContent);
                //向举报用户发送消息
                content = "你好，你举报的<span style=\"color:blue;\">【" + msg.getTitle() +
                        "】</span>被确认违规，我们已删除该帖并警告帖主" +
                        "感谢你对三国历史可视化展示平台社区做出的贡献";
                sendMessage.sendSysMsgForJuBao(juBaoMsg.getUid(), content);
            }
            return (delete > 0) ? Result.deleteSuccess() : Result.deleteError();
        } else {
            msg.setStatus(1);
            return msgServices.changeMsgStatus(msg);
        }
    }

    @Override
    public Result<String> deleteById(String token, Long jid) {
        if (verify.isAdmin(token)) {
            int i = juBaoMsgDao.deleteById(jid);
            if (i > 0) {
                return Result.deleteSuccess("处理成功");
            } else {
                return Result.deleteSuccess("处理失败，请稍后重试");
            }
        }
        return null;
    }

    @Override
    public Result<Integer> deleteMore(List<Long> jids) {
        int i = juBaoMsgDao.deleteBatchIds(jids);
        if (i == jids.size()) {
            return Result.deleteSuccess();
        } else {
            return Result.deleteError("似乎有未删除的元素");
        }
    }

    private List<JuBaoMsg> juBaoFormat(List<JuBaoMsg> list) {
        for (JuBaoMsg juBaoMsg : list) {
            User user = userDao.selectById(juBaoMsg.getUid());
            User beUser = userDao.selectById(juBaoMsg.getBeUid());
            Msg msg = msgDao.selectById(juBaoMsg.getMid());
            if (msg == null) {
                msg = new Msg();
                msg.setTitle("<span style=\"color:red;\">该帖已被删除，标题已无法查看!</span>");
                msg.setContent("<span style=\"color:red;\">该帖已被删除，内容已无法查看!</span>");
            }
            if(user == null){
                user = new User();
                user.setUname("用户已注销");
            }
            if(beUser == null){
                beUser = new User();
                beUser.setUname("用户已注销");
            }
            juBaoMsg.setUser(user);
            juBaoMsg.setBeUser(beUser);
            juBaoMsg.setMsg(msg);
        }
        return list;
    }
}
