package com.three_kingdoms.util;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.MessageType;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class SendMessage {
    @Resource
    private RedisTemplate redisTemplate;

    public Result<String> sendSysMsg(Long uid,String title, String content){
        String messageKey = "three-kingdoms:user:"+uid+":message:new:"+MessageType.SYSTEM;
        String messageId = getNow();
        Map<String,Object> messageValue = new HashMap<>();
        messageValue.put("title",title);
        messageValue.put("content",content);
        messageValue.put("time",getNow());
        redisTemplate.opsForHash().put(messageKey,messageId,messageValue);
        return Result.saveSuccess("信息发送成功");
    }

    public Result<String> sendMsg(Long uid,String title, String content,MessageType type){
        String messageKey = "three-kingdoms:user:"+uid+":message:new:"+type;
        String messageId = getNow();
        Map<String,Object> messageValue = new HashMap<>();
        messageValue.put("title",title);
        messageValue.put("content",content);
        messageValue.put("time",getNow());
        redisTemplate.opsForHash().put(messageKey,messageId,messageValue);
        return Result.saveSuccess("信息发送成功");
    }

    public Result<String> sendSysMsgForJuBao(Long uid, String content){
        String messageKey = "three-kingdoms:user:"+uid+":message:new:"+MessageType.SYSTEM;
        String messageId = getNow();
        Map<String,Object> messageValue = new HashMap<>();
        messageValue.put("title","举报处理结果通知");
        messageValue.put("content",content);
        messageValue.put("time",getNow());
        redisTemplate.opsForHash().put(messageKey,messageId,messageValue);
        return Result.saveSuccess("信息发送成功");
    }

    public Result<String> sendSysMsgForDaHui(Long uid, String content){
        String messageKey = "three-kingdoms:user:"+uid+":message:new:"+MessageType.SYSTEM;
        String messageId = getNow();
        Map<String,Object> messageValue = new HashMap<>();
        messageValue.put("title","帖子审核结果通知");
        messageValue.put("content",content);
        messageValue.put("time",getNow());
        redisTemplate.opsForHash().put(messageKey,messageId,messageValue);
        return Result.saveSuccess("信息发送成功");
    }

    public Result<String> sendSysMsgForWeiGui(Long uid, String content){
        String messageKey = "three-kingdoms:user:"+uid+":message:new:"+MessageType.SYSTEM;
        String messageId = getNow();
        Map<String,Object> messageValue = new HashMap<>();
        messageValue.put("title","违规处罚通知");
        messageValue.put("content",content);
        messageValue.put("time",getNow());
        redisTemplate.opsForHash().put(messageKey,messageId,messageValue);
        return Result.saveSuccess("信息发送成功");
    }

    public Result<String> sendSysMsgForBlock(Long uid, String reason){
        String messageKey = "three-kingdoms:user:"+uid+":message:new:"+MessageType.SYSTEM;
        String messageId = getNow();
        String content = "您好，您因"+reason+"希望您遵守社区规则，良好的社区环境需要大家共同维护";
        Map<String,Object> messageValue = new HashMap<>();
        messageValue.put("title","违规处罚通知");
        messageValue.put("content",content);
        messageValue.put("time",getNow());
        redisTemplate.opsForHash().put(messageKey,messageId,messageValue);
        return Result.saveSuccess("信息发送成功");
    }

    private String getNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }
}
