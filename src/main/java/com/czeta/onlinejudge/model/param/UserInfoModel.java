package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName UserInfoParamModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 11:08
 * @Version 1.0
 */
@Data
public class UserInfoModel {
    private Long id;
    private String github;
    private String blog;
    private String mood;
}
