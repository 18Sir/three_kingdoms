package com.three_kingdoms.services.impl;

import com.three_kingdoms.dao.Powerdao;
import com.three_kingdoms.domain.Power;
import com.three_kingdoms.services.PowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PowerServiceImpl implements PowerService {
    @Autowired
    private Powerdao powerdao;
    @Override
    public List<Power> findPowerAll() {
        List<Power> powerList = powerdao.selectList(null);
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
        Power power = powerdao.selectById(pid);
        return power;
    }

    @Override
    public Integer savePower(Power power) {
        int i = powerdao.insert(power);
        return i;
    }

    @Override
    public Integer updatePower(Power power) {
        int i = powerdao.updateById(power);
        return i;
    }

    @Override
    public Integer delPower(Long pid) {
        int i = powerdao.deleteById(pid);
        return i;
    }
}
