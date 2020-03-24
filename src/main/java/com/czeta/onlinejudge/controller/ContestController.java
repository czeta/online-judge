package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.Announcement;
import com.czeta.onlinejudge.model.param.ContestConditionPageModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.SubmitConditionPageModel;
import com.czeta.onlinejudge.model.param.SubmitModel;
import com.czeta.onlinejudge.model.result.*;
import com.czeta.onlinejudge.service.ContestService;
import com.czeta.onlinejudge.utils.response.APIResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/contestList")
    public APIResult<IPage<PublicSimpleContestModel>> getPublicContestList(@RequestBody PageModel pageModel) {
        return new APIResult<>(contestService.getPublicContestList(pageModel));
    }

    @PostMapping("/conditionalContestList")
    public APIResult<IPage<PublicSimpleContestModel>> getPublicContestListByCondition(@RequestBody ContestConditionPageModel contestConditionPageModel) {
        return new APIResult<>(contestService.getPublicContestListByCondition(contestConditionPageModel));
    }

    @GetMapping("/contestInfo/{contestId}")
    public APIResult<DetailContestModel> getDetailContestInfoById(@PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getDetailContestInfoById(contestId, userId));
    }

    @PostMapping("/contestInfo/{contestId}/signUp")
    public APIResult<Boolean> saveNewSignUpContest(@PathVariable Long contestId, @RequestParam String password, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.saveNewSignUpContest(contestId, password, userId));
    }

    @GetMapping("/contestInfo/{contestId}/announcements")
    public APIResult<List<Announcement>> getContestAnnouncementList(@PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getContestAnnouncementList(contestId, userId));
    }

    @GetMapping("/contestInfo/{contestId}/problemList")
    public APIResult<List<PublicSimpleProblemModel>> getSimpleProblemListByContestId(@PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getSimpleProblemListByContestId(contestId, userId));
    }

    @GetMapping("/contestInfo/{contestId}/problemInfo/{problemId}")
    public APIResult<DetailProblemModel> getDetailProblemInfoByIdOfContest(@PathVariable Long contestId, @PathVariable Long problemId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getDetailProblemInfoByIdOfContest(problemId, contestId, userId));
    }

    @PostMapping("/contestInfo/{contestId}/statusList")
    public APIResult<IPage<PublicSubmitModel>> getSubmitModelListByContestId(@RequestBody PageModel pageModel, @PathVariable Long contestId,  @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getSubmitModelListByContestId(pageModel, contestId, userId));
    }

    @PostMapping("/contestInfo/{contestId}/conditionalStatusList")
    public APIResult<IPage<PublicSubmitModel>> getSubmitModelListByConditionOfContest(@RequestBody SubmitConditionPageModel submitConditionPageModel, @PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getSubmitModelListByConditionOfContest(submitConditionPageModel, contestId, userId));
    }

    @PostMapping("/contestInfo/{contestId}/submit")
    public APIResult submitProblemOfContest(@RequestBody SubmitModel submitModel, @PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        contestService.submitProblemOfContest(submitModel, contestId, userId);
        return new APIResult<>();
    }

    @ApiOperation(value = "分页获得主页公告信息列表", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "path", required = true)
    })
    @ApiResponses({})
    @PostMapping("/contestInfo/{contestId}/rank")
    public APIResult getRankItemListByContestId(@RequestBody PageModel pageModel, @PathVariable Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.getRankItemListByContestId(pageModel, contestId, userId));
    }

}
