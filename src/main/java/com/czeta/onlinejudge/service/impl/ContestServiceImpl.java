package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.convert.ContestMapstructConvert;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.dao.mapper.AdminMapper;
import com.czeta.onlinejudge.dao.mapper.ContestMapper;
import com.czeta.onlinejudge.dao.mapper.ContestUserMapper;
import com.czeta.onlinejudge.dao.mapper.ProblemMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.ContestRankModel;
import com.czeta.onlinejudge.enums.ContestSignUpRule;
import com.czeta.onlinejudge.model.param.ContestModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.result.PublicSimpleProblemModel;
import com.czeta.onlinejudge.model.result.SimpleContestModel;
import com.czeta.onlinejudge.service.ContestService;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudge.utils.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ContestServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/15 17:48
 * @Version 1.0
 */
@Transactional
@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ContestUserMapper contestUserMapper;

    @Override
    public void saveNewContest(ContestModel contestModel, Long adminId) {
        AssertUtils.isTrue(ContestSignUpRule.isContain(contestModel.getSignUpRule()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "报名模式不存在");
        AssertUtils.isTrue(ContestRankModel.isContain(contestModel.getRankModel()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "排名模式不存在");
        AssertUtils.notNull(adminId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Contest contestInfo = ContestMapstructConvert.INSTANCE.contestModelToContest(contestModel);
        Admin admin = adminMapper.selectById(adminId);
        AssertUtils.notNull(admin, BaseStatusMsg.APIEnum.PARAM_ERROR);
        contestInfo.setCreator(admin.getUsername());
        try {
            contestMapper.insert(contestInfo);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_PROBLEM);
        }
    }

    @Override
    public boolean updateContestInfo(ContestModel contestModel, Long contestId, Long adminId) {
        AssertUtils.notNull(contestModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.isTrue(ContestSignUpRule.isContain(contestModel.getSignUpRule()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "报名模式不存在");
        AssertUtils.isTrue(ContestRankModel.isContain(contestModel.getRankModel()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "排名模式不存在");
        Contest contestInfo = ContestMapstructConvert.INSTANCE.contestModelToContest(contestModel);
        contestInfo.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        contestMapper.updateById(contestInfo);
        return true;
    }

    @Override
    public IPage<SimpleContestModel> getSimpleContestList(PageModel pageParam) {
        Page<Contest> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        IPage<Contest> contestIPage = contestMapper.selectPage(page, Wrappers.<Contest>lambdaQuery()
                .orderByAsc(Contest::getCrtTs));
        List<SimpleContestModel> list = new ArrayList<>();
        for (Contest c : contestIPage.getRecords()) {
            list.add(ContestMapstructConvert.INSTANCE.contestToSimpleContestModel(c));
        }
        return PageUtils.setOpr(contestIPage, new Page<SimpleContestModel>(), list);
    }

    @Override
    public Contest getContestInfo(Long contestId) {
        return contestMapper.selectById(contestId);
    }

    @Override
    public List<Long> getProblemListOfContest(Long contestId) {
        Contest contestInfo = getContestInfo(contestId);
        return problemMapper.selectList(Wrappers.<Problem>lambdaQuery()
                .eq(Problem::getSourceId, contestInfo.getId())
                .orderByAsc(Problem::getCrtTs))
                .stream()
                .map(Problem::getId)
                .collect(Collectors.toList());
    }

    @Override
    public IPage<ContestUser> getAppliedContestUserList(PageModel pageParam, Long contestId) {
        Page<ContestUser> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        return contestUserMapper.selectPage(page, Wrappers.<ContestUser>lambdaQuery()
                .eq(ContestUser::getContestId, contestId)
                .orderByAsc(ContestUser::getCrtTs));
    }

    @Override
    public boolean updateAppliedContestUser(Short status, Long id, Long contestId, Long adminId) {
        ContestUser contestUser = new ContestUser();
        contestUser.setId(id);
        contestUser.setStatus(status);
        contestUser.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        contestUserMapper.updateById(contestUser);
        return true;
    }
}
