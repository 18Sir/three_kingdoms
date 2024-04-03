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
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    //人物id
    @TableId(type = IdType.AUTO)
    private Long aid;
    //人物名
    private String aFname;
    //人物字
    private String aTname;
    //人物出生时间
    private String bTime;
    //人物过世时间
    private String dTime;
    //人物出生地id
    private Long addrId;
    //人物出生地对象
    @TableField(exist = false)
    private Addr addr;
    //人物参与事件id组
    private String events;
    //事件对象
    @TableField(exist = false)
    private List<Event> eventList;
    //人物的生平事迹
    private String story;
    //人物的所属势力
    private String power;
    //逻辑删除
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

}
