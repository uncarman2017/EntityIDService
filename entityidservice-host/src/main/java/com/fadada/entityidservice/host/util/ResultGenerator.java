package com.fadada.entityidservice.host.util;

import org.springframework.validation.BindingResult;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "OK";
    private static final String DEFAULT_FAIL_MESSAGE = "SERVER ERROR";


    public static Result genSuccessResult() {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;

    }

    public static Result genSuccessResult(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setObject(data);
        return result;
    }

    public static Result genClientFailResult(String message) {
        Result result = new Result();
        result.setCode(ResultCode.FAIL_CLIENT.code);
        result.setMessage(message);
        return result;
    }

    public static Result genServerFailResult(String message) {
        Result result = new Result();
        result.setCode(ResultCode.FAIL_SERVER.code);
        result.setMessage(message);
        return result;
    }

    public static Result genServerFailResult() {
        Result result = new Result();
        result.setCode(ResultCode.FAIL_SERVER.code);
        result.setMessage(DEFAULT_FAIL_MESSAGE);
        return result;
    }

    public static Result genClientFailResult(BindingResult bindingResult) {
        Result result = new Result();
        String message = "error";
        try {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        } catch (Exception e) {

        }
        result.setCode(ResultCode.FAIL_CLIENT.code);
        result.setMessage(message);
        return result;
    }

    public static boolean isSuccess(Result result) {
        return result.getCode() == ResultCode.SUCCESS.code;
    }
}
