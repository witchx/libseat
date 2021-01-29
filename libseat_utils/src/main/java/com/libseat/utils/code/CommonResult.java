package com.libseat.utils.code;

public class CommonResult<T> {
    private long code;
    private String msg;
    private T data;
 
    protected CommonResult() {
    }
 
    protected CommonResult(long code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }
 
    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }
 
    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param msg 提示信息
     */
    public static <T> CommonResult<T> success(T data, String msg) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), msg, data);
    }
 
    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMsg(), null);
    }
 
    /**
     * 失败返回结果
     *
     * @param msg 提示信息
     */
    public static <T> CommonResult<T> failed(String msg) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), msg, null);
    }
 
    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCode.FAILED);
    }
 
    /**
     * 参数验证失败返回结果
     */
    public static <T> CommonResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }
 
    /**
     * 参数验证失败返回结果
     *
     * @param msg 提示信息
     */
    public static <T> CommonResult<T> validateFailed(String msg) {
        return new CommonResult<T>(ResultCode.VALIDATE_FAILED.getCode(), msg, null);
    }
 
    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMsg(), data);
    }
 
    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMsg(), data);
    }
 
    public long getCode() {
        return code;
    }
 
    public void setCode(long code) {
        this.code = code;
    }
 
    public String getMsg() {
        return msg;
    }
 
    public void setMsg(String msg) {
        this.msg = msg;
    }
 
    public T getData() {
        return data;
    }
 
    public void setData(T data) {
        this.data = data;
    }
}