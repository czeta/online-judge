package com.czeta.onlinejudge.dao.entity;

import lombok.Data;

/**
 * @ClassName ProblemTag
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:28
 * @Version 1.0
 */
@Data
public class ProblemTag {
    private Long id;
    private Long problemId;
    private Long tagId;
    private Short status;
    private String crtTs;
    private String lmTs;
}
