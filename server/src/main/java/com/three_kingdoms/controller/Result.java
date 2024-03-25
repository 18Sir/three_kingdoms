package com.three_kingdoms.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private ResultCode code;
    private String msg;
    private T data;

    public static <E> Result<E> success(ResultCode code,String msg,E data){
        return new Result<>(code,msg,data);
    }

    public static <E> Result<E> selectSuccess(E data){
        return new Result<>(ResultCode.SELECT_OK,"查询成功",data);
    }

    public static <E> Result<E> updateSuccess(){
        return new Result<>(ResultCode.UPDATE_OK,"更新成功",null);
    }

    public static <E> Result<E> saveSuccess(){
        return new Result<>(ResultCode.SAVE_OK,"添加成功",null);
    }

    public static <E> Result<E> deleteSuccess(){
        return new Result<>(ResultCode.DELETE_OK,"删除成功",null);
    }

    public static <E> Result<E> selectError(){
        return new Result<>(ResultCode.SELECT_ERR,"查询失败",null);
    }

    public static <E> Result<E> saveError(){
        return new Result<>(ResultCode.SAVE_ERR,"添加失败",null);
    }

    public static <E> Result<E> deleteError(){
        return new Result<>(ResultCode.DELETE_ERR,"删除失败",null);
    }

    public static <E> Result<E> updateError(){
        return new Result<>(ResultCode.UPDATE_ERR,"修改失败",null);
    }

    public static <E> Result<E> error(ResultCode code,String msg){
        return new Result<>(code,msg,null);
    }
}
