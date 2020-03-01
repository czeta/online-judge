package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName ProblemJudgeType
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:21
 * @Version 1.0
 */
@Data
public class ProblemJudgeType {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long problemId;
    private Integer judgeId;
    private Long reptileProblemId;
    private String allSampleInput;
    private String allSampleOutput;
    private Short status;
    private String crtTs;
    private String lmTs;
}
