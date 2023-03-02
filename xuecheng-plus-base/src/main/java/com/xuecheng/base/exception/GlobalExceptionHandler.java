package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Slf4j
@ControllerAdvice//控制器增强
public class GlobalExceptionHandler {

//    处理可预知异常，程序员主动抛出的 XueChengPlusException

    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)//状态码返回500
    @ResponseBody
    public RestErrorResponse doXueChengPlusException(XueChengPlusException e){
//        打印日志
        log.error("捕获异常信息{}",e.getErrMessage());
//        这儿的RestErrorResponse方法里面只有一个errMessage属性，封装成一个对象返回给前端，让前端解析，我也不知道为什么。
        return new RestErrorResponse(e.getErrMessage());
    }


//    不可预知异常Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doException(Exception e){
    log.error("捕获异常信息{}",e.getMessage());
    //这里不让这样写， 你这样给前端人员返回一些错误信息，怎么能行呢？
//    return new RestErrorResponse(e.getMessage());
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doValidException(MethodArgumentNotValidException argumentNotValidException) {

        BindingResult bindingResult = argumentNotValidException.getBindingResult();
        StringBuffer errMsg = new StringBuffer();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(error -> {
            errMsg.append(error.getDefaultMessage()).append(",");
        });
        log.error(errMsg.toString());
        return new RestErrorResponse(errMsg.toString());
    }



}
