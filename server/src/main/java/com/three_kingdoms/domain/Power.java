package com.three_kingdoms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
}
