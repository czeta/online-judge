package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName ContestModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/16 9:13
 * @Version 1.0
 */
@Data
public class ContestModel {
    private Long id;

    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private String signUpRule;
    private String password;
    private String rankModel;
    private Integer realtimeRank;
    private Short status;
}
