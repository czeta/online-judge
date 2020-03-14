package com.czeta.onlinejudge.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czeta.onlinejudge.dao.entity.ProblemTag;
import com.czeta.onlinejudge.model.result.ProblemTagModel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @InterfaceName ProblemTagMapper
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/12 20:56
 * @Version 1.0
 */
@Repository
public interface ProblemTagMapper extends BaseMapper<ProblemTag> {

    @Select("SELECT * FROM problem_tag pt INNER JOIN tag t ON pt.tag_id = t.id WHERE pt.problem_id = #{problemId}")
    List<ProblemTagModel> selectProblemTagJoinTag(Long problemId);
}
