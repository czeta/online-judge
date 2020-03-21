package com.czeta.onlinejudge.convert;

import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.model.result.PublicRankModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @InterfaceName RankMapstructConvert
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/20 11:03
 * @Version 1.0
 */
@Mapper
public interface RankMapstructConvert {

    RankMapstructConvert INSTANCE = Mappers.getMapper(RankMapstructConvert.class);

    PublicRankModel UserInfoToPublicRankModel(User userInfo);

}
