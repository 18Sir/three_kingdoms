package com.three_kingdoms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.domain.Msg;
import com.three_kingdoms.services.MsgServices;
import com.three_kingdoms.util.JWTUtil;
import com.three_kingdoms.util.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/msgs")
public class MsgController {
    @Autowired
    private MsgServices msgServices;
    @Autowired
    private Verify verify;

    //分页获取全部帖子
    @GetMapping("/all")
    public Result getAllToPage(@RequestParam Long current, @RequestParam Long size) {

        Page p = new Page(current, size);
        IPage page = msgServices.findAll(p);
        if (page.getRecords().size() > 0) {
            return Result.selectSuccess(page);
        }
        return Result.error(ResultCode.SELECT_ERR, "暂无帖子");
    }

    //直接获取所有帖子
    @GetMapping("")
    public Result<List<Msg>> getAllNoPage() {
        return msgServices.findAll();
    }

    //根据id查找帖子
    @GetMapping("/m/{mid}")
    public Result findByMid(@PathVariable Long mid) {
        Map map = msgServices.findByMid(mid);
        if (map != null)
            return Result.selectSuccess(map);
        else
            return Result.error(ResultCode.SELECT_ERR, "帖子不存在");
    }

    //查找帖子并增加1访问量
    @GetMapping("/m/v/{mid}")
    public Result<Map> findByMidWithViews(@RequestHeader(name = "Authorization") String token,
                                          @PathVariable Long mid) {
        return msgServices.findByIdWithViews(token, mid);
    }

    //根据用户id查找帖子
    @GetMapping("/u/{uid}")
    public Result findByUid(@PathVariable Long uid) {
        List<Msg> msgs = msgServices.findByUid(uid);
        return Result.selectSuccess(msgs);
    }

    //查找自己的帖
    @GetMapping("/u")
    public Result findByUid(@RequestHeader(name = "Authorization") String token) {
        Long uid = JWTUtil.getTokenUid(token);
        List<Msg> msgs = msgServices.findByUid(uid);
        return Result.selectSuccess(msgs);
    }

    //根据标题查找帖子
    @GetMapping("/t/{title}")
    public Result findByTitle(@RequestParam Long current, @RequestParam Long size,
                              @PathVariable String title) {
        Page p = new Page(current, size);
        IPage page = msgServices.findByTitle(title, p);
        if (page.getRecords().size() > 0) {
            return Result.selectSuccess(page);
        }
        return Result.error(ResultCode.SELECT_ERR, "暂无帖子");
    }

    //根据类型筛选帖子
    @GetMapping("/t")
    public Result findByType(@RequestParam Long current, @RequestParam Long size,
                             @RequestParam Integer type) {
        Page p = new Page(current, size);
        IPage page = msgServices.findByType(p, type);
        if (page.getRecords().size() > 0) {
            return Result.selectSuccess(page);
        }
        return Result.error(ResultCode.SELECT_ERR, "暂无帖子");
    }

    //查找帖子的评论
    @GetMapping("/c/{mid}")
    public Result getMsgComment(@PathVariable Long mid) {
        List<Comment> list = msgServices.getMsgComment(mid);
        if (list.size() > 0) {
            return Result.selectSuccess(list);
        } else
            return Result.error(ResultCode.SELECT_ERR, "该帖尚无评论");
    }

    //返回给用户端的帖子的评论
    @GetMapping("/c/u/{mid}")
    public Result<List<Comment>> getMsgCommentForUser(@RequestHeader(name = "Authorization") String token,
                                                      @PathVariable Long mid,
                                                      @RequestParam String sort) {
        return msgServices.getMsgCommentForUser(token, mid, sort);
    }

    //添加帖子
    @PostMapping
    public Result save(@RequestHeader(name = "Authorization") String token, @RequestBody Msg msg) {
        return msgServices.save(token, msg);
    }

    //删除帖子
    @DeleteMapping("/{mid}")
    public Result delete(@RequestHeader(name = "Authorization") String token, @PathVariable Long mid) {
        Long uid1 = JWTUtil.getTokenUid(token);
        if (msgServices.findByMidToMsg(mid) != null) {
            Long uid2 = msgServices.findByMidToMsg(mid).getUid();
            if (uid1.equals(uid2) || verify.isAdmin(token)) {
                Integer i = msgServices.delete(mid);
                if (i > 0)
                    return Result.deleteSuccess();
                else
                    return Result.deleteError();
            } else {
                return Result.error(ResultCode.DELETE_ERR, "您没有权限这样操作");
            }
        } else {
            return Result.error(ResultCode.SELECT_ERR, "暂无该帖信息");
        }
    }

    //    删除更多
    @DeleteMapping("/more")
    public Result<Integer> deleteMore(@RequestBody List<Long> mids) {
        return msgServices.deleteMore(mids);
    }

    //更新帖子
    @PutMapping
    public Result update(@RequestBody Msg msg) {
        Integer i = msgServices.update(msg);
        if (i > 0) {
            return Result.updateSuccess();
        } else
            return Result.updateError();
    }

    @GetMapping("/checked/all")
    //分页获取所有已审核的帖子
    public Result<IPage<Msg>> findAllCheckedMsg(@RequestParam Long current,
                                                @RequestParam Long size,
                                                @RequestParam String sort) {
        IPage<Msg> page = new Page<>(current, size);
        return msgServices.findAllCheckedMsg(page, sort);
    }

    //根据类型筛选所有已审核的帖子
    @GetMapping("/checked/type")
    Result<IPage<Msg>> findAllCheckedByType(@RequestParam Long current,
                                            @RequestParam Long size,
                                            @RequestParam Integer type,
                                            @RequestParam String sort) {
        IPage<Msg> page = new Page<>(current, size);
        return msgServices.findAllCheckedByType(page, type, sort);
    }

    //根据标题名查找所有已审核的帖子
    @GetMapping("/checked/title")
    public Result<IPage<Msg>> findAllCheckedByTitle(@RequestParam Long current,
                                                    @RequestParam Long size,
                                                    @RequestParam String title,
                                                    @RequestParam String sort) {
        IPage<Msg> page = new Page<>(current, size);
        return msgServices.findAllCheckedByTitle(page, title, sort);
    }

    //点赞相关
    @PutMapping("/likes")
    public Result<Long> likes(@RequestHeader(name = "Authorization") String token,
                              @RequestParam Long mid,
                              @RequestParam Boolean b) {
        return msgServices.likes(token, mid, b);
    }

    //点踩相关
    @PutMapping("/un/likes")
    public Result<Long> unLikes(@RequestHeader(name = "Authorization") String token,
                                @RequestParam Long mid,
                                @RequestParam Boolean b) {
        return msgServices.unLikes(token, mid, b);
    }

    //分享相关
    @PutMapping("/shares")
    public Result<Long> share(@RequestHeader(name = "Authorization") String token,
                              @RequestParam Long mid) {
        return msgServices.share(token, mid);
    }

    //更改帖子审核状态接口
    @PutMapping("/status")
    public Result<String> changeMsgStatus(@RequestBody Msg msg) {
        return msgServices.changeMsgStatus(msg);
    }

}
