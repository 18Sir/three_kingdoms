package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //用户id
    @TableId(type = IdType.AUTO)
    private Long uid;
    //用户名
    private String uname;
    //用户的密码
    private String password;
    //用户的头像
    private String avatar;
    //用户的手机号
    private String email;
    //性别
    private String sex;
    //出生日期
    private Date birth;
    //状态（0正常，1封禁中）
    private Integer status;
    //是否管理员
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private int admin;
    //帖子数量
    @TableField(exist = false)
    private int msgNum;
    //逻辑删除
    @TableLogic(value = "0",delval = "1")
    private Integer isDelete;
    //解封的时间（秒级）
    @TableField(exist = false)
    private String unBlockTime;
    //新消息数
    @TableField(exist = false)
    private Integer newNum;

    public User(String uname, String password) {
        this.uname = uname;
        this.password = password;
    }


}
