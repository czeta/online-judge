package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.convert.UserInfoMapstructConvert;
import com.czeta.onlinejudge.dao.entity.Certification;
import com.czeta.onlinejudge.dao.entity.UserCertification;
import com.czeta.onlinejudge.dao.mapper.CertificationMapper;
import com.czeta.onlinejudge.dao.mapper.UserCertificationMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.model.param.CertificationModel;
import com.czeta.onlinejudge.model.param.UserCertificationModel;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;
import com.czeta.onlinejudge.service.CertificationService;
import com.czeta.onlinejudge.util.exception.APIRuntimeException;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import com.czeta.onlinejudge.util.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName CertificationServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/5 11:21
 * @Version 1.0
 */
@Slf4j
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
}
