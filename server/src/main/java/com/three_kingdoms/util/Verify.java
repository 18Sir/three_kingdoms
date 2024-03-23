package com.three_kingdoms.util;

import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Verify {
    @Autowired
    private UserDao userDao;

    //判断是否是管理员用户
    public boolean isAdmin(String token){
        Long uid = JWTUtil.getTokenUid(token);
        System.out.println(userDao);
        User user = userDao.selectById(uid);
        if(user.getAdmin() > 0){
            return true;
        }
        return false;
    }

    public boolean isAdmin(Long uid){
        User user = userDao.selectById(uid);
        if(user.getAdmin() > 0){
            return true;
        }
        return false;
    }
}
