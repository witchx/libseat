package com.libseat.utils.code;

import com.libseat.utils.code.IErrorCode;

public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    EMPTY(400, "没有相关数据");
    private long code;
    private String msg;
 
    private ResultCode(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }
 
    @Override
    public long getCode() {
        return code;
    }
 
    @Override
    public String getMsg() {
        return msg;
    }
}