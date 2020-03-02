package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.Certification;
import com.czeta.onlinejudge.dao.entity.Message;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.entity.UserCertification;
import com.czeta.onlinejudge.model.param.RegisterModel;
import com.czeta.onlinejudge.model.param.UserAccountModel;
import com.czeta.onlinejudge.model.param.UserCertificationModel;
import com.czeta.onlinejudge.model.param.UserInfoModel;

import java.util.List;

/**
 * @ClassName UserService
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 14:50
 * @Version 1.0
 */
public interface UserService {


    void saveNewUser(RegisterModel registerModel);

    User getUserInfoById(Long id);

    List<Long> getSolvedProblemsByUserId(Long userId);

    List<Long> getNotSolvedProblemsByUserId(Long userId);

    List<Message> getMessagesByUserId(Long userId);

    boolean updateUserInfoByUserId(UserInfoModel userInfoModel);

    boolean updateUserEmailByUserId(String oldEmail, String newEmail, Long userId);

    boolean updateUserPasswordByUserId(String oldPassword, String newPassword, Long userId);

    void saveNewCertification(UserCertificationModel userCertificationModel);

    boolean updateUserCertification(UserCertificationModel userCertificationModel);

    UserCertification getUserCertification(Long userId);
}
