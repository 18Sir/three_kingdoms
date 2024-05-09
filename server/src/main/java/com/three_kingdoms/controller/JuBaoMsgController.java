package com.three_kingdoms.controller;

import com.three_kingdoms.domain.JuBaoMsg;
import com.three_kingdoms.services.JuBaoMsgService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/juBaoMsgs")
public class JuBaoMsgController {
    @Resource
    private JuBaoMsgService juBaoMsgService;

    @GetMapping("/all")
    public Result<List<JuBaoMsg>> findAll(){
        return juBaoMsgService.findAll();
    }

    @PostMapping
    public Result<String> save(@RequestHeader(name = "Authorization") String token,
                               @RequestBody JuBaoMsg juBaoMsg){
        return juBaoMsgService.save(token, juBaoMsg);
    }

    @PutMapping
    public Result<String> updateById(@RequestHeader(name = "Authorization") String token,
                                     @RequestBody JuBaoMsg juBaoMsg){
        return juBaoMsgService.updateById(token, juBaoMsg);
    }

    @DeleteMapping("/{jid}")
    public Result<String> deleteById(@RequestHeader(name = "Authorization") String token,
                                     @PathVariable Long jid){
        return juBaoMsgService.deleteById(token,jid);
    }

    //    删除更多
    @DeleteMapping("/more")
    public Result<Integer> deleteMore(@RequestBody List<Long> jids) {
        return juBaoMsgService.deleteMore(jids);
    }

}
