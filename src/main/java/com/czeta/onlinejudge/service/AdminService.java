package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.model.param.AdminRegisterModel;
import com.czeta.onlinejudge.model.param.AnnouncementModel;
import com.czeta.onlinejudge.model.param.CertificationModel;
import com.czeta.onlinejudge.model.param.JudgeTypeModel;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;

import java.util.List;

/**
 * @InterfaceName AdminService
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 14:37
 * @Version 1.0
 */
public interface AdminService {

    List<User> getUserInfoList();

    List<User> getUserInfosByUsernameKey(String usernameKey);

    boolean resetUserPasswordByUsername(String username);

    boolean disableUserAccountByUsername(String username);

    List<AppliedCertificationModel> getAppliedCertificationList();

    boolean updateAppliedCertificationByUserId(Short status, Long userId);

    List<Certification> getCertificationTypes();

    void saveNewCertificationType(String type);

    boolean updateCertification(CertificationModel certificationModel);

    List<Admin> getAdminInfoList();

    List<Admin> getAdminInfoListByUsernameKey(String usernameKey);

    void saveNewAdmin(AdminRegisterModel adminRegisterModel);

    boolean resetAdminPasswordByUsername(String username);

    boolean disableAdminAccountByUsername(String username);

    List<Announcement> getHomePageAnnouncementList();

    void saveNewHomePageAnnouncement(AnnouncementModel announcementModel, Long userId);

    boolean updateHomePageAnnouncement(AnnouncementModel announcementModel);

    String getFAQContent();

    boolean updateFAQContent(String content);

    List<JudgeType> getJudgeMachineList();

    void saveNewJudgeMachine(JudgeTypeModel judgeTypeModel);

    List<JudgeType> getJudgeSpiderList();

    void saveNewJudgeSpider(JudgeTypeModel judgeTypeModel);

    boolean updateJudgeInfoById(JudgeTypeModel judgeTypeModel);

}
