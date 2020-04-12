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
    private Integer judgeTypeId;
    private String codeTemplate;
    private Integer spj;
    private String spjCode;
    private String spjLanguage;
    private String spjVersion;
    private Long spiderProblemId;
    private Short status;
    private String crtTs;
    private String lmTs;
}