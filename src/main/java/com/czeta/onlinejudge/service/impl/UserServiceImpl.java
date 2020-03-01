package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.convert.UserInfoMapstructConvert;
import com.czeta.onlinejudge.dao.entity.SolvedProblem;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.mapper.SolvedProblemMapper;
import com.czeta.onlinejudge.dao.mapper.UserMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudge.model.param.RegisterParamModel;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.util.exception.APIRuntimeException;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import com.czeta.onlinejudge.util.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SolvedProblemMapper solvedProblemMapper;

    public void saveNewUser(RegisterParamModel registerParamModel) {
        AssertUtils.notNull(registerParamModel.getUsername(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(registerParamModel.getPassword(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(registerParamModel.getPassword(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = UserInfoMapstructConvert.INSTANCE.registerParamToUserInfo(registerParamModel);
        user.setPassword(PasswordUtils.encrypt(user.getPassword(), jwtProperties.getSecret()));
        user.setRoleId(RoleType.COMMON.getId());
        user.setRank(userMapper.selectCount(null) + 1);
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_USERNAME);
        }
    }

    public User getUserInfoById(Long id) {
        AssertUtils.notNull(id, BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = userMapper.selectById(id);
        AssertUtils.notNull(user, BaseStatusMsg.APIEnum.RESP_FIELD_VALID);
        user.setPassword(null);
        return user;
    }

    public List<Long> getSolvedProblemByUserId(Long id) {
        AssertUtils.notNull(id, BaseStatusMsg.APIEnum.PARAM_ERROR);
        List<SolvedProblem> solvedProblemList = solvedProblemMapper.selectList(Wrappers.<SolvedProblem>lambdaQuery()
                .select(SolvedProblem::getProblemId)
                .eq(SolvedProblem::getUserId, id)
                .eq(SolvedProblem::getSubmitStatus, SubmitStatus.ACCEPTED.getName())
                .groupBy(SolvedProblem::getProblemId));
        return solvedProblemList.stream().map(SolvedProblem::getProblemId).collect(Collectors.toList());
    }

    public List<Long> getNotSolvedProblemByUserId(Long id) {
        AssertUtils.notNull(id, BaseStatusMsg.APIEnum.PARAM_ERROR);
        List<SolvedProblem> solvedProblemList = solvedProblemMapper.selectList(Wrappers.<SolvedProblem>lambdaQuery()
                .select(SolvedProblem::getProblemId)
                .eq(SolvedProblem::getUserId, id)
                .ne(SolvedProblem::getSubmitStatus, SubmitStatus.ACCEPTED.getName())
                .groupBy(SolvedProblem::getProblemId));
        return solvedProblemList.stream().map(SolvedProblem::getProblemId).collect(Collectors.toList());
    }

}
