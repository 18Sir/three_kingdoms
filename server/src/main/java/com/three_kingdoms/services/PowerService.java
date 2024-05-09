package com.three_kingdoms.services;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.domain.Power;

import java.util.List;

public interface PowerService {
    //查找全部
    public List<Power> findPowerAll();
    //按id查找
    public Power findPowerById(Long pid);
    //增加
    public Integer savePower(Power power);
    //修改
    public Integer updatePower(Power power);
    //删除
    public Integer delPower(Long pid);
    //删除多个
    public Result<Integer> delPowerMore(List<Long> pids);

    //以下为redis形式

}
