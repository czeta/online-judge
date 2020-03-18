package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.SubmitConditionPageModel;
import com.czeta.onlinejudge.model.result.PublicSubmitModel;
import com.czeta.onlinejudge.service.SubmitService;
import com.czeta.onlinejudge.utils.response.APIResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName StatusController
 * @Description 评测状态界面控制器
 * @Author chenlongjie
 * @Date 2020/3/18 11:14
 * @Version 1.0
 */
@Slf4j
@Api(tags = "Status Controller")
@RestController
@RequestMapping("/api/status")
public class StatusController {
    @Autowired
    private SubmitService submitService;

    @PostMapping("/statusList")
    public APIResult<IPage<PublicSubmitModel>> getPublicSubmitModelList(@RequestBody PageModel pageModel) {
        return new APIResult<>(submitService.getPublicSubmitModelList(pageModel));
    }

    @PostMapping("/conditionalStatusList")
    public APIResult<IPage<PublicSubmitModel>> getPublicSubmitModelList(@RequestBody SubmitConditionPageModel submitConditionPageModel) {
        return new APIResult<>(submitService.getPublicSubmitModelListByCondition(submitConditionPageModel));
    }

    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/code")
    public APIResult<String> getSubmitCodeByProblemId(@RequestParam Long submitId, @RequestParam Long problemId, @RequestAttribute Long userId) {
        return new APIResult<>(submitService.getSubmitCodeByProblemId(submitId, problemId, userId));
    }
}
