package com.czeta.onlinejudge.controller;

import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.*;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;
import com.czeta.onlinejudge.service.*;
import com.czeta.onlinejudge.util.response.APIResult;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName AdminController
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/3 9:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private JudgeService judgeService;


    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/userManager/userInfoList")
    public APIResult<List<User>> getUserInfoList() {
        return new APIResult<>(userService.getUserInfoList());
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/userManager/userInfoList/{usernameKey}")
    public APIResult<List<User>> getUserInfoList(@PathVariable String usernameKey) {
        return new APIResult<>(userService.getUserInfosByUsernameKey(usernameKey));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/save")
    public APIResult saveNewUser(@RequestBody UserRegisterModel userRegisterModel) {
        userService.saveNewUser(userRegisterModel);
        return new APIResult();
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/password/update/{username}")
    public APIResult<Boolean> updateUserPassword(@PathVariable String username) {
        return new APIResult<>(userService.resetUserPasswordByUsername(username));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/account/update/{username}")
    public APIResult<Boolean> updateUserAccount(@PathVariable String username) {
        return new APIResult<>(userService.disableUserAccountByUsername(username));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/userManager/appliedCertificationList")
    public APIResult<List<AppliedCertificationModel>> getAppliedCertificationList() {
        return new APIResult<>(certificationService.getAppliedCertificationList());
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/appliedCertification/update")
    public APIResult<Boolean> updateAppliedCertification(@RequestParam Short status, @RequestParam Long userId) {
        return new APIResult<>(certificationService.updateAppliedCertificationByUserId(status, userId));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/certManager/certTypeList")
    public APIResult<List<Certification>> getCertificationTypeList() {
        return new APIResult<>(certificationService.getCertificationTypes());
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/certManager/save")
    public APIResult saveNewCertificationType(@RequestParam String type) {
        certificationService.saveNewCertificationType(type);
        return new APIResult();
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/certManager/update")
    public APIResult<Boolean> updateCertification(@RequestBody CertificationModel certificationModel) {
        return new APIResult<>(certificationService.updateCertification(certificationModel));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/adminManager/adminInfoList")
    public APIResult<List<Admin>> getAdminInfoList() {
        return new APIResult<>(adminService.getAdminInfoList());
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/adminManager/adminInfoList/{usernameKey}")
    public APIResult<List<Admin>> getAdminInfoList(@PathVariable String usernameKey) {
        return new APIResult<>(adminService.getAdminInfoListByUsernameKey(usernameKey));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/save")
    public APIResult saveNewAdmin(@RequestBody AdminRegisterModel adminRegisterModel) {
        adminService.saveNewAdmin(adminRegisterModel);
        return new APIResult();
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/password/update/{username}")
    public APIResult<Boolean> updateAdminPassword(@PathVariable String username) {
        return new APIResult<>(adminService.resetAdminPasswordByUsername(username));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/account/update/{username}")
    public APIResult<Boolean> updateAdminAccount(@PathVariable String username) {
        return new APIResult<>(adminService.disableAdminAccountByUsername(username));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/ancManager/ancInfoList")
    public APIResult<List<Announcement>> getHomePageAnnouncementList() {
        return new APIResult<>(announcementService.getHomePageAnnouncementList());
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/ancManager/ancInfo/{id}")
    public APIResult<Announcement> getHomePageAnnouncement(@PathVariable Long id) {
        return new APIResult<>(announcementService.getHomePageAnnouncementById(id));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/ancManager/save")
    public APIResult saveNewHomePageAnnouncement(@RequestBody AnnouncementModel announcementModel, @RequestAttribute Long userId) {
        announcementService.saveNewHomePageAnnouncement(announcementModel, userId);
        return new APIResult();
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/ancManager/update")
    public APIResult<Boolean> updateHomePageAnnouncement(@RequestBody AnnouncementModel announcementModel) {
        return new APIResult<>(announcementService.updateHomePageAnnouncement(announcementModel));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/faq")
    public APIResult<String> getFAQContent() {
        return new APIResult(announcementService.getFAQContent());
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/faq/update")
    public APIResult<Boolean> updateFAQContent(String content) {
        return new APIResult<>(announcementService.updateFAQContent(content));
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/judgeManager/judgeMachineList")
    public APIResult<List<JudgeType>> getJudgeMachineList() {
        return new APIResult<>(judgeService.getJudgeMachineList());
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/judgeManager/judgeMachine/save")
    public APIResult saveNewJudgeMachine(@RequestBody JudgeTypeModel judgeTypeModel) {
        judgeService.saveNewJudgeMachine(judgeTypeModel);
        return new APIResult();
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/judgeManager/judgeSpiderList")
    public APIResult<List<JudgeType>> getJudgeSpiderList() {
        return new APIResult<>(judgeService.getJudgeSpiderList());
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/judgeManager/judgeSpider/save")
    public APIResult saveNewJudgeSpider(@RequestBody JudgeTypeModel judgeTypeModel) {
        judgeService.saveNewJudgeSpider(judgeTypeModel);
        return new APIResult();
    }

    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/judgeManager/judgeInfo/update")
    public APIResult<Boolean> updateJudgeInfo(@RequestBody JudgeTypeModel judgeTypeModel) {
        return new APIResult<>(judgeService.updateJudgeInfoById(judgeTypeModel));
    }

}
