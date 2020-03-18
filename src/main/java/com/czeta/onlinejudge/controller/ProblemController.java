package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.ProblemConditionPageModel;
import com.czeta.onlinejudge.model.param.SubmitModel;
import com.czeta.onlinejudge.model.result.DetailProblemModel;
import com.czeta.onlinejudge.model.result.PublicSimpleProblemModel;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.utils.response.APIResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/problemList")
    public APIResult<IPage<PublicSimpleProblemModel>> getPublicProblemList(@RequestBody PageModel pageModel) {
        return new APIResult<>(problemService.getPublicProblemList(pageModel));
    }

    @GetMapping("/conditionalProblemList")
    public APIResult<IPage<PublicSimpleProblemModel>> getPublicProblemList(@RequestBody ProblemConditionPageModel problemConditionPageModel) {
        return new APIResult<>(problemService.getPublicProblemListByCondition(problemConditionPageModel));
    }

    @GetMapping("/problemInfo/{problemId}")
    public APIResult<DetailProblemModel> getDetailProblemInfo(@PathVariable Long problemId) {
        return new APIResult<>(problemService.getDetailProblemInfoById(problemId));
    }

    @PostMapping("/submit")
    public APIResult submitProblem(@RequestBody SubmitModel submitModel, @RequestAttribute Long userId) {
        problemService.submitProblem(submitModel, userId);
        return new APIResult();
    }
}
