package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.SubmitConditionPageModel;
import com.czeta.onlinejudge.model.result.PublicSubmitModel;
import com.czeta.onlinejudge.service.SubmitService;
import com.czeta.onlinejudge.utils.response.APIResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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

    @ApiOperation(value = "分页获得公共提交评测列表", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @PostMapping("/statusList")
    public APIResult<IPage<PublicSubmitModel>> getPublicSubmitModelList(@RequestBody PageModel pageModel) {
        return new APIResult<>(submitService.getPublicSubmitModelList(pageModel));
    }

    @ApiOperation(value = "根据筛选参数分页获得公共提交评测列表", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "submitConditionPageModel", value = "分页参数与筛选参数model", dataType = "SubmitConditionPageModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "无分页参数")
    })
    @PostMapping("/conditionalStatusList")
    public APIResult<IPage<PublicSubmitModel>> getPublicSubmitModelList(@RequestBody SubmitConditionPageModel submitConditionPageModel) {
        return new APIResult<>(submitService.getPublicSubmitModelListByCondition(submitConditionPageModel));
    }

    @ApiOperation(value = "获取指定提交评测信息的代码", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "submitId", value = "提交ID", dataType = "Long", paramType = "query", required = true),
            @ApiImplicitParam(name = "problemId", value = "问题ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2003, message = "无该题代码阅读权限")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/code")
    public APIResult<String> getSubmitCode(@RequestParam Long submitId, @RequestParam Long problemId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(submitService.getSubmitCode(submitId, problemId, userId));
    }
}
