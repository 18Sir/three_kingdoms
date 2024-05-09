package com.three_kingdoms.services.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.MessageType;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.exception.SystemException;
import com.three_kingdoms.services.UserServices;
import com.three_kingdoms.util.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServicesImpl implements UserServices {
    @Resource
    private UserDao userDao;

    @Value("${web.imageUrl}")
    private String imageUrl;

    @Autowired
    private EmailCode emailCode;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Verify verify;

    @Resource
    private SendMessage sendMessage;

    @Override
    public User findById(Long id) {
        User user = userDao.selectById(id);
        user.setPassword("用户密码不可见");
        return checkBlock(user);
    }

    @Override
    public User findByName(String name) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("uname", name);
        User user = checkBlock(userDao.selectOne(qw));
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = userDao.selectList(null);
        for (User user : userList) {
            user = checkBlock(user);
        }
        return userList;
    }

    //检查封禁，如果已解封则更新用户状态
    private User checkBlock(User user){
        if(user == null || user.getUid() == null){
            return user;
        }
        String blockKey = "three-kingdoms:block:"+user.getUid();
        Long expire = redisTemplate.getExpire(blockKey);
        if(expire == -2){
            if(user.getStatus() > 0){
                user.setStatus(0);
                userDao.updateById(user);
            }
        } else if (expire == -1) {
            user.setUnBlockTime("永久");
        } else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(System.currentTimeMillis()+expire * 1000);
            user.setUnBlockTime(time);
        }
        return user;
    }

    @Override
    public Integer save(User user) {
        //加密
        String md5pw = SecureUtil.md5(user.getPassword());
        user.setPassword(md5pw);
        //添加
        return userDao.insert(user);
    }

    @Override
    public Integer delete(Long id) {
        return userDao.deleteById(id);
    }

    @Override
    public Result<User> update(String token, User user) {
        Long uid = JWTUtil.getTokenUid(token);
        User u = findById(uid);
        if (u != null) {
            if (findByName(user.getUname()) == null || user.getUname().equals(u.getUname())) {
                user.setPassword(null);
                int res = userDao.updateById(user);
                if (res > 0) {
                    return Result.updateSuccess("信息修改成功");
                } else {
                    return Result.updateError("信息修改失败，请稍后重试");
                }
            } else {
                return Result.updateError("修改的用户名已存在！");
            }

        } else {
            return Result.updateError("用户不存在");
        }
    }

    @Override
    public Result<String> updateAvatar(String token, MultipartFile file) {
        String fileName;
        String name = file.getOriginalFilename();
        if (name != null) {
            //保证文件名字是唯一的，防止文件覆盖
            fileName = UUID.randomUUID() + name.substring(name.lastIndexOf("."));
        } else {
            return Result.error(ResultCode.SAVE_ERR, "头像上传失败");
        }
        StringBuilder path = new StringBuilder("D:\\毕设\\avatarImages\\");
        path.append(fileName);
        //存储到本地磁盘
        try {
            file.transferTo(new File(path.toString()));
            User user = new User();
            user.setUid(JWTUtil.getTokenUid(token));
            user.setAvatar(imageUrl + fileName);
            userDao.updateById(user);
            if (userDao.updateById(user) > 0) {
                return Result.success(ResultCode.SAVE_OK, "头像上传成功", user.getAvatar());
            } else {
                return Result.updateError("头像上传失败");
            }

        } catch (IOException e) {
            throw new SystemException(ResultCode.SAVE_ERR, "头像上传失败");
        }
    }

    @Override
    public Result<String> sendEmailCode(String token, String email) {
        Long uid = JWTUtil.getTokenUid(token);
        User user = userDao.selectById(uid);
        String oldEmail = user.getEmail();
        //判断是否是用户自己的邮箱,是则只发送验证码
        if (oldEmail != null && oldEmail.equals(email)) {
            emailCode.sendCode(user, email);
            return Result.success(ResultCode.SAVE_OK, "验证码发送成功，请前往邮箱查看", null);
        }else{
            //不是则代表注册或修改邮箱，需要校验
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.select("email");
            qw.eq("email", email);
            List<User> users = userDao.selectList(qw);
            if (!users.isEmpty() && users != null) {
                return Result.error(ResultCode.BUSINESS_ERR, "注册的邮箱已存在");
            }else {
                emailCode.sendCode(user, email);
                return Result.success(ResultCode.SAVE_OK, "验证码发送成功，请前往邮箱查看", null);
            }
        }
    }

    @Override
    public Result<Boolean> verifyEmailCode(String token, String code) {
        User user = userDao.selectById(JWTUtil.getTokenUid(token));
        String oldCode = emailCode.getCode(user);
        if ("".equals(oldCode)) {
            return Result.error(ResultCode.SELECT_ERR,
                    false, "验证码已失效，请重新发送");
        } else if (oldCode.equals(code)) {
            return Result.selectSuccess(true, "验证码校验成功");
        } else {
            return Result.error(ResultCode.SELECT_ERR, false, "验证码错误");
        }
    }

    @Override
    public Result<Boolean> verifyEmailCodeWithUpdate(String token, String code,
                                                     String email) {
        Boolean b = verifyEmailCode(token, code).getData();
        if (b) {
            User user = userDao.selectById(JWTUtil.getTokenUid(token));
            String msg = "邮箱换绑成功";
            if (user.getEmail() == null) {
                msg = "邮箱绑定成功";
            }
            user.setEmail(email);
            int i = userDao.updateById(user);
            if (i > 0) {
                return Result.success(ResultCode.UPDATE_OK, msg, true);
            } else {
                return Result.updateError("邮箱绑定失败");
            }
        } else {
            return Result.updateError("验证码校验失败");
        }
    }

    @Override
    public Result<String> updatePassword(String token, String pw) {
        User user = userDao.selectById(JWTUtil.getTokenUid(token));
        String md5pw = SecureUtil.md5(pw);
        user.setPassword(md5pw);
        int i = userDao.updateById(user);
        if (i > 0) {
            String key = "three-kingdoms:" + user.getUid() + ":token";
            redisTemplate.delete(key);
            return Result.updateSuccess("密码修改成功");
        } else {
            return Result.updateError("密码修改失败，请稍后重试");
        }
    }

    @Override
    public Result<String> verifyPassword(String token, String password) {
        User user = userDao.selectById(JWTUtil.getTokenUid(token));
        String newPassword = SecureUtil.md5(password);
        if (user.getPassword().equals(newPassword)) {
            return Result.selectSuccess(null, "密码正确");
        } else {
            return Result.selectError("密码错误");
        }
    }

    @Override
    public Result<String> blockUser(String token, Long blockUid, Long time, String reason) {
        //先判断是不是管理员
        if(verify.isAdmin(token)){
            //然后添加进小黑屋
            String blockKey = "three-kingdoms:block:"+blockUid;
            redisTemplate.opsForValue().set(blockKey,reason,time,TimeUnit.SECONDS);
            User user = userDao.selectById(blockUid);
            user.setStatus(1);
            userDao.updateById(user);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String unBlockTime = sdf.format(System.currentTimeMillis()+time * 1000);
            reason += "对您做出封禁处罚，解封时间："+unBlockTime + "，封禁期间无法发布帖子与评论。";
            sendMessage.sendSysMsgForWeiGui(user.getUid(),reason);
            return Result.saveSuccess("封禁成功");
        }else{
            return Result.saveError("您没有这个权限");
        }
    }

    @Override
    public Result<String> unBlockUser(String token, Long blockUid) {
        if(verify.isAdmin(token)){
            String blockKey = "three-kingdoms:block:"+blockUid;
            Boolean delete = redisTemplate.delete(blockKey);
            if(delete){
                User user = userDao.selectById(blockUid);
                user.setStatus(0);
                userDao.updateById(user);
                sendMessage.sendSysMsg(user.getUid(),"解封通知",
                        "您的封禁已解除，可以正常发表帖子和评论了！");
                return Result.deleteSuccess("封禁已解除");
            }else{
                return Result.deleteSuccess("解除失败，请稍后重试");
            }
        }else{
            return Result.saveError("您没有这个权限");
        }
    }

    @Override
    public Result<Long> blockUserTTC(String token) {
        Long blockUid = JWTUtil.getTokenUid(token);
        String blockKey = "three-kingdoms:block:"+blockUid;
        Long expire = redisTemplate.getExpire(blockKey);
        return Result.selectSuccess(expire == null ? 0 : expire);
    }

    //向用户发送信息
    public Result<String> sendUserMessage(Long uid, String title, String content, MessageType type){
        return sendMessage.sendSysMsg(uid,title,content);
    }

    //用户删除信息
    public Result<String> userDelMessage(String token,String hashKey,MessageType type){
        Long uid = JWTUtil.getTokenUid(token);
        String messageKey = "three-kingdoms:user:"+uid+":message:"+type;
        redisTemplate.opsForHash().delete(messageKey,hashKey);
        return Result.deleteSuccess("消息删除成功");
    }

    //新消息提示
    public Result<Integer> hasNewMessage(String token){
        Long uid = JWTUtil.getTokenUid(token);
        String newMessageKey = "three-kingdoms:user:"+uid+":message:new:"+MessageType.SYSTEM;
        Map newMessage = redisTemplate.opsForHash().entries(newMessageKey);
        Integer newNum = newMessage == null ? 0 : newMessage.size();
        return Result.selectSuccess(newNum);
    }

    //用户访问自己的消息
    public Result<Map<String, Object>> userGetMessage(String token,MessageType type){
        Long uid = JWTUtil.getTokenUid(token);
        String newMessageKey = "three-kingdoms:user:"+uid+":message:new:"+type;
        String messageKey = "three-kingdoms:user:"+uid+":message:"+type;
        Map newMessage = redisTemplate.opsForHash().entries(newMessageKey);
        Map Message = redisTemplate.opsForHash().entries(messageKey);
        if(newMessage != null){
            redisTemplate.delete(newMessageKey);
            redisTemplate.opsForHash().putAll(messageKey,newMessage);
        }else{
            newMessage = new LinkedHashMap();
        }
        Map<String,Object> userMessage = new LinkedHashMap<>();
        userMessage.put("new",MapSort.sortMapByDesc(newMessage));
        userMessage.put("history",MapSort.sortMapByDesc(Message));
        userMessage.put("newNum",newMessage.size());
        return Result.selectSuccess(userMessage);
    }

}
