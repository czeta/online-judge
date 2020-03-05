package com.czeta.onlinejudge.convert;

import com.czeta.onlinejudge.dao.entity.JudgeType;
import com.czeta.onlinejudge.model.param.JudgeTypeModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @InterfaceName JudgeMapstructConvert
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 20:40
 * @Version 1.0
 */
@Mapper
public interface JudgeMapstructConvert {
    JudgeMapstructConvert INSTANCE = Mappers.getMapper(JudgeMapstructConvert.class);

    JudgeType judgeTypeModelToJudgeType(JudgeTypeModel judgeTypeModel);
}
