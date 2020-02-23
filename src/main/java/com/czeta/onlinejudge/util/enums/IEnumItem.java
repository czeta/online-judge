package com.czeta.onlinejudge.util.enums;

import java.util.Objects;

/**
 * @InterfaceName IEnumItem
 * @Description 基础枚举接口
 * @Author chenlongjie
 * @Date 2020/2/23 12:56
 * @Version 1.0
 */
public interface IEnumItem<K, V> {
    K getCode();
    
    V getMessage();
    
    default boolean equalByCode(K code) {
        return Objects.equals(this.getCode(), code);
    }
}
