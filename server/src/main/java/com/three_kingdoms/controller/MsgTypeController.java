package com.three_kingdoms.controller;

import com.three_kingdoms.domain.MsgType;
import com.three_kingdoms.services.MsgTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/msgTypes")
public class MsgTypeController {
    @Resource
    private MsgTypeService msgTypeService;

    //    查询所有的类型
    @GetMapping()
    public Result<List<MsgType>> findAllType(){
        return msgTypeService.findAllType();
    }

    //    增加类型
    @PostMapping()
    public Result<Integer> saveType(@RequestBody MsgType msgType){
        return msgTypeService.saveType(msgType);
    }

    //    删除类型
    @DeleteMapping("/{tid}")
    public Result<Integer> deleteType(@PathVariable Long tid){
        return msgTypeService.deleteType(tid);
    }

    //    删除多个类型
    @DeleteMapping()
    public Result<Integer> deleteMoreType(@RequestBody List<Long> tidList){
        return msgTypeService.deleteMoreType(tidList);
    }

    //    修改类型
    @PutMapping()
    public Result<Integer> updateType(@RequestBody MsgType msgType){
        return msgTypeService.updateType(msgType);
    }
}
