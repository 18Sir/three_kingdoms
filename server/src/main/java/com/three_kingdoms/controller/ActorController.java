package com.three_kingdoms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Actor;
import com.three_kingdoms.domain.Addr;
import com.three_kingdoms.domain.Event;
import com.three_kingdoms.services.impl.ActorServicesImpl;
import com.three_kingdoms.util.Verify;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actors")
public class ActorController {
    @Resource
    private ActorServicesImpl actorServices;
    @Resource
    private Verify verify;
    //分页查询所有人物
    @GetMapping
    public Result findAllToPage(@RequestParam Long current,
                                @RequestParam Long size) {
        Page page = new Page(current,size);
        IPage iPage = actorServices.findAllToPage(page.getCurrent(), page.getSize());
        if (iPage.getRecords() != null) {
            return Result.selectSuccess(iPage);
        } else {
            return Result.selectError();
        }
    }

    //查询所有人物
    @GetMapping("/all")
    public Result<List<Actor>> findAllActor(){
        List<Actor> allActor = actorServices.findAllActor();
        if (!allActor.isEmpty()) {
            return Result.selectSuccess(allActor);
        } else {
            return Result.selectError();
        }
    }

    //按人物名或字分页查询所有人物
    @GetMapping("/n/{name}")
    public Result findAllByName(@PathVariable String name, @RequestParam Long current,
                                @RequestParam Long size) {
        Page page = new Page(current,size);
        IPage iPage = actorServices.findAllByName(name, page);
        if (iPage.getRecords() != null) {
            return Result.selectSuccess(iPage);
        } else {
            return Result.selectError();
        }
    }

    //按id查询人物
    @GetMapping("/{aid}")
    public Result findById(@PathVariable Long aid) {
        Actor actor = actorServices.findById(aid);
        if (actor != null) {
            return Result.selectSuccess(actor);
        } else {
            return Result.error(ResultCode.SELECT_ERR, "人物不存在");
        }
    }

    //查询人物参与的事件
    @GetMapping("/e/{aid}")
    public Result findActorEvents(@PathVariable Long aid) {
        List<Event> eventList = actorServices.findEvents(aid);
        if (eventList.size() > 0) {
            return Result.selectSuccess(eventList);
        } else {
            return Result.error(ResultCode.SELECT_ERR, "暂无数据");
        }
    }

    //查询人物的出生地
    @GetMapping("/a/{aid}")
    public Result findActorAddr(@PathVariable Long aid) {
        Addr addr = actorServices.findActorAddr(aid);
        if (addr != null) {
            return Result.selectSuccess(addr);
        } else {
            return Result.error(ResultCode.SELECT_ERR, "暂无数据");
        }
    }

    //根据势力范围查找
    @GetMapping("/p/{name}")
    public Result<IPage> findAllByPower(@PathVariable String name,@RequestParam Long current,
                                        @RequestParam Long size,@RequestParam String[] powers){
//        System.out.println(name + "  " + current + "   " + size+"  "+ Arrays.toString(powers));
        IPage p = new Page(current,size);
        if(name.equals("all")){
            name = null;
        }
        IPage page = actorServices.findAllByPower(p, name, powers);
        if(page.getRecords().size() > 0){
            return Result.selectSuccess(page);
        }else{
            return Result.error(ResultCode.SELECT_ERR,"暂无数据");
        }
    }

    //增加一个人物
    @PostMapping
    public Result save(@RequestHeader(name = "Authorization") String token, @RequestBody Actor actor) {
        if (verify.isAdmin(token)) {
            if (actor == null) {
                return Result.error(ResultCode.SAVE_ERR, "请求体不能为空");
            }
            Integer i = actorServices.save(actor);
            if (i > 0) {
                return Result.saveSuccess();
            } else {
                return Result.saveError();
            }
        } else {
            return Result.error(ResultCode.SAVE_ERR, "无权操作");
        }
    }

    //删除一个人物
    @DeleteMapping("/{aid}")
    public Result deleteById(@RequestHeader(name = "Authorization") String token, @PathVariable Long aid) {
        if (verify.isAdmin(token)) {
            Integer i = actorServices.delete(aid);
            if (i > 0) {
                return Result.deleteSuccess();
            } else {
                return Result.deleteError();
            }
        } else {
            return Result.error(ResultCode.DELETE_ERR, "无权操作");
        }
    }

    //删除多个人物
    @DeleteMapping("/more")
    public Result deleteMore(@RequestHeader(name = "Authorization") String token ,@RequestBody List<Long> aids){
//        System.out.println(aids);
        if (verify.isAdmin(token)) {
            Integer i = actorServices.deleteMore(aids);
            if (i > 0) {
                return Result.deleteSuccess();
            } else {
                return Result.deleteError();
            }
        } else {
            return Result.error(ResultCode.DELETE_ERR, "无权操作");
        }
    }

    //修改一个人物
    @PutMapping
    public Result update(@RequestHeader(name = "Authorization") String token, @RequestBody Actor actor) {
        if (verify.isAdmin(token)) {
            System.out.println(actor);
            if (actor == null) {
                return Result.error(ResultCode.SAVE_ERR, "请求体不能为空");
            }
            Integer i = actorServices.update(actor);
            if (i > 0) {
                return Result.updateSuccess();
            } else {
                return Result.updateError();
            }
        } else {
            return Result.error(ResultCode.UPDATE_ERR, "无权操作");
        }
    }
}
