package com.czeta.onlinejudge.service.impl;

import com.czeta.onlinejudge.convert.UserInfoMapstructConvert;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.mapper.UserMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.RegisterParamModel;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.util.exception.APIRuntimeException;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import com.czeta.onlinejudge.util.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 16:13
 * @Version 1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;

    public void saveNewUser(RegisterParamModel registerParamModel) {
        AssertUtils.notNull(registerParamModel.getUsername(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(registerParamModel.getPassword(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(registerParamModel.getPassword(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = UserInfoMapstructConvert.INSTANCE.registerParamToUserInfo(registerParamModel);
        user.setPassword(PasswordUtils.encrypt(user.getPassword(), jwtProperties.getSecret()));
        user.setRoleId(RoleType.COMMON.getCode());
        user.setRank(userMapper.selectCount(null) + 1);
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_USERNAME);
        }
    }

    public User getUserInfoById(Long id) {
        return userMapper.selectById(id);
    }
}
