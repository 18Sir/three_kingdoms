package com.three_kingdoms.controller;

import com.three_kingdoms.exception.BusinessException;
import com.three_kingdoms.exception.SystemException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex){
        return Result.error(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException ex){
        return Result.error(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result doPatternException(ConstraintViolationException ex){
        return Result.error(ResultCode.BUSINESS_ERR,"请正确填写内容");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result doIllegalArgumentException(IllegalArgumentException ex){
        return Result.error(ResultCode.BUSINESS_ERR,"请传入合法字符");
    }

    @ExceptionHandler(Exception.class)
    public Result doException(Exception ex){
        System.out.println("错误类："+ex.getClass());
        System.out.println("错误信息："+ex.getMessage());
        ex.printStackTrace();
        return Result.error(ResultCode.SYSTEM_UNKNOWN_ERR,"系统错误");
    }
}
