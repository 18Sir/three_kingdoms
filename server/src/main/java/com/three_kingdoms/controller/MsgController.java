package com.three_kingdoms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Msg;
import com.three_kingdoms.services.impl.MsgServicesImpl;
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
    private MsgServicesImpl msgServices;
    @Autowired
    private Verify verify;

    //获取全部帖子
    @GetMapping("/all")
    public Result getAll(@RequestParam Long current, @RequestParam Long size) {

        Page p = new Page(current,size);
        IPage page = msgServices.findAll(p);
        if(page.getRecords().size() > 0){
            return Result.selectSuccess(page);
        }
        return Result.error(ResultCode.SELECT_ERR,"暂无帖子");
    }

    //根据id查找帖子
    @GetMapping("/m/{mid}")
    public Result findByMid(@PathVariable Long mid){
        Map map = msgServices.findByMid(mid);
        if(map != null)
            return Result.selectSuccess(map);
        else
            return Result.error(ResultCode.SELECT_ERR,"帖子不存在");
    }

    //根据用户id查找帖子
    @GetMapping("/u/{uid}")
    public Result findByUid(@PathVariable Long uid){
        List<Msg> msgs = msgServices.findByUid(uid);
        if(msgs.size() > 0){
            return Result.selectSuccess(msgs);
        }else
            return Result.error(ResultCode.SELECT_ERR,"该用户还没发过帖子");
    }

    //根据标题查找帖子
    @GetMapping("/t/{title}")
    public Result findByTitle(@RequestParam Long current, @RequestParam Long size,
                              @PathVariable String title){
        Page p = new Page(current,size);
        IPage page = msgServices.findByTitle(title,p);
        if(page.getRecords().size() > 0){
            return Result.selectSuccess(page);
        }
        return Result.error(ResultCode.SELECT_ERR,"暂无帖子");
    }

    //根据类型筛选帖子
    @GetMapping("/t")
    public Result findByType(@RequestParam Long current, @RequestParam Long size,
                             @RequestParam Integer type){
        Page p = new Page(current,size);
        IPage page = msgServices.findByType(p,type);
        if(page.getRecords().size() > 0){
            return Result.selectSuccess(page);
        }
        return Result.error(ResultCode.SELECT_ERR,"暂无帖子");
    }

    //查找帖子的评论
    @GetMapping("/c/{mid}")
    public Result getMsgComment(@PathVariable Long mid){
        List list = msgServices.getMsgComment(mid);
        if(list.size() > 0){
            return Result.selectSuccess(list);
        }else
            return Result.error(ResultCode.SELECT_ERR,"该帖尚无评论");
    }

    //添加帖子
    @PostMapping
    public Result save(@RequestHeader(name = "Authorization") String token,@RequestBody Msg msg){
        msg.setUid(JWTUtil.getTokenUid(token));
        Integer i = msgServices.save(msg);
        if(i > 0){
            return Result.saveSuccess();
        }else
            return Result.saveError();
    }
    //删除帖子
    @DeleteMapping("/{mid}")
    public Result delete(@RequestHeader(name = "Authorization")String token,@PathVariable Long mid){
        Long uid1 = JWTUtil.getTokenUid(token);
        if(msgServices.findByMidToMsg(mid) != null){
            Long uid2 = msgServices.findByMidToMsg(mid).getUid();
            if(uid1.equals(uid2) || verify.isAdmin(token)){
                Integer i = msgServices.delete(mid);
                if(i > 0)
                    return Result.deleteSuccess();
                else
                    return Result.deleteError();
            }else{
                return Result.error(ResultCode.DELETE_ERR,"您没有权限这样操作");
            }
        }else{
            return Result.error(ResultCode.SELECT_ERR,"暂无该帖信息");
        }


    }
    //更新帖子
    @PutMapping
    public Result update(@RequestBody Msg msg){
        Integer i = msgServices.update(msg);
        if(i > 0){
            return Result.updateSuccess();
        }else
            return Result.updateError();
    }
}
