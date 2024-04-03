package com.three_kingdoms.services.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.three_kingdoms.controller.Result;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.exception.SystemException;
import com.three_kingdoms.services.UserServices;
import com.three_kingdoms.util.EmailCode;
import com.three_kingdoms.util.JWTUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    @Override
    public User findById(Long id) {
        User user = userDao.selectById(id);
        user.setPassword("用户密码不可见");
        return user;
    }

    @Override
    public User findByName(String name) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("uname", name);
        User user = userDao.selectOne(qw);
        return user;
    }

    @Override
    public List<User> findAll() {

        List<User> userList = userDao.selectList(null);
        return userList;
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
}
