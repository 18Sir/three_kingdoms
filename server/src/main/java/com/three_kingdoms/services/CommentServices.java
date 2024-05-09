package com.three_kingdoms.services;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.Comment;

public interface CommentServices {
    //发表评论
    public Result<String> save(String token, Comment c);
    //回复评论
    public Result<String> reply(String token,Comment c);
    //删除评论
    public Result<String> delete(String token, Long cid);
    //更新评论（更新评论的点赞数）
    public Integer update(Comment c);
    //按id查评论
    public Comment findById(Long cid);
    //点赞评论
    Result<Long> likes(String token, Long cid, Boolean b);
    //点踩评论
    Result<Long> unLikes(String token, Long cid, Boolean b);
}
