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
    /**
     * 分页获取公有界面提交评测列表
     * @param pageModel
     * @return
     */
    IPage<PublicSubmitModel> getPublicSubmitModelList(PageModel pageModel);

    /**
     * 通过筛选参数分页获取公有界面提交评测列表
     * @param submitConditionPageModel
     * @return
     */
    IPage<PublicSubmitModel> getPublicSubmitModelListByCondition(SubmitConditionPageModel submitConditionPageModel);

    /**
     * 获取提交评测的代码
     * @param submitId
     * @param problemId
     * @param userId
     * @return
     */
    String getSubmitMsgCode(Long submitId, Long problemId, Long userId);
}
