package com.czeta.onlinejudge.util.response;

/**
 * @InterfaceName IBaseResponse
 * @Description 基础response接口
 * @Author chenlongjie
 * @Date 2020/2/23 12:56
 * @Version 1.0
 */
public interface IBaseResponse {
    Integer getStatus();
    String getMessage();
}
