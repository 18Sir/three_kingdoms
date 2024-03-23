package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //用户id
    @TableId(type = IdType.AUTO)
    private long uid;
    //用户名
    private String uname;
    //用户的密码
    private String password;
    //用户的头像
    private String avatar;
    //用户的手机号
    private String email;
    //是否管理员
    private int admin;
    //帖子数量
    @TableField(exist = false)
    private int msgNum;

    public User(String uname, String password) {
        this.uname = uname;
        this.password = password;
    }


}
