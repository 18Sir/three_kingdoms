package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    //评论id
    @TableId(type = IdType.AUTO)
    private Long cid;
    //评论所属帖的id
    private Long mid;
    //评论用户id
    private Long uid;
    //评论用户名称
    @TableField(exist = false)
    private String uname;
    //评论用户头像
    @TableField(exist = false)
    private String avatar;
    //评论内容
    private String content;
    //评论的点赞数
    private Long likes;
    //评论发布时间
    private Date createTime;
    //评论层级（0级为直接回复帖子的，0级以上为回复评论的评论）
    private Long height;
    //评论被回复的用户id
    private Long replyUid;
    //点踩数
    private Long unLikes;
    //回复数
    private Long replyNum;
    //热度
    private Long hot;
    //被回复的用户
    @TableField(exist = false)
    private User replyUser;
    //回复的所有评论
    @TableField(exist = false)
    private List<Comment> commentList;
    //要回复的评论id
    private Long beCid;
    //用户是否已点赞
    @TableField(exist = false)
    private Boolean isLiked;
    //用户是否已点踩
    @TableField(exist = false)
    private Boolean isUnLiked;
}
