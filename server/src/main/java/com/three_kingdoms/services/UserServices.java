package com.three_kingdoms.services;

import com.three_kingdoms.domain.User;

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
    public Integer update(User user);

}
