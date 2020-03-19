package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.SubmitConditionPageModel;
import com.czeta.onlinejudge.model.result.PublicSubmitModel;

/**
 * @InterfaceName SubmitService
 * @Description 提交状态服务
 * @Author chenlongjie
 * @Date 2020/3/18 11:22
 * @Version 1.0
 */
public interface SubmitService {
    IPage<PublicSubmitModel> getPublicSubmitModelList(PageModel pageModel);

    IPage<PublicSubmitModel> getPublicSubmitModelListByCondition(SubmitConditionPageModel submitConditionPageModel);

    String getSubmitCode(Long submitId, Long problemId, Long userId);
}
