package com.three_kingdoms.controller;

import com.three_kingdoms.exception.BusinessException;
import com.three_kingdoms.exception.SystemException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ProjectExceptionAdvice {

    @ExceptionHandler(SystemException.class)
    public Result<String> doSystemException(SystemException ex){
        log.error(ex.getMessage(),ex+"\n");
        return Result.error(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<String> doBusinessException(BusinessException ex){
        return Result.error(ex.getCode(),ex.getMessage());
    }

    /**
     * 方法RequestParam/PathVariable形式参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> doPatternException(){
        return Result.error(ResultCode.BUSINESS_ERR,"请正确填写内容");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> doIllegalArgumentException(){
        return Result.error(ResultCode.BUSINESS_ERR,"请传入合法字符");
    }

    /**
    * 方法参数校验异常
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> doNotValidException(){
        return Result.error(ResultCode.BUSINESS_ERR,"参数不合法");
    }

    /**
     * 方法参数校验异常[类型不配备]
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    public Result<String> doUnexpectedTypeException(){
        return Result.error(ResultCode.BUSINESS_ERR,"参数类型不匹配");
    }

    /**
     * 前端传递参数验证异常
     * /@param 'ex'
     * /@return
     */
    @ExceptionHandler(BindException.class)
    public Result<String> doBindException(){
        return Result.error(ResultCode.BUSINESS_ERR,"提交的参数不符合规范");
    }


    @ExceptionHandler(Exception.class)
    public Result<String> doException(Exception ex){
        log.info("错误类："+ex.getClass());
        log.info("错误信息："+ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace())+"\n");
        return Result.error(ResultCode.SYSTEM_UNKNOWN_ERR,"系统错误");
    }
}
