package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.model.param.RegisterParamModel;

/**
 * @ClassName UserService
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 14:50
 * @Version 1.0
 */
public interface UserService {


    void saveNewUser(RegisterParamModel registerParamModel);

    User getUserInfoById(Long id);

}
