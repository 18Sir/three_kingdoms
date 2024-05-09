package com.three_kingdoms.controller;

import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.services.CommentServices;
import com.three_kingdoms.util.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentServices commentServices;
    @Autowired
    private Verify verify;

    //发表评论
    @PostMapping
    public Result<String> save(@RequestHeader(name = "Authorization") String token,
                       @RequestBody Comment comment){
        return commentServices.save(token, comment);
    }

    //回复评论
    @PostMapping("/reply")
    public Result<String> reply(@RequestHeader(name = "Authorization") String token,
                                @RequestBody Comment comment){
        return commentServices.reply(token, comment);
    }

    //删除评论
    @DeleteMapping("/{cid}")
    public Result deleteById(@RequestHeader(name = "Authorization") String token,
                             @PathVariable Long cid){
        return commentServices.delete(token,cid);
    }

    //点赞逻辑
    @PutMapping("/likes")
    public Result<Long> likes(@RequestHeader(name = "Authorization") String token,
                              @RequestParam Long cid,
                              @RequestParam Boolean b) {
        return commentServices.likes(token, cid, b);
    }
    //点踩逻辑
    @PutMapping("/un/likes")
    public Result<Long> unLikes(@RequestHeader(name = "Authorization") String token,
                              @RequestParam Long cid,
                              @RequestParam Boolean b) {
        return commentServices.unLikes(token, cid, b);
    }


    @GetMapping("/{cid}")
    public Result getById(@PathVariable Long cid){
        Comment comment = commentServices.findById(cid);
        if(comment != null){
            return Result.selectSuccess(comment);
        }else {
            return Result.error(ResultCode.SELECT_ERR,"评论不存在！");
        }
    }
}
