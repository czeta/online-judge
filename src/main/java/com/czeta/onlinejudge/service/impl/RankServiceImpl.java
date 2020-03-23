package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.convert.RankMapstructConvert;
import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.mapper.UserMapper;
import com.czeta.onlinejudge.enums.CommonItemStatus;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.result.PublicRankModel;
import com.czeta.onlinejudge.model.result.PublicSimpleProblemModel;
import com.czeta.onlinejudge.service.RankService;
import com.czeta.onlinejudge.utils.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RankServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/20 10:48
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<PublicRankModel> getPublicRankModelListOfAcRank(PageModel pageModel) {
        Page<User> page = new Page<>(pageModel.getOffset(), pageModel.getLimit());
        IPage<User> userIPage = userMapper.selectPage(page, Wrappers.<User>lambdaQuery()
                .eq(User::getStatus, CommonItemStatus.ENABLE.getCode())
                .orderByDesc(User::getAcNum)
                .orderByAsc(User::getSubmitCount));
        List<PublicRankModel> list = new ArrayList<>();
        for (User user : userIPage.getRecords()) {
            PublicRankModel publicRankModel = RankMapstructConvert.INSTANCE.UserInfoToPublicRankModel(user);
            list.add(publicRankModel);
        }
        return PageUtils.setOpr(userIPage, new Page<PublicRankModel>(), list);
    }

    @Override
    public IPage<PublicRankModel> getPublicRankModelListOfRatingRank(PageModel pageModel) {
        Page<User> page = new Page<>(pageModel.getOffset(), pageModel.getLimit());
        IPage<User> userIPage = userMapper.selectPage(page, Wrappers.<User>lambdaQuery()
                .eq(User::getStatus, CommonItemStatus.ENABLE.getCode())
                .orderByAsc(User::getRank)
                .orderByDesc(User::getAcNum)
                .orderByAsc(User::getSubmitCount));
        List<PublicRankModel> list = new ArrayList<>();
        for (User user : userIPage.getRecords()) {
            PublicRankModel publicRankModel = RankMapstructConvert.INSTANCE.UserInfoToPublicRankModel(user);
            list.add(publicRankModel);
        }
        return PageUtils.setOpr(userIPage, new Page<PublicRankModel>(), list);
    }
}
