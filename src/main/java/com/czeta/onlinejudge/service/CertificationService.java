package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.Certification;
import com.czeta.onlinejudge.dao.entity.UserCertification;
import com.czeta.onlinejudge.model.param.CertificationModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.UserCertificationModel;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;

import java.util.List;

/**
 * @ClassName CertificationService
 * @Description 认证服务
 * @Author chenlongjie
 * @Date 2020/3/5 11:21
 * @Version 1.0
 */
public interface CertificationService {
    /**
     * 申请用户实名认证
     * @param userCertificationModel
     */
    void saveNewCertification(UserCertificationModel userCertificationModel);

    /**
     * 更新用户认证信息
     * @param userCertificationModel
     * @return
     */
    boolean updateUserCertification(UserCertificationModel userCertificationModel);

    /**
     * 获取用户认证信息
     * @param userId
     * @return
     */
    UserCertification getUserCertification(Long userId);

    /**
     * 获取所有用户申请的认证列表
     * @return
     */
    IPage<AppliedCertificationModel> getAppliedCertificationList(PageModel pageModel);

    /**
     * 通过或不通过用户申请的认证
     * @param status
     * @param userId
     * @return
     */
    boolean updateAppliedCertificationByUserId(Short status, Long userId);

    /**
     * 获取实名认证类型
     * @return
     */
    List<Certification> getCertificationTypes();

    /**
     * 获取有效的实名认证类型
     */
    List<Certification> getValidCertificationTypes();

    /**
     * 确定最终实名认证类型列表
     * @param typeList
     */
    void saveAndUpdateCertification(List<String> typeList);

    /**
     * 更新实名认证名
     * @param certificationModel
     * @return
     */
    boolean updateCertification(CertificationModel certificationModel);
}
