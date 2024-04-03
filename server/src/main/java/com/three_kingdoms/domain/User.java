package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
    //是否管理员
    private int admin;
    //帖子数量
    @TableField(exist = false)
    private int msgNum;
    //逻辑删除
    @TableLogic(value = "0",delval = "1")
    private Integer isDelete;

    public User(String uname, String password) {
        this.uname = uname;
        this.password = password;
    }


}
