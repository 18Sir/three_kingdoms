package com.three_kingdoms.services.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.three_kingdoms.dao.UserDao;
import com.three_kingdoms.domain.User;
import com.three_kingdoms.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServicesImpl implements UserServices {
    @Autowired
    private UserDao userDao;

    @Override
    public User findById(Long id) {
        User user = userDao.selectById(id);
        user.setPassword("用户密码不可见");
        return user;
    }

    @Override
    public User findByName(String name) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("uname",name);
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
    public Integer update(User user) {
        return userDao.updateById(user);
    }



}
