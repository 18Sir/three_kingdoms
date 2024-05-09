package com.three_kingdoms.services.impl;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.controller.ResultCode;
import com.three_kingdoms.dao.Powerdao;
import com.three_kingdoms.domain.Power;
import com.three_kingdoms.services.PowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PowerServiceImpl implements PowerService {
    @Autowired
    private Powerdao powerDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Power> findPowerAll() {
        List<Power> powerList = powerDao.selectList(null);
        return powerList;
    }

    public void rFindAllPower(){

    }

    public Result<String> rSavePower(Power power){
        String key = "three-kingdoms:power:"+power.getPid();
        Map<String, Object> map = null;
        try {
//            map = power.getPaddr();
            map.put("name",power.getPname());
            redisTemplate.opsForHash().putAll(key,map);
            return Result.saveSuccess("势力信息添加成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
