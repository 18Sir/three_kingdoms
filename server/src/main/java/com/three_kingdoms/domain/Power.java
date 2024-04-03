package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(autoResultMap = true)
public class Power {
    @TableId(type = IdType.AUTO)
    private Long pid;
    private String pname;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String,Object> paddr;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String,Object> pactor;
    @TableLogic(value = "0",delval = "1")
    private Integer isDelete;
}
