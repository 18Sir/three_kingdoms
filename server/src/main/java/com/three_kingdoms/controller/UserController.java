package com.three_kingdoms.controller;

import cn.hutool.crypto.SecureUtil;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.exception.SystemException;
import com.three_kingdoms.services.impl.UserServicesImpl;
import com.three_kingdoms.util.JWTUtil;
import com.three_kingdoms.util.Verify;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Resource
    private UserServicesImpl userServices;
    @Resource
    private Verify verify;

    UserController() {

    }

    @Value("${web.imageUrl}")
    private String imageUrl;

    @GetMapping
    public Result<User> getById(@RequestHeader(name = "Authorization") String token) {
        Long uid = JWTUtil.getTokenUid(token);
        User user = userServices.findById(uid);
        if (user != null) {
            return Result.selectSuccess(user);
        } else {
            return Result.error(ResultCode.SELECT_ERR, "该用户不存在");
        }

    }

    //获取全部用户（仅管理员可用）
    @GetMapping("/all")
    public Result<List<User>> getAll(@RequestHeader(name = "Authorization") String token) {
        if (verify.isAdmin(token)) {
            List<User> list = userServices.findAll();
            return Result.selectSuccess(list);
        } else {
            return Result.error(ResultCode.SELECT_ERR, "您没有这个权限");
        }

    }

    //用户注册
    @PostMapping("/r")
    public Result<User> save(@Pattern(regexp = "^\\S{2,16}$") String uname,
                             @Pattern(regexp = "^\\S{6,16}$") String password) {
        User user = userServices.findByName(uname);
        if (user == null) {
            User user1 = new User(uname, password);
            int res = userServices.save(user1);
            if (res > 0) {
                return Result.success(ResultCode.SAVE_OK, "注册成功", null);
            } else {
                return Result.saveError();
            }
        } else {
            return Result.error(ResultCode.SAVE_ERR, "用户名已存在");
        }
    }

    @PutMapping
    public Result<User> updateById(@RequestHeader(name = "Authorization") String token, @RequestBody User user) {
        Long uid = JWTUtil.getTokenUid(token);
        if (userServices.findById(uid) != null) {
            if (userServices.findByName(user.getUname()) == null) {
                int res = userServices.update(user);
                if (res > 0) {
                    return Result.updateSuccess();
                } else {
                    return Result.updateError();
                }
            } else {
                return Result.error(ResultCode.UPDATE_ERR, "修改的用户名已存在！");
            }

        } else {
            return Result.error(ResultCode.UPDATE_ERR, "用户不存在");
        }
    }

    //注销其他用户（仅管理员）
    @DeleteMapping("/{uid}")
    public Result<User> deleteById(@RequestHeader(name = "Authorization") String token, @PathVariable Long uid) {
        //判断是否是管理员用户
        if (verify.isAdmin(token)) {
            int res = userServices.delete(uid);
            if (res > 0) {
                return Result.deleteSuccess();
            } else
                return Result.deleteError();
        } else {
            return Result.error(ResultCode.DELETE_ERR, "没有权限操作");
        }
    }

    //注销自己
    @DeleteMapping
    public Result<User> delete(@RequestHeader(name = "Authorization") String token) {
        Long uid = JWTUtil.getTokenUid(token);
        int res = userServices.delete(uid);
        if (res > 0) {
            return Result.deleteSuccess();
        } else
            return Result.deleteError();
    }

    //用户登录
    @PostMapping("/l")
    public Result<String> login(@Pattern(regexp = "^\\S{2,16}$") String uname,
                                @Pattern(regexp = "^\\S{6,16}$") String password) {
        //根据用户名查询用户
        User loginUser = userServices.findByName(uname);
        //判断用户是否存在
        if (loginUser != null) {
            //如果存在判断密码是否正确
            if (SecureUtil.md5(password).equals(loginUser.getPassword())) {
                //分配token信息
                Map<String, Object> claims = new HashMap<>();
                claims.put("uid", loginUser.getUid());
                claims.put("uname", uname);
                String token = JWTUtil.genToken(claims);
                return Result.success(ResultCode.Login_OK, "登录成功", token);
            } else {
                return Result.error(ResultCode.Login_ERR, "用户名或密码错误");
            }
        } else {
            return Result.error(ResultCode.Login_ERR, "用户名不存在");
        }
    }

    //上传头像接口
    @PostMapping("/upload")
    public Result<String> uploadAvatar(@RequestHeader(name = "Authorization") String token, MultipartFile file) {
        String fileName = "";
        String name = file.getOriginalFilename();
        if (name != null) {
            //保证文件名字是唯一的，防止文件覆盖
            fileName = UUID.randomUUID().toString() + name.substring(name.lastIndexOf("."));
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
            userServices.update(user);
            return Result.success(ResultCode.SAVE_OK, "上传成功", user.getAvatar());
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException(ResultCode.SAVE_ERR, "上传失败");
        }
    }

}
