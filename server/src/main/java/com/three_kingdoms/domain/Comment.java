package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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
    private int likes;
    //评论发布时间
    private Date createTime;
}
