package com.three_kingdoms.util;

import cn.hutool.core.util.RandomUtil;
import com.three_kingdoms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class EmailCode {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    // 获取发件人邮箱
    @Value("${spring.mail.username}")
    private String sender;

    // 获取发件人昵称
    @Value("${spring.mail.nickname}")
    private String nickname;

    public void sendCode(User user, String email) {
        String key = "three-kingdoms:user:"+user.getUid()+":emailCode";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(nickname + '<' + sender + '>');
        message.setTo(email);
        message.setSubject("欢迎访问三国历史可视化展示平台");

        // 使用 hutool-all 生成 6 位随机数验证码
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);
            String content = "【验证码】您的验证码为：" + code + " 。 验证码五分钟内有效，逾期作废。\n\n\n" +
                    "------------------------------\n\n\n" +
                    "若不是您本人操作，请无视此信息\n\n";
            message.setText(content);
            mailSender.send(message);
    }

    public String getCode(User user){
        String key = "three-kingdoms:user:"+user.getUid()+":emailCode";
        Object o = redisTemplate.opsForValue().get(key);
        if(o == null){
            return "";
        }
        return o.toString();
    }
}
