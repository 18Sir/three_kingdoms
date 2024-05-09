package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ju_bao_msg")
public class JuBaoMsg {
    @TableId(type = IdType.AUTO)
    private Long jid;
    //举报的用户id
    private Long uid;
    //被举报的用户id
    private Long beUid;
    //举报的帖子id
    private Long mid;
    //举报的理由
    private String reason;
    //详细描述
    private String detail;
    //处理结果
    private String result;
    //举报的用户
    @TableField(exist = false)
    private User user;
    //被举报的用户
    @TableField(exist = false)
    private User beUser;
    //被举报的帖子
    @TableField(exist = false)
    private Msg msg;
    //逻辑删除
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;
}
