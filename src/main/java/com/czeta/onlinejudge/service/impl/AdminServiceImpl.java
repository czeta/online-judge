package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.convert.JudgeMapstructConvert;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.dao.mapper.*;
import com.czeta.onlinejudge.enums.*;
import com.czeta.onlinejudge.model.param.AdminRegisterModel;
import com.czeta.onlinejudge.model.param.AnnouncementModel;
import com.czeta.onlinejudge.model.param.CertificationModel;
import com.czeta.onlinejudge.model.param.JudgeTypeModel;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;
import com.czeta.onlinejudge.service.AdminService;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.util.exception.APIRuntimeException;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import com.czeta.onlinejudge.util.utils.DateUtils;
import com.czeta.onlinejudge.util.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName AdminServiceIml
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 14:39
 * @Version 1.0
 */
@Repository
public class AdminServiceImpl implements AdminService {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CertificationMapper certificationMapper;

    @Autowired
    private UserCertificationMapper userCertificationMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private JudgeTypeMapper judgeTypeMapper;

    @Override
    public List<User> getUserInfoList() {
        return userMapper.selectList(null)
                .stream()
                .map(s -> {
                    s.setPassword(null);
                    return s;
                }).collect(Collectors.toList());
    }

    @Override
    public List<User> getUserInfosByUsernameKey(String usernameKey) {
        AssertUtils.notNull(usernameKey, BaseStatusMsg.APIEnum.PARAM_ERROR);
        List<User> userList = userMapper.selectList(Wrappers.<User>lambdaQuery()
                .like(User::getUsername, "%" + usernameKey + "%"))
                .stream()
                .map(s -> {
                    s.setPassword(null);
                    return s;
                }).collect(Collectors.toList());
        return userList;
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
    public List<AppliedCertificationModel> getAppliedCertificationList() {
        return userCertificationMapper.selectAppliedCertification();
    }

    @Override
    public boolean updateAppliedCertificationByUserId(Short status, Long userId) {
        UserCertification userCertification = userCertificationMapper.selectOne(Wrappers.<UserCertification>lambdaQuery()
                .eq(UserCertification::getUserId, userId)
                .ne(UserCertification::getStatus, 0));
        AssertUtils.isNull(userCertification, BaseStatusMsg.APIEnum.PARAM_ERROR);
        UserCertification updatedUserCertification = new UserCertification();
        updatedUserCertification.setStatus(status);
        updatedUserCertification.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        return userCertificationMapper.update(updatedUserCertification, Wrappers.<UserCertification>lambdaQuery()
                .eq(UserCertification::getUserId, userId)) == 1;
    }

    @Override
    public List<Certification> getCertificationTypes() {
        return certificationMapper.selectList(null);
    }

    @Override
    public void saveNewCertificationType(String type) {
        Certification certification = new Certification();
        certification.setName(type);
        try {
            certificationMapper.insert(certification);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_NAME);
        }
    }

    @Override
    public boolean updateCertification(CertificationModel certificationModel) {
        AssertUtils.notNull(certificationModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        Certification certification = new Certification();
        certification.setId(certificationModel.getId());
        certification.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        if (certificationModel.getName() != null) {  // 更新认证名
            certification.setName(certificationModel.getName());
            certificationMapper.updateById(certification);
        } else if (certificationModel.getStatus() != null) { // 更新认证类型状态（启用或不弃用）
            certification.setStatus(certificationModel.getStatus());
            certificationMapper.updateById(certification);
            // 当认证类型发生改变时，实名认证表的审核状态也要发生改变
            UserCertification userCertification = new UserCertification();
            userCertification.setStatus(certificationModel.getStatus() == 0 ? (short)-1 : (short)1);
            userCertificationMapper.update(userCertification, Wrappers.<UserCertification>lambdaQuery()
                    .eq(UserCertification::getCertificationId, certificationModel.getId()));
        } else {
            throw new APIRuntimeException(BaseStatusMsg.APIEnum.PARAM_ERROR);
        }
        return true;
    }

    @Override
    public List<Admin> getAdminInfoList() {
        return adminMapper.selectList(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getRoleId, RoleType.COMMON_ADMIN.getCode()))
                .stream()
                .map(s -> {
                    s.setPassword(null);
                    return s;
                }).collect(Collectors.toList());
    }

    @Override
    public List<Admin> getAdminInfoListByUsernameKey(String usernameKey) {
        AssertUtils.notNull(usernameKey, BaseStatusMsg.APIEnum.PARAM_ERROR);
        List<Admin> adminList = adminMapper.selectList(Wrappers.<Admin>lambdaQuery()
                .like(Admin::getUsername, "%" + usernameKey + "%"))
                .stream()
                .map(s -> {
                    s.setPassword(null);
                    return s;
                }).collect(Collectors.toList());
        return adminList;
    }

