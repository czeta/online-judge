package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName RangedUserModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/10 14:26
 * @Version 1.0
 */
@Data
public class RangedUserModel {
    private String prefix;
    private String suffix;
    private Integer startNumber;
    private Integer endNumber;
    private Integer passwordLength;
}
