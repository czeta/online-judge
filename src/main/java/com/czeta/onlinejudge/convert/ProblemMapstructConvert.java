package com.czeta.onlinejudge.convert;

import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.entity.ProblemJudgeType;
import com.czeta.onlinejudge.model.param.MachineProblemModel;
import com.czeta.onlinejudge.model.result.DetailProblemModel;
import com.czeta.onlinejudge.model.result.PublicSimpleProblemModel;
import com.czeta.onlinejudge.model.result.SimpleProblemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @InterfaceName ProblemInfoMapstructConvert
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/12 20:12
 * @Version 1.0
 */
@Mapper
public interface ProblemMapstructConvert {
    ProblemMapstructConvert INSTANCE = Mappers.getMapper(ProblemMapstructConvert.class);

    Problem machineProblemToProblem(MachineProblemModel machineProblemModel);

    ProblemJudgeType machineProblemToProblemJudgeType(MachineProblemModel machineProblemModel);

    SimpleProblemModel problemToSimpleProblemModel(Problem problem);

    PublicSimpleProblemModel problemToPublicSimpleProblemModel(Problem problem);

    DetailProblemModel problemToDetailProblemModel(Problem problem);
}
