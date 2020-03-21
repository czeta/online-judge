package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.result.PublicRankModel;

/**
 * @InterfaceName RankService
 * @Description 排名服务：ac排名、rating排名
 * @Author chenlongjie
 * @Date 2020/3/20 10:48
 * @Version 1.0
 */
public interface RankService {
    /**
     * 分页获取AC Rank榜
     * @return
     */
    IPage<PublicRankModel> getPublicRankModelListOfAcRank(PageModel pageModel);

    /**
     * 分页获取Rating Rank榜
     * @param pageModel
     * @return
     */
    IPage<PublicRankModel> getPublicRankModelListOfRatingRank(PageModel pageModel);
}
