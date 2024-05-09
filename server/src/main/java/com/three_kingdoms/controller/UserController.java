package com.three_kingdoms.controller;

import cn.hutool.crypto.SecureUtil;
import com.three_kingdoms.domain.MessageType;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.services.UserServices;
import com.three_kingdoms.util.JWTUtil;
import com.three_kingdoms.util.Verify;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Resource
    private UserServices userServices;
    @Resource
    private Verify verify;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public Result<User> getById(@RequestHeader(name = "Authorization") String token) {
        Long uid = JWTUtil.getTokenUid(token);
        User user = userServices.findById(uid);
        user.setNewNum(userServices.hasNewMessage(token).getData());
        if (user != null) {
            return Result.selectSuccess(user);
        } else {
            return Result.error(ResultCode.SELECT_ERR, "该用户不存在");
        }

    }

    @GetMapping("/{uid}")
    public Result<User> getAnotherById(@PathVariable Long uid) {
        return Result.selectSuccess(userServices.findById(uid));
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
    public Result<User> updateById(@RequestHeader(name = "Authorization") String token, @NotNull @RequestBody User user) {
        return userServices.update(token, user);
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
                String key = "three-kingdoms:user:" + loginUser.getUid() + ":token";
                redisTemplate.opsForValue().set(key, token, 7, TimeUnit.DAYS);
                return Result.success(ResultCode.Login_OK, "登录成功", token);
            } else {
                return Result.error(ResultCode.Login_ERR, "用户名或密码错误");
            }
        } else {
            return Result.error(ResultCode.Login_ERR, "用户名不存在");
        }
    }

    //上传头像接口

    @CrossOrigin
    @PostMapping("/upload")
    public Result<String> uploadAvatar(@RequestHeader(name = "Authorization") String token, @NotNull MultipartFile file) {
        return userServices.updateAvatar(token, file);
    }

    //发送邮件接口
    @PostMapping("/email/send")
    public Result<String> sendEmailCode(@RequestHeader(name = "Authorization") String token,
                                        @Email String email) {
        return userServices.sendEmailCode(token, email);
    }

    //验证邮件接口
    @GetMapping("/email/verify")
    public Result<Boolean> verifyEmailCode(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam String code) {
        return userServices.verifyEmailCode(token, code);
    }

    //验证邮箱验证码，成功后更新邮箱
    @GetMapping("/email/vwu")
    public Result<Boolean> verifyEmailCodeWithUpdate(@RequestHeader(name = "Authorization") String token,
                                                    @RequestParam String code,
                                                    @Email @RequestParam String email){
        return userServices.verifyEmailCodeWithUpdate(token, code, email);
    }

    //修改密码
    @PostMapping("/password")
    public Result<String> bindEmail(@RequestHeader(name = "Authorization") String token,
                                    String password) {
        return userServices.updatePassword(token, password);
    }

    //验证密码
    @PostMapping("/password/verify")
    public Result<String> verifyPassword(@RequestHeader(name = "Authorization") String token,
                                         String password) {
        return userServices.verifyPassword(token, password);
    }

    @PostMapping("/block")
    public Result<String> blockUser(@RequestHeader(name = "Authorization") String token,
                                    @RequestParam Long blockUid,
                                    @RequestParam Long time,
                                    @RequestParam String reason){
        return userServices.blockUser(token, blockUid, time, reason);
    }

    @DeleteMapping("/block/{blockUid}")
    public Result<String> unBlockUser(@RequestHeader(name = "Authorization") String token,
                                      @PathVariable Long blockUid){
        return userServices.unBlockUser(token, blockUid);
    }

    @GetMapping("/block")
    public Result<Long> blockUserTTC(@RequestHeader(name = "Authorization") String token){
        return userServices.blockUserTTC(token);
    }

    //向用户发送信息
    @PostMapping("/message/send")
    Result<String> sendUserMessage(@RequestParam Long uid,
                                   @RequestParam String title,
                                   @RequestParam String content,
                                   @RequestParam MessageType type){
        return userServices.sendUserMessage(uid, title, content, type);
    }
    //用户删除信息
    @DeleteMapping("/message")
    Result<String> userDelMessage(@RequestHeader(name = "Authorization") String token,
                                  @RequestParam String hashKey,
                                  @RequestParam MessageType type){
        return userServices.userDelMessage(token, hashKey, type);
    }
    //新消息提示
    @GetMapping("/message/alert")
    Result<Integer> hasNewMessage(@RequestHeader(name = "Authorization") String token){
        return userServices.hasNewMessage(token);
    }
    //用户访问自己的消息
    @GetMapping("/message/{type}")
    Result<Map<String, Object>> userGetMessage(@RequestHeader(name = "Authorization") String token,
                                               @PathVariable MessageType type){
        return userServices.userGetMessage(token,type);
    }

}
