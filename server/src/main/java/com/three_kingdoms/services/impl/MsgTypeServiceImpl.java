package com.three_kingdoms.services.impl;

import com.three_kingdoms.controller.Result;
import com.three_kingdoms.dao.MsgTypeDao;
import com.three_kingdoms.domain.MsgType;
import com.three_kingdoms.services.MsgTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgTypeServiceImpl implements MsgTypeService {
    @Resource
    private MsgTypeDao msgTypeDao;

    @Override
    public Result<List<MsgType>> findAllType() {
        return Result.selectSuccess(msgTypeDao.selectList(null));
    }

    @Override
    public Result<Integer> saveType(MsgType msgType) {
        int i = msgTypeDao.insert(msgType);
        if(i > 0){
            return Result.saveSuccess();
        }else{
            return Result.saveError();
        }
    }

    @Override
    public Result<Integer> deleteType(Long tid) {
        int i = msgTypeDao.deleteById(tid);
        if(i > 0){
            return Result.deleteSuccess();
        }else{
            return Result.deleteError();
        }
    }

    @Override
    public Result<Integer> deleteMoreType(List<Long> tidList) {
        int i = msgTypeDao.deleteBatchIds(tidList);
        if(i == tidList.size()){
            return Result.deleteSuccess("类型全部删除成功");
        }else{
            return Result.deleteError("似乎有未删除的元素");
        }
    }

    @Override
    public Result<Integer> updateType(MsgType msgType) {
        int i = msgTypeDao.updateById(msgType);
        if(i > 0){
            return Result.updateSuccess();
        }else{
            return Result.updateError();
        }
    }
}
