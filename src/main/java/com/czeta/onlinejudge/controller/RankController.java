package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.result.PublicRankModel;
import com.czeta.onlinejudge.service.RankService;
import com.czeta.onlinejudge.utils.response.APIResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName RankController
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/23 11:46
 * @Version 1.0
 */
@Slf4j
@Api(tags = "Rank Controller")
@RestController
@RequestMapping("/api/rank")
public class RankController {
    @Autowired
    private RankService rankService;

    @ApiOperation(value = "获取AC榜", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @PostMapping("/ac")
    public APIResult<IPage<PublicRankModel>> getPublicRankModelListOfAcRank(@RequestBody PageModel pageModel) {
        return new APIResult<>(rankService.getPublicRankModelListOfAcRank(pageModel));
    }

    @ApiOperation(value = "获取Rating榜", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @PostMapping("/rating")
    public APIResult<IPage<PublicRankModel>> getPublicRankModelListOfRatingRank(@RequestBody PageModel pageModel) {
        return new APIResult<>(rankService.getPublicRankModelListOfRatingRank(pageModel));
    }

}
