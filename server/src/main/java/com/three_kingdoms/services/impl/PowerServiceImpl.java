package com.three_kingdoms.services.impl;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.dao.Powerdao;
import com.three_kingdoms.domain.Power;
import com.three_kingdoms.services.PowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PowerServiceImpl implements PowerService {
    @Autowired
    private Powerdao powerDao;
    @Override
    public List<Power> findPowerAll() {
        List<Power> powerList = powerDao.selectList(null);
//        for (int i = 0; i < powerList.size(); i++) {
//            Map<String, Object> paddr = powerList.get(i).getPaddr();
//            Map<String, Object> pactor = powerList.get(i).getPactor();
//            Object[] names = pactor.values().toArray();
//            System.out.println(names.toString());
//
//        }
        return powerList;
    }

    @Override
    public Power findPowerById(Long pid) {
        Power power = powerDao.selectById(pid);
        return power;
    }

    @Override
    public Integer savePower(Power power) {
        int i = powerDao.insert(power);
        return i;
    }

    @Override
    public Integer updatePower(Power power) {
        int i = powerDao.updateById(power);
        return i;
    }

    @Override
    public Integer delPower(Long pid) {
        int i = powerDao.deleteById(pid);
        return i;
    }

    @Override
    public Result<Integer> delPowerMore(List<Long> pids) {
        if(pids.isEmpty()){
            return Result.error(ResultCode.BUSINESS_ERR,"未提供要删除的元素");
        }
        Integer i = powerDao.deleteBatchIds(pids);

        if(i == pids.size()){
            return Result.deleteSuccess("全部删除成功");
        }else{
            return Result.deleteError("似乎有未删除的元素");
        }
    }
}
