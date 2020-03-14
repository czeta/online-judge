package com.czeta.onlinejudge.model.result;

import lombok.Data;

/**
 * @ClassName ProblemTagModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/14 11:22
 * @Version 1.0
 */
@Data
public class ProblemTagModel {
    Long problemId;
    Integer tagId;
    String name;
}
