package com.czeta.onlinejudge.model.result;

import lombok.Data;

/**
 * @ClassName SimpleContestModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/16 9:26
 * @Version 1.0
 */
@Data
public class SimpleContestModel {
    private Long id;
    private String title;
    private String signUpRule;
    private String rankModel;
    private String creator;
    private Short status;
}
