package com.czeta.onlinejudge.convert;

import com.czeta.onlinejudge.dao.entity.Contest;
import com.czeta.onlinejudge.model.param.ContestModel;
import com.czeta.onlinejudge.model.result.DetailContestModel;
import com.czeta.onlinejudge.model.result.PublicSimpleContestModel;
import com.czeta.onlinejudge.model.result.SimpleContestModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @InterfaceName ContestMapstructConvert
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/16 10:21
 * @Version 1.0
 */
@Mapper
public interface ContestMapstructConvert {
    ContestMapstructConvert INSTANCE = Mappers.getMapper(ContestMapstructConvert.class);

    Contest contestModelToContest(ContestModel contestModel);

    SimpleContestModel contestToSimpleContestModel(Contest contest);

    PublicSimpleContestModel contestToPublicSimpleContestModel(Contest contest);

    DetailContestModel contestToDetailContestModel(Contest contest);
}
