package com.three_kingdoms.services;

import com.three_kingdoms.domain.Comment;

public interface CommentServices {
    //发表评论
    public Integer save(Comment c);
    //删除评论
    public Integer delete(Long cid);
    //更新评论（更新评论的点赞数）
    public Integer update(Comment c);
    //按id查评论
    public Comment findById(Long cid);
}
