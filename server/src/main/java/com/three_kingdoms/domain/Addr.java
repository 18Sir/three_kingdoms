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
public class Addr {
    //地点id
    @TableId(type = IdType.AUTO)
    private Long addrId;
    //地点名
    private String addrName;
    //所属州
    private String state;
    //所属郡
    private String country;
    //地点详情描述
    private String detail;
    //涉及事件
    private String eids;
    //事件信息
    @TableField(exist = false)
    private List<Event> eventList;
    //子地点
    @TableField(exist = false)
    private List<Addr> children;
    //逻辑删除
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;
}
