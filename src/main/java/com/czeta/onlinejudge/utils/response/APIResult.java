package com.czeta.onlinejudge.utils.response;

import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.IBaseException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName APIResult
 * @Description 用于Controller返回的通用泛型类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@ApiModel("RESTFUL风格返回接口")
public class APIResult<T> {
    @ApiModelProperty("响应码")
    private Integer status;
    @ApiModelProperty("响应信息")
    private String message;
    @ApiModelProperty("访问结果数据对象")
    private T data;

    public APIResult(T data) {
        this();
        this.data = data;
    }

    public APIResult() {
        this(IBaseStatusMsg.APIEnum.SUCCESS);
    }

    public APIResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public APIResult(IBaseStatusMsg statusMsg) {
        this.status = statusMsg.getCode();
        this.message = statusMsg.getMessage();
    }

    public APIResult(Exception e) {
        if (e instanceof IBaseException) {
            IBaseException apiException = (IBaseException) e;
            this.status = apiException.getCode();
            this.message = apiException.getMessage();
        } else {
            this.status = IBaseStatusMsg.APIEnum.SERVER_ERROR.getCode();
            this.message = e.getMessage();
        }
    }

    public APIResult(Exception e, T data) {
        this(e);
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
