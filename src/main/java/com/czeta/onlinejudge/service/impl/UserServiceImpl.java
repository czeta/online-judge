package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.convert.UserInfoMapstructConvert;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.dao.mapper.*;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.CommonItemStatus;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudge.model.param.UserRegisterModel;
import com.czeta.onlinejudge.model.param.UserInfoModel;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudge.utils.utils.PasswordUtils;
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

    public void saveNewUser(UserRegisterModel userRegisterModel) {
        AssertUtils.notNull(userRegisterModel.getUsername(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(userRegisterModel.getPassword(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(userRegisterModel.getEmail(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        Admin admin = adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername, userRegisterModel.getUsername()));
        AssertUtils.isNull(admin, BaseStatusMsg.EXISTED_USERNAME);
        User user = UserInfoMapstructConvert.INSTANCE.userRegisterModelToUserInfo(userRegisterModel);
        user.setPassword(PasswordUtils.encrypt(user.getPassword(), jwtProperties.getSecret()));
        user.setRoleId(RoleType.COMMON_USER.getCode());
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
    public IPage<Message> getMessagesByUserId(PageModel pageParam, Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Page<Message> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        return messageMapper.selectPage(page, Wrappers.<Message>lambdaQuery().eq(Message::getUserId, userId));
    }

    @Override
    public boolean updateMessageStatusById(Long msgId) {
        AssertUtils.notNull(msgId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Message message = new Message();
        message.setId(msgId);
        message.setStatus(CommonItemStatus.ENABLE.getCode());
        messageMapper.updateById(message);
        return true;
    }

    @Override
    public boolean updateUserInfoByUserId(UserInfoModel userInfoModel) {
        AssertUtils.notNull(userInfoModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = UserInfoMapstructConvert.INSTANCE.userInfoModelToUserInfo(userInfoModel);
        user.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        userMapper.updateById(user);
        return true;
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
        userMapper.updateById(newUserInfo);
        return true;
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
        userMapper.updateById(newUserInfo);
        return true;
    }

    @Override
    public IPage<User> getUserInfoList(PageModel pageModel) {
        Page page = new Page(pageModel.getOffset(), pageModel.getLimit());
        IPage<User> userIPage = userMapper.selectPage(page,null);
        userIPage.setRecords(userIPage.getRecords()
                .stream()
                .map(s -> {
                    s.setPassword(null);
                    return s;
                }).collect(Collectors.toList()));
        return userIPage;
    }

    @Override
    public IPage<User> getUserInfosByUsernameKey(PageModel<String> pageModel) {
        AssertUtils.notNull(pageModel.getParamData(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        Page page = new Page(pageModel.getOffset(), pageModel.getLimit());
        IPage<User> userIPage = userMapper.selectPage(page, Wrappers.<User>lambdaQuery()
                .like(User::getUsername, "%" + pageModel.getParamData() + "%"));
        userIPage.setRecords(userIPage.getRecords()
                .stream()
                .map(s -> {
                    s.setPassword(null);
                    return s;
                }).collect(Collectors.toList()));
        return userIPage;
    }

    public boolean resetUserPasswordByUsername(String username) {
        AssertUtils.notNull(username, BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = new User();
        user.setPassword(PasswordUtils.encrypt(username, jwtProperties.getSecret()));
        user.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        userMapper.update(user, Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username));
        return true;
    }

    @Override
    public boolean disableUserAccountByUsername(String username) {
        AssertUtils.notNull(username, BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = new User();
        user.setStatus((short) 0);
        user.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        userMapper.update(user, Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username));
        return true;
    }

    @Override
    public boolean updateUserHeadByUserId(Long userId, String headPath) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = new User();
        user.setId(userId);
        user.setHeadPortrait(headPath);
        return userMapper.updateById(user) == 1;
    }
}
