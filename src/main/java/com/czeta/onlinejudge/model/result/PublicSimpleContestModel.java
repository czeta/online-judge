package com.czeta.onlinejudge.model.result;

import lombok.Data;

/**
 * @ClassName PublicSimpleContestModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/19 12:29
 * @Version 1.0
 */
@Data
public class PublicSimpleContestModel {
    private Long id;
    private String title;
    private String startTime;
    private String endTime;
    private String signUpRule;
    private String rankModel;
}
