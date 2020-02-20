package com.czeta.onlinejudge.util.enums;

import java.util.Objects;

public interface IEnumItem<K, V> {
    K getCode();
    
    V getMessage();
    
    default boolean equalByCode(K code) {
        return Objects.equals(this.getCode(), code);
    }
}
