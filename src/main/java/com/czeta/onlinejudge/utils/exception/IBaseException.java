package com.czeta.onlinejudge.utils.exception;

/**
 * @InterfaceName IBaseException
 * @Description 基础异常接口
 * @Author chenlongjie
 * @Date 2020/2/23 12:56
 * @Version 1.0
 */
public interface IBaseException {
    Integer getCode();
    String getMessage();
}
