package com.libseat.api.utils;

public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");
    private long code;
    private String message;
 
    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
 
    @Override
    public long getCode() {
        return code;
    }
 
    @Override
    public String getMessage() {
        return message;
    }
}