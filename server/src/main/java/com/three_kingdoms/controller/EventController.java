package com.three_kingdoms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Event;
import com.three_kingdoms.services.impl.EventServicesImpl;
import com.three_kingdoms.util.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventServicesImpl eventServices;
    @Autowired
    private Verify verify;

    //分页查询所有事件信息
    @GetMapping
    public Result findAllToPage(@RequestParam Long current,
                                @RequestParam Long size) {
        Page p = new Page(current, size);
        IPage<Event> page = eventServices.findAllToPage(p);
        if (page.getRecords() != null) {
            return Result.selectSuccess(page);
        } else {
            return Result.selectError();
        }
    }

    //查询所有事件信息
    @GetMapping("/all")
    public Result fndAllEvents() {
        List<Event> eventList = eventServices.findAll();
        if (eventList.size() > 0) {
            return Result.selectSuccess(eventList);
        } else {
            return Result.selectError();
        }
    }

    //按事件名查询事件信息
    @GetMapping("/n/{eName}")
    public Result findAllByName(@PathVariable String eName, @RequestParam Long current,
                                @RequestParam Long size) {
        Page p = new Page(current, size);
        IPage page = eventServices.findAllByName(eName, p);
        if (page != null) {
            return Result.selectSuccess(page);
        } else {
            return Result.selectError();
        }
    }

    //查询单个事件详细信息
    @GetMapping("/{eid}")
    public Result<Event> findById(@PathVariable Long eid) {
        Event event = eventServices.findById(eid);
        if (event != null) {
            return Result.selectSuccess(event);
        } else {
            return Result.selectError();
        }
    }

    //增加事件
    @PostMapping
    public Result<Event> save(@RequestHeader(name = "Authorization") String token, @RequestBody Event event) {
        if (verify.isAdmin(token)) {
            Integer i = eventServices.save(event);
            if (i > 0) {
                return Result.saveSuccess();
            } else {
                return Result.saveError();
            }
        } else {
            return Result.error(ResultCode.SAVE_ERR, "无权操作");
        }

    }

    //修改事件
    @PutMapping
    public Result update(@RequestHeader(name = "Authorization") String token, @RequestBody Event event) {
        if (verify.isAdmin(token)) {
            Integer i = eventServices.update(event);
            if (i > 0) {
                return Result.updateSuccess();
            } else {
                return Result.updateError();
            }
        } else {
            return Result.error(ResultCode.UPDATE_ERR, "无权操作");
        }
    }

    //删除事件
    @DeleteMapping("/{eid}")
    public Result delete(@RequestHeader(name = "Authorization") String token, @PathVariable Long eid) {
        if (verify.isAdmin(token)) {
            Integer i = eventServices.delete(eid);
            if (i > 0) {
                return Result.deleteSuccess();
            } else {
                return Result.deleteError();
            }
        } else {
            return Result.error(ResultCode.DELETE_ERR, "无权操作");
        }
    }

    //删除多个事件
    @DeleteMapping("/more")
    public Result deleteMore(@RequestHeader(name = "Authorization") String token, @RequestBody List<Long> eids) {
        System.out.println(eids);
        if (verify.isAdmin(token)) {
            Integer i = eventServices.deleteMore(eids);
            if (i > 0) {
                return Result.deleteSuccess();
            } else {
                return Result.deleteError();
            }
        } else {
            return Result.error(ResultCode.DELETE_ERR, "无权操作");
        }
    }
}
