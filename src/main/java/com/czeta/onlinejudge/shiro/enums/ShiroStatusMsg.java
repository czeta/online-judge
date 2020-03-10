package com.czeta.onlinejudge.shiro.enums;

import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;

/**
 * @EnumName ShiroStatusMsg
 * @Description shiro认证与权限控制的通用返回状态码
 * @Author chenlongjie
 * @Date 2020/2/23 13:20
 * @Version 1.0
 */
public enum ShiroStatusMsg implements IBaseStatusMsg {
    AUTHORITY_EXCEED(APIEnum.AUTHORITY_EXCEED.getCode(), APIEnum.AUTHORITY_EXCEED.getMessage()),
    LOGIN_AUTHORITY_EXCEED(APIEnum.LOGIN_AUTHORITY_EXCEED.getCode(), APIEnum.LOGIN_AUTHORITY_EXCEED.getMessage()),
    INVALID_TOKEN(3001, "token无效"),
    PARAM_ERROR(3002, "shiro内部参数错误");


    private Integer code;
    private String message;

    private ShiroStatusMsg(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
