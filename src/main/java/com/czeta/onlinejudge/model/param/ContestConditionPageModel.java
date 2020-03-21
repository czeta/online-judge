package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName ContestConditionPageModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/19 12:29
 * @Version 1.0
 */
@Data
public class ContestConditionPageModel {
    private PageModel pageModel;

    private String titleKey;
    private String signUpRule;
    private String rankModel;
    private String contestRunningStatus;
}
