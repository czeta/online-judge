package com.czeta.onlinejudge.convert;

import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.model.param.RegisterParamModel;
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

    User registerParamToUserInfo(RegisterParamModel registerParamModel);
}
