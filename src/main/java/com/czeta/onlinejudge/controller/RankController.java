package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.result.PublicRankModel;
import com.czeta.onlinejudge.service.RankService;
import com.czeta.onlinejudge.utils.response.APIResult;
import io.swagger.annotations.Api;
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

    @PostMapping("/ac")
    public APIResult<IPage<PublicRankModel>> getPublicRankModelListOfAcRank(@RequestBody PageModel pageModel) {
        return new APIResult<>(rankService.getPublicRankModelListOfAcRank(pageModel));
    }

    @PostMapping("/rating")
    public APIResult<IPage<PublicRankModel>> getPublicRankModelListOfRatingRank(@RequestBody PageModel pageModel) {
        return new APIResult<>(rankService.getPublicRankModelListOfRatingRank(pageModel));
    }

}
