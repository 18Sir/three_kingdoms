package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    //事件id
    @TableId(type = IdType.AUTO)
    private Long eid;
    //事件名
    private String eName;
    //事件内容
    private String eContent;
    //时间发生时间
    private String eTime;
    //事件的涉及地点
    private String addrs;
    //事件地点的信息
    @TableField(exist = false)
    private List<Addr> addrList;
    //事件涉及人物
    private String aids;
    //事件人物的信息
    @TableField(exist = false)
    private List<Actor> actorList;
    //逻辑删除
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;
}
