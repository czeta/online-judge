package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.Announcement;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.ContestConditionPageModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.SubmitConditionPageModel;
import com.czeta.onlinejudge.model.param.SubmitModel;
import com.czeta.onlinejudge.model.result.*;
import com.czeta.onlinejudge.service.ContestService;
import com.czeta.onlinejudge.utils.response.APIResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @ClassName ContestController
 * @Description 问题控制器
 * @Author chenlongjie
 * @Date 2020/3/19 12:20
 * @Version 1.0
 */
@Slf4j
@Api(tags = "Contest Controller")
@RestController
@RequestMapping("/api/contest")
public class ContestController {
    @Autowired
    private ContestService contestService;

    @ApiOperation(value = "(列表)分页获取公有界面比赛列表信息", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=1)
    @PostMapping("/contestList")
    public APIResult<IPage<PublicSimpleContestModel>> getPublicContestList(@RequestBody PageModel pageModel) {
        return new APIResult<>(contestService.getPublicContestList(pageModel));
    }

    @ApiOperation(value = "(筛选列表)按筛选条件分页获取公有界面比赛列表信息", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestConditionPageModel", value = "分页请求参数和筛选参数", dataType = "ContestConditionPageModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "无分页参数"),
            @ApiResponse(code = 2001, message = "比赛进行状态不合法")
    })
    @ApiOperationSupport(order=2)
    @PostMapping("/conditionalContestList")
    public APIResult<IPage<PublicSimpleContestModel>> getPublicContestListByCondition(@RequestBody ContestConditionPageModel contestConditionPageModel) {
        return new APIResult<>(contestService.getPublicContestListByCondition(contestConditionPageModel));
    }

    @ApiOperation(value = "(详情)获取比赛详情信息", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "比赛不存在或已下线")
    })
    @ApiOperationSupport(order=3)
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/contestInfo/{contestId}")
    public APIResult<DetailContestModel> getDetailContestInfoById(@PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getDetailContestInfoById(contestId, userId));
    }

    @ApiOperation(value = "(报名)比赛报名", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true),
            @ApiImplicitParam(name = "password", value = "密码，在报名规则为“密码”时该字段必须不为null", dataType = "String", paramType = "query", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "密码错误"),
            @ApiResponse(code = 2001, message = "尚未认证实名信息"),
            @ApiResponse(code = 2001, message = "比赛不存在"),
            @ApiResponse(code = 2300, message = "报名已经截止"),
            @ApiResponse(code = 2105, message = "已经申请过比赛")
    })
    @ApiOperationSupport(order=4)
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/contestInfo/{contestId}/signUp")
    public APIResult<Boolean> saveNewSignUpContest(@PathVariable Long contestId, @RequestParam(required = false) String password, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.saveNewSignUpContest(contestId, password, userId));
    }

    @ApiOperation(value = "(公告)获取比赛公告详情列表", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=5)
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/contestInfo/{contestId}/announcements")
    public APIResult<List<Announcement>> getContestAnnouncementList(@PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getContestAnnouncementList(contestId, userId));
    }

    @ApiOperation(value = "(题目)获取指定比赛下的所有题目简略信息列表", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=6)
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/contestInfo/{contestId}/problemList")
    public APIResult<List<PublicSimpleProblemModel>> getSimpleProblemListByContestId(@PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getSimpleProblemListByContestId(contestId, userId));
    }

    @ApiOperation(value = "(题目)获取指定比赛下的指定题目详情信息", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true),
            @ApiImplicitParam(name = "problemId", value = "问题ID", dataType = "Long", paramType = "path", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=7)
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/contestInfo/{contestId}/problemInfo/{problemId}")
    public APIResult<DetailProblemModel> getDetailProblemInfoByIdOfContest(@PathVariable Long contestId, @PathVariable Long problemId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getDetailProblemInfoByIdOfContest(problemId, contestId, userId));
    }

    @ApiOperation(value = "(评测)分页获取指定比赛下的评测状态列表", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true),
    })
    @ApiResponses({})
    @ApiOperationSupport(order=8)
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/contestInfo/{contestId}/statusList")
    public APIResult<IPage<PublicSubmitModel>> getSubmitModelListByContestId(@RequestBody PageModel pageModel, @PathVariable Long contestId,  @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getSubmitModelListByContestId(pageModel, contestId, userId));
    }

    @ApiOperation(value = "(分页评测)按筛选条件分页获取指定比赛下的评测状态列表", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "submitConditionPageModel", value = "分页参数与筛选参数model", dataType = "SubmitConditionPageModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "无分页参数")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/contestInfo/{contestId}/conditionalStatusList")
    public APIResult<IPage<PublicSubmitModel>> getSubmitModelListByConditionOfContest(@RequestBody SubmitConditionPageModel submitConditionPageModel, @PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getSubmitModelListByConditionOfContest(submitConditionPageModel, contestId, userId));
    }

    @ApiOperation(value = "(提交)提交比赛的问题", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "submitModel", value = "提交信息model", dataType = "SubmitModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "比赛不存在"),
            @ApiResponse(code = 2001, message = "题目不存在"),
            @ApiResponse(code = 2001, message = "代码语言不合法或不支持")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/contestInfo/{contestId}/submit")
    public APIResult submitProblemOfContest(@RequestBody SubmitModel submitModel, @PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        contestService.submitProblemOfContest(submitModel, contestId, userId);
        return new APIResult<>();
    }

    @ApiOperation(value = "(排名)获取实时排名", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=9)
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/contestInfo/{contestId}/rank")
    public APIResult getRankItemListByContestId(@RequestBody PageModel pageModel, @PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getRankItemListByContestId(pageModel, contestId, userId));
    }

}
