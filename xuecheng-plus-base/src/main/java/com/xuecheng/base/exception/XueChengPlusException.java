package com.xuecheng.base.exception;

public class XueChengPlusException extends RuntimeException{
//    前端要求的
    private String errMessage;

    public XueChengPlusException() {
    }

    public XueChengPlusException(String errMessage) {
        super(errMessage);
        //构造器这里设计对属性赋值很灵性，搭配下面个getErrMessage方法
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }
//CommonError类型啊， 就是想用一个通用的，也是可以改的
    public static void cast(CommonError commonError){
        throw new XueChengPlusException(commonError.getErrMessage());
    }
//手敲异常信息。
    public static void cast(String errMessage){
        throw new XueChengPlusException(errMessage);
    }
}
