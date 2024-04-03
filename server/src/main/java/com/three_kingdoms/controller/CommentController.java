package com.three_kingdoms.controller;

import com.three_kingdoms.domain.Comment;
import com.three_kingdoms.services.CommentServices;
import com.three_kingdoms.util.JWTUtil;
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
    public Result save(@RequestHeader(name = "Authorization") String token,
                       @RequestBody Comment comment){
        comment.setUid(JWTUtil.getTokenUid(token));
        Integer i = commentServices.save(comment);
        if(i > 0){
            return Result.success(ResultCode.SAVE_OK,"评论发表成功",null);
        }else{
            return Result.error(ResultCode.SAVE_ERR,"评论发表失败");
        }
    }

    //删除评论
    @DeleteMapping("/{cid}")
    public Result deleteById(@RequestHeader(name = "Authorization") String token,
                             @PathVariable Long cid){
        Long uid1 = JWTUtil.getTokenUid(token);
        Long uid2 = commentServices.findById(cid).getUid();
        if(uid1.equals(uid2) || verify.isAdmin(token)){
            if(commentServices.delete(cid) > 0){
                return Result.deleteSuccess();
            }else{
                return Result.deleteError();
            }
        }else{
            return Result.error(ResultCode.DELETE_ERR,"您没有权限操作");
        }
    }

    //增加评论点赞数
    @PutMapping("/1/{cid}")
    public Result addCommentLikes(@PathVariable Long cid){
        Comment comment = commentServices.findById(cid);
        if(comment != null){
            comment.setLikes(comment.getLikes()+1);
            if(commentServices.update(comment) > 0)
                return Result.success(ResultCode.UPDATE_OK,"点赞收到啦！",null);
            else
                return Result.error(ResultCode.UPDATE_ERR,"点赞失败了");
        }else{
            return Result.error(ResultCode.UPDATE_ERR,"点赞失败了");
        }
    }

    //减少评论点赞数
    @PutMapping("/0/{cid}")
    public Result reduceCommentLikes(@PathVariable Long cid){
        Comment comment = commentServices.findById(cid);
        if(comment != null){
            comment.setLikes(comment.getLikes()-1);
            if(commentServices.update(comment) > 0)
                return Result.success(ResultCode.UPDATE_OK,"点赞减少了",null);
            else
                return Result.error(ResultCode.UPDATE_ERR,"失败了");
        }else{
            return Result.error(ResultCode.UPDATE_ERR,"失败了");
        }
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
