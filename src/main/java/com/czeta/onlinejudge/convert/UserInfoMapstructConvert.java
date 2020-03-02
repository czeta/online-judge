package com.czeta.onlinejudge.convert;

import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.entity.UserCertification;
import com.czeta.onlinejudge.model.param.RegisterModel;
import com.czeta.onlinejudge.model.param.UserCertificationModel;
import com.czeta.onlinejudge.model.param.UserInfoModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName UserInfoMapstructConvert
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 15:11
 * @Version 1.0
 */
@Mapper
public interface UserInfoMapstructConvert {

    UserInfoMapstructConvert INSTANCE = Mappers.getMapper(UserInfoMapstructConvert.class);

    User registerModelToUserInfo(RegisterModel registerModel);

    User userInfoModelToUserInfo(UserInfoModel userInfoModel);

    UserCertification userCertificationModelToUserCertification(UserCertificationModel userCertificationModel);
}
