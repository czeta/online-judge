package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.convert.UserInfoMapstructConvert;
import com.czeta.onlinejudge.dao.entity.Certification;
import com.czeta.onlinejudge.dao.entity.UserCertification;
import com.czeta.onlinejudge.dao.mapper.CertificationMapper;
import com.czeta.onlinejudge.dao.mapper.UserCertificationMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.CommonItemStatus;
import com.czeta.onlinejudge.model.param.CertificationModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.UserCertificationModel;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;
import com.czeta.onlinejudge.service.CertificationService;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName CertificationServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/5 11:21
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class CertificationServiceImpl implements CertificationService {
    @Autowired
    private UserCertificationMapper userCertificationMapper;

    @Autowired
    private CertificationMapper certificationMapper;

    @Override
    public void saveNewCertification(UserCertificationModel userCertificationModel) {
        AssertUtils.notNull(userCertificationModel.getUserId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(userCertificationModel.getCertificationId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        Certification certification = certificationMapper.selectOne(Wrappers.<Certification>lambdaQuery()
                .eq(Certification::getId, userCertificationModel.getCertificationId())
                .eq(Certification::getStatus, CommonItemStatus.ENABLE.getCode()));
        AssertUtils.notNull(certification, BaseStatusMsg.APIEnum.PARAM_ERROR);
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
        userCertificationMapper.update(userCertification, Wrappers.<UserCertification>lambdaQuery()
                .eq(UserCertification::getUserId, userCertification.getUserId())
                .eq(UserCertification::getCertificationId, userCertification.getCertificationId()));
        return true;
    }

    @Override
    public UserCertification getUserCertification(Long userId) {
        AssertUtils.notNull(userId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        return userCertificationMapper.selectOne(Wrappers.<UserCertification>lambdaQuery()
                .eq(UserCertification::getUserId, userId));
    }

    @Override
    public IPage<AppliedCertificationModel> getAppliedCertificationList(PageModel pageModel) {
        Page page = new Page<>(pageModel.getOffset(), pageModel.getLimit());
        return userCertificationMapper.selectAppliedCertification(page);
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
    public List<Certification> getValidCertificationTypes() {
        return certificationMapper.selectList(Wrappers.<Certification>lambdaQuery()
                .eq(Certification::getStatus, CommonItemStatus.ENABLE.getCode()));
    }

    @Override
    public void saveAndUpdateCertification(List<String> typeList) {
        HashMap<String, Integer> typeMap = new HashMap<>();
        for (String str : typeList) {
            typeMap.put(str, 0);
        }
        List<Certification> certificationList = certificationMapper.selectList(Wrappers.<Certification>lambdaQuery()
                .eq(Certification::getStatus, CommonItemStatus.ENABLE.getCode()));
        for (Certification certification : certificationList) {
            if (!typeMap.containsKey(certification.getName())) { // 弃用
                certification.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
                certification.setStatus(CommonItemStatus.DISABLE.getCode());
                certificationMapper.updateById(certification);
                // 当认证类型发生改变时，实名认证表的审核状态也要发生改变
                UserCertification userCertification = new UserCertification();
                userCertification.setStatus(CommonItemStatus.DISABLE.getCode());
                userCertificationMapper.update(userCertification, Wrappers.<UserCertification>lambdaQuery()
                        .eq(UserCertification::getCertificationId, certification.getId()));
            } else { // 标记已存在类型
                typeMap.put(certification.getName(), 1);
            }
        }
        for (Map.Entry<String, Integer> typeEntry : typeMap.entrySet()) {
            if (typeEntry.getValue().equals(0)) { // 新创建的类型
                Certification certification = new Certification();
                certification.setName(typeEntry.getKey());
                certification.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
                certification.setStatus(CommonItemStatus.ENABLE.getCode());
                int count = certificationMapper.update(certification, Wrappers.<Certification>lambdaQuery()
                        .eq(Certification::getName, certification.getName()));
                if (count != 0) continue;
                try {
                    certificationMapper.insert(certification);
                } catch (Exception e) {
                    throw new APIRuntimeException(BaseStatusMsg.EXISTED_NAME);
                }
            }
        }
    }

    @Override
    public boolean updateCertification(CertificationModel certificationModel) {
        AssertUtils.notNull(certificationModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(certificationModel.getName(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        Certification certification = new Certification();
        certification.setId(certificationModel.getId());
        certification.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        certification.setName(certificationModel.getName());
        try {
            certificationMapper.updateById(certification);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_NAME);
        }
        return true;
    }
}
