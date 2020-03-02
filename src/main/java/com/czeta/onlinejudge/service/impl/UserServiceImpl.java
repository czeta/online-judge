package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.convert.UserInfoMapstructConvert;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.dao.mapper.*;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudge.model.param.RegisterModel;
import com.czeta.onlinejudge.model.param.UserAccountModel;
import com.czeta.onlinejudge.model.param.UserCertificationModel;
import com.czeta.onlinejudge.model.param.UserInfoModel;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.util.exception.APIRuntimeException;
import com.czeta.onlinejudge.util.response.APIResult;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import com.czeta.onlinejudge.util.utils.DateUtils;
import com.czeta.onlinejudge.util.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private AdminMapper adminMapper;

    @Autowired
    private SolvedProblemMapper solvedProblemMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserCertificationMapper userCertificationMapper;

    public void saveNewUser(RegisterModel registerModel) {
        AssertUtils.notNull(registerModel.getUsername(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(registerModel.getPassword(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(registerModel.getEmail(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        Admin admin = adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, registerModel.getUsername()));
        AssertUtils.isNull(admin, BaseStatusMsg.EXISTED_USERNAME);
        User user = UserInfoMapstructConvert.INSTANCE.registerModelToUserInfo(registerModel);
        user.setPassword(PasswordUtils.encrypt(user.getPassword(), jwtProperties.getSecret()));
        user.setRoleId(RoleType.COMMON.getId());
        user.setRank(userMapper.selectCount(null) + 1);
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_USERNAME);
        }
    }

    public User getUserInfoById(Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = userMapper.selectById(userId);
        AssertUtils.notNull(user, BaseStatusMsg.APIEnum.RESP_FIELD_VALID);
        user.setPassword(null);
        return user;
    }

    public List<Long> getSolvedProblemsByUserId(Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        List<SolvedProblem> solvedProblemList = solvedProblemMapper.selectList(Wrappers.<SolvedProblem>lambdaQuery()
                .select(SolvedProblem::getProblemId)
                .eq(SolvedProblem::getUserId, userId)
                .eq(SolvedProblem::getSubmitStatus, SubmitStatus.ACCEPTED.getName())
                .groupBy(SolvedProblem::getProblemId));
        return solvedProblemList.stream().map(SolvedProblem::getProblemId).collect(Collectors.toList());
    }

    public List<Long> getNotSolvedProblemsByUserId(Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        List<SolvedProblem> solvedProblemList = solvedProblemMapper.selectList(Wrappers.<SolvedProblem>lambdaQuery()
                .select(SolvedProblem::getProblemId)
                .eq(SolvedProblem::getUserId, userId)
                .ne(SolvedProblem::getSubmitStatus, SubmitStatus.ACCEPTED.getName())
                .groupBy(SolvedProblem::getProblemId));
        return solvedProblemList.stream().map(SolvedProblem::getProblemId).collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessagesByUserId(Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        return messageMapper.selectList(Wrappers.<Message>lambdaQuery().eq(Message::getUserId, userId));
    }

    @Override
    public boolean updateUserInfoByUserId(UserInfoModel userInfoModel) {
        AssertUtils.notNull(userInfoModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = UserInfoMapstructConvert.INSTANCE.userInfoModelToUserInfo(userInfoModel);
        user.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        return userMapper.updateById(user) == 1;
    }

    @Override
    public boolean updateUserEmailByUserId(String oldEmail, String newEmail, Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 校验旧信息是否匹配
        User oldUserInfo = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, userId)
                .eq(User::getEmail, oldEmail));
        AssertUtils.notNull(oldUserInfo, BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 更新信息
        User newUserInfo = new User();
        newUserInfo.setId(userId);
        newUserInfo.setEmail(newEmail);
        newUserInfo.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        return userMapper.updateById(newUserInfo) == 1;
    }

    @Override
    public boolean updateUserPasswordByUserId(String oldPassword, String newPassword, Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 校验旧信息是否匹配
        oldPassword = PasswordUtils.encrypt(oldPassword, jwtProperties.getSecret());
        newPassword = PasswordUtils.encrypt(newPassword, jwtProperties.getSecret());
        User oldUserInfo = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, userId)
                .eq(User::getPassword, oldPassword));
        AssertUtils.notNull(oldUserInfo, BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 更新信息
        User newUserInfo = new User();
        newUserInfo.setId(userId);
        newUserInfo.setPassword(newPassword);
        newUserInfo.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        return userMapper.updateById(newUserInfo) == 1;
    }

    @Override
    public void saveNewCertification(UserCertificationModel userCertificationModel) {
        AssertUtils.notNull(userCertificationModel.getUserId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(userCertificationModel.getCertificationId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        UserCertification userCertification = UserInfoMapstructConvert.INSTANCE.userCertificationModelToUserCertification(userCertificationModel);
        userCertification.setStatus((short) 0);
        userCertificationMapper.insert(userCertification);
    }

    @Override
    public boolean updateUserCertification(UserCertificationModel userCertificationModel) {
        AssertUtils.notNull(userCertificationModel.getUserId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(userCertificationModel.getCertificationId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        UserCertification userCertification = UserInfoMapstructConvert.INSTANCE.userCertificationModelToUserCertification(userCertificationModel);
        userCertification.setStatus((short) 0);
        userCertification.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        return userCertificationMapper.update(userCertification, Wrappers.<UserCertification>lambdaQuery()
                .eq(UserCertification::getUserId, userCertification.getUserId())
                .eq(UserCertification::getCertificationId, userCertification.getCertificationId())) == 1;
    }

    @Override
    public UserCertification getUserCertification(Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        return userCertificationMapper.selectOne(Wrappers.<UserCertification>lambdaQuery()
                .eq(UserCertification::getUserId, userId));
    }
}
