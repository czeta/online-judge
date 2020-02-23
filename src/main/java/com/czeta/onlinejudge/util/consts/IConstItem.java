package com.czeta.onlinejudge.util.consts;

/**
 * @InterfaceName IConstItem
 * @Description 基础常量接口
 * @Author chenlongjie
 * @Date 2020/2/23 12:56
 * @Version 1.0
 */
public interface IConstItem<T, V> {
    T getConstItemKey();
    
    V getConstItemValue();
}
