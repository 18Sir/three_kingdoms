package com.three_kingdoms.controller;

import com.three_kingdoms.domain.JuBaoComment;
import com.three_kingdoms.services.JuBaoCommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/juBaoComments")
public class JuBaoCommentController {
    @Resource
    private JuBaoCommentService juBaoCommentService;

    @GetMapping("/all")
    public Result<List<JuBaoComment>> findAll(){
        return juBaoCommentService.findAll();
    }

    @PostMapping
    public Result<String> save(@RequestHeader(name = "Authorization") String token,
                               @RequestBody JuBaoComment juBaoComment){
        return juBaoCommentService.save(token, juBaoComment);
    }

    @PutMapping
    public Result<String> updateById(@RequestHeader(name = "Authorization") String token,
                                     @RequestBody JuBaoComment juBaoComment){
        return juBaoCommentService.updateById(token, juBaoComment);
    }

    @DeleteMapping("/{jid}")
    public Result<String> deleteById(@RequestHeader(name = "Authorization") String token,
                                     @PathVariable Long jid){
        return juBaoCommentService.deleteById(token,jid);
    }

    //    删除更多
    @DeleteMapping("/more")
    public Result<Integer> deleteMore(@RequestBody List<Long> jids) {
        return juBaoCommentService.deleteMore(jids);
    }

}
