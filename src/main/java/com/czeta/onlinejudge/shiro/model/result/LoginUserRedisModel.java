package com.czeta.onlinejudge.shiro.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName LoginUserRedisModel
 * @Description 登录用户信息 Redis缓存model
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LoginUserRedisModel extends LoginUserModel {
    private String salt;
}
