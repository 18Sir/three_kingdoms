package com.three_kingdoms.services;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserServices {
    //根据id查找用户
    public User findById(Long id);
    //根据用户名查找用户
    public User findByName(String name);
    //查找所有用户
    public List<User> findAll();
    //添加用户
    public Integer save(User user);
    //删除用户
    public Integer delete(Long id);
    //更新用户信息
    public Result<User> update(String token,User user);
    //更新头像
    public Result<String> updateAvatar(String token, MultipartFile file);
    //发送邮箱验证码
    public Result<String> sendEmailCode(String token,String email);
    //校验邮箱验证码
    public Result<Boolean> verifyEmailCode(String token,String code);
    //校验邮箱验证码，成功后更新用户邮箱
    public Result<Boolean> verifyEmailCodeWithUpdate(String token,String code,String email);
    //修改密码
    public Result<String> updatePassword(String token,String pw);
    //验证密码
    Result<String> verifyPassword(String token, String password);
}
