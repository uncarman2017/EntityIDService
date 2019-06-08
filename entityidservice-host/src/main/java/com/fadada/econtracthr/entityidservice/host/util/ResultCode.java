package com.fadada.econtracthr.entityidservice.host.util;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(0, "成功"),//成功
    FAIL_CLIENT(200, "客户端错误"),
    FAIL_SERVER(300, "服务端错误"),
    NOT_FOUND(404, "接口不存在");
    public int code;
    private String description;

    ResultCode(int code, String description) {
        this.code = code;
    }
}