    @Override
    public void saveNewAdmin(AdminRegisterModel adminRegisterModel) {
        AssertUtils.notNull(adminRegisterModel.getUsername(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(adminRegisterModel.getPassword(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, adminRegisterModel.getUsername()));
        AssertUtils.isNull(user, BaseStatusMsg.EXISTED_USERNAME);
        Admin admin = new Admin();
        admin.setUsername(adminRegisterModel.getUsername());
        admin.setPassword(PasswordUtils.encrypt(adminRegisterModel.getPassword(), jwtProperties.getSecret()));
        admin.setRoleId(RoleType.COMMON_ADMIN.getCode());
        admin.setDescription(adminRegisterModel.getDescription());
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_USERNAME);
        }
    }

    @Override
    public boolean resetAdminPasswordByUsername(String username) {
        AssertUtils.notNull(username, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Admin admin = new Admin();
        admin.setPassword(PasswordUtils.encrypt(username, jwtProperties.getSecret()));
        admin.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        adminMapper.update(admin, Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername, username));
        return true;
    }

    @Override
    public boolean disableAdminAccountByUsername(String username) {
        AssertUtils.notNull(username, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Admin admin = new Admin();
        admin.setStatus((short) 0);
        admin.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        adminMapper.update(admin, Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername, username));
        return true;
    }

    @Override
    public List<Announcement> getHomePageAnnouncementList() {
        return announcementMapper.selectList(Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getSourceId, AnnouncementType.HOME_PAGE.getCode())
                .orderByDesc(Announcement::getStatus)
                .orderByDesc(Announcement::getLmTs));
    }

    @Override
    public void saveNewHomePageAnnouncement(AnnouncementModel announcementModel, Long userId) {
        AssertUtils.notNull(announcementModel.getTitle(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(announcementModel.getContent(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementModel.getTitle());
        announcement.setContent(announcementModel.getContent());
        announcement.setCreator(adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getId, userId)).getUsername());
        announcement.setSourceId(Long.valueOf(AnnouncementType.HOME_PAGE.getCode()));
        announcementMapper.insert(announcement);
    }

    @Override
    public boolean updateHomePageAnnouncement(AnnouncementModel announcementModel) {
        Announcement announcement = new Announcement();
        announcement.setId(announcementModel.getId());
        announcement.setTitle(announcementModel.getTitle());
        announcement.setContent(announcementModel.getContent());
        announcement.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        announcementMapper.updateById(announcement);
        return true;
    }

    @Override
    public String getFAQContent() {
        return announcementMapper.selectOne(Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getSourceId, AnnouncementType.FAQ.getCode())
                .eq(Announcement::getStatus, 1)).getContent();
    }

    @Override
    public boolean updateFAQContent(String content) {
        Announcement announcement = new Announcement();
        announcement.setContent(content);
        announcement.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        announcementMapper.update(announcement, Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getSourceId, AnnouncementType.FAQ.getCode()));
        return true;
    }

    @Override
    public List<JudgeType> getJudgeMachineList() {
        return judgeTypeMapper.selectList(Wrappers.<JudgeType>lambdaQuery()
                .eq(JudgeType::getType, JudgeTypeEnum.JUDGE_MACHINE.getCode())
                .orderByDesc(JudgeType::getStatus));
    }

    @Override
    public void saveNewJudgeMachine(JudgeTypeModel judgeTypeModel) {
        AssertUtils.notNull(judgeTypeModel.getName(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(judgeTypeModel.getUrl(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        JudgeType judgeType = JudgeMapstructConvert.INSTANCE.judgeTypeModelToJudgeType(judgeTypeModel);
        judgeType.setType(JudgeTypeEnum.JUDGE_MACHINE.getCode());
        judgeType.setStatus(JudgeStatus.STOPPED.getCode());
        judgeTypeMapper.insert(judgeType);
    }

    @Override
    public boolean updateJudgeInfoById(JudgeTypeModel judgeTypeModel) {
        AssertUtils.notNull(judgeTypeModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        JudgeType judgeType = JudgeMapstructConvert.INSTANCE.judgeTypeModelToJudgeType(judgeTypeModel);
        judgeTypeMapper.updateById(judgeType);
        return true;
    }

    @Override
    public List<JudgeType> getJudgeSpiderList() {
        return judgeTypeMapper.selectList(Wrappers.<JudgeType>lambdaQuery()
                .eq(JudgeType::getType, JudgeTypeEnum.JUDGE_SPIDER.getCode())
                .orderByDesc(JudgeType::getStatus));
    }

    @Override
    public void saveNewJudgeSpider(JudgeTypeModel judgeTypeModel) {
        AssertUtils.notNull(judgeTypeModel.getName(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(judgeTypeModel.getUrl(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        JudgeType judgeType = JudgeMapstructConvert.INSTANCE.judgeTypeModelToJudgeType(judgeTypeModel);
        judgeType.setType(JudgeTypeEnum.JUDGE_SPIDER.getCode());
        judgeType.setStatus(JudgeStatus.STOPPED.getCode());
        judgeTypeMapper.insert(judgeType);
    }
}
