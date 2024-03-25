package com.three_kingdoms.controller;

import com.three_kingdoms.exception.BusinessException;
import com.three_kingdoms.exception.SystemException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ProjectExceptionAdvice {

    @ExceptionHandler(SystemException.class)
    public Result<String> doSystemException(SystemException ex){
        return Result.error(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<String> doBusinessException(BusinessException ex){
        return Result.error(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> doPatternException(){
        return Result.error(ResultCode.BUSINESS_ERR,"请正确填写内容");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> doIllegalArgumentException(){
        return Result.error(ResultCode.BUSINESS_ERR,"请传入合法字符");
    }

    @ExceptionHandler(Exception.class)
    public Result<String> doException(Exception ex){
        log.info("错误类："+ex.getClass());
        log.info("错误信息："+ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return Result.error(ResultCode.SYSTEM_UNKNOWN_ERR,"系统错误");
    }
}
