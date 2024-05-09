package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Msg {
    //帖子id
    @TableId(type = IdType.AUTO)
    private Long mid;
    //帖子所属用户id
    private Long uid;
    //帖子所属用户头像
    @TableField(exist = false)
    private String userAvatar;
    //帖子所属用户名字
    @TableField(exist = false)
    private String userName;
    //帖子浏览量
    private Long visits;
    //帖子点赞数
    private Long likes;
    //点踩数
    private Long unLikes;
    //热度
    private Long hot;
    //帖子分享数
    private long share;
    //帖子的标题
    private String title;
    //帖子的内容
    private String content;
    //帖子发布时间
    private Date time;
    //帖子类型
    private Integer type;
    //帖子状态
    private Integer status;
    //逻辑删除
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;
    //评论数
    @TableField(exist = false)
    private Integer commentNum;
    //包含评论
    @TableField(exist = false)
    private List<Comment> commentList;
    //用户是否已点赞
    @TableField(exist = false)
    private Boolean isLiked;
    //用户是否已点踩
    @TableField(exist = false)
    private Boolean isUnLiked;
}
