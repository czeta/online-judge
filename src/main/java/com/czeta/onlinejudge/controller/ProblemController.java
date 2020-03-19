package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.ProblemConditionPageModel;
import com.czeta.onlinejudge.model.param.SubmitModel;
import com.czeta.onlinejudge.model.result.DetailProblemModel;
import com.czeta.onlinejudge.model.result.PublicSimpleProblemModel;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.utils.response.APIResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ProblemController
 * @Description 题目控制器
 * @Author chenlongjie
 * @Date 2020/3/17 13:08
 * @Version 1.0
 */
@Slf4j
@Api(tags = "Problem Controller")
@RestController
@RequestMapping("/api/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @ApiOperation(value = "分页获得公共题目列表", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @PostMapping("/problemList")
    public APIResult<IPage<PublicSimpleProblemModel>> getPublicProblemList(@RequestBody PageModel pageModel) {
        return new APIResult<>(problemService.getPublicProblemList(pageModel));
    }

    @ApiOperation(value = "根据筛选条件，分页获得公共题目列表", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemConditionPageModel", value = "分页请求参数与筛选条件model", dataType = "ProblemConditionPageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @PostMapping("/conditionalProblemList")
    public APIResult<IPage<PublicSimpleProblemModel>> getPublicProblemList(@RequestBody ProblemConditionPageModel problemConditionPageModel) {
        return new APIResult<>(problemService.getPublicProblemListByCondition(problemConditionPageModel));
    }

    @ApiOperation(value = "根据问题ID获取问题详情", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "问题ID", dataType = "Long", paramType = "path", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2200, message = "题目异常，已下线"),
            @ApiResponse(code = 2101, message = "无权查看该题：属于比赛题")
    })
    @GetMapping("/problemInfo/{problemId}")
    public APIResult<DetailProblemModel> getDetailProblemInfo(@PathVariable Long problemId) {
        return new APIResult<>(problemService.getDetailProblemInfoById(problemId));
    }

    @ApiOperation(value = "用户提交题目", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "submitModel", value = "提交代码model", dataType = "SubmitModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/submit")
    public APIResult submitProblem(@RequestBody SubmitModel submitModel, @RequestAttribute Long userId) {
        problemService.submitProblem(submitModel, userId);
        return new APIResult();
    }
}
