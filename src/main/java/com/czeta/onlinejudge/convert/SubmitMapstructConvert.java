package com.czeta.onlinejudge.convert;

import com.czeta.onlinejudge.dao.entity.Submit;
import com.czeta.onlinejudge.model.result.PublicSubmitModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @InterfaceName SubmitMapstructConvert
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 11:40
 * @Version 1.0
 */
@Mapper
public interface SubmitMapstructConvert {
    SubmitMapstructConvert INSTANCE = Mappers.getMapper(SubmitMapstructConvert.class);

    PublicSubmitModel submitToPublicSubmitModel(Submit submit);
}
