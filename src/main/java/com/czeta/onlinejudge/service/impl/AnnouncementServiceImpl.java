package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.dao.entity.Admin;
import com.czeta.onlinejudge.dao.entity.Announcement;
import com.czeta.onlinejudge.dao.mapper.AdminMapper;
import com.czeta.onlinejudge.dao.mapper.AnnouncementMapper;
import com.czeta.onlinejudge.enums.AnnouncementType;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.CommonItemStatus;
import com.czeta.onlinejudge.model.param.AnnouncementModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.service.AnnouncementService;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @ClassName AnnouncementServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/5 11:25
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Announcement getAnnouncementInfoById(Long id) {
        return announcementMapper.selectOne(Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getId, id));
    }

    @Override
    public IPage<Announcement> getHomePageAnnouncementList(PageModel pageModel) {
        Page page = new Page(pageModel.getOffset(), pageModel.getLimit());
        return announcementMapper.selectPage(page, Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getSourceId, AnnouncementType.HOME_PAGE.getCode())
                .orderByDesc(Announcement::getStatus)
                .orderByDesc(Announcement::getLmTs));
    }

    @Override
    public void saveNewHomePageAnnouncement(AnnouncementModel announcementModel, Long adminId) {
        AssertUtils.notNull(announcementModel.getTitle(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(announcementModel.getContent(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(adminId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementModel.getTitle());
        announcement.setContent(announcementModel.getContent());
        announcement.setCreator(adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getId, adminId)).getUsername());
        announcement.setSourceId(Long.valueOf(AnnouncementType.HOME_PAGE.getCode()));
        announcementMapper.insert(announcement);
    }

    @Override
    public boolean updateHomePageAnnouncement(AnnouncementModel announcementModel) {
        Announcement announcement = new Announcement();
        announcement.setId(announcementModel.getId());
        announcement.setTitle(announcementModel.getTitle());
        announcement.setContent(announcementModel.getContent());
        announcement.setStatus(announcementModel.getStatus());
        announcement.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        announcementMapper.updateById(announcement);
        return true;
    }

    @Override
    public String getFAQContent() {
        return announcementMapper.selectOne(Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getSourceId, AnnouncementType.FAQ.getCode())
                .eq(Announcement::getStatus, 1)).getContent();
    }

    @Override
    public boolean updateFAQContent(String content) {
        Announcement announcement = new Announcement();
        announcement.setContent(content);
        announcement.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        announcementMapper.update(announcement, Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getSourceId, AnnouncementType.FAQ.getCode()));
        return true;
    }

    @Override
    public void saveNewContestAnnouncement(AnnouncementModel announcementModel, Long contestId, Long adminId) {
        AssertUtils.isTrue(announcementModel.getTitle() != null && announcementModel.getContent() != null
                && contestId != null && adminId != null, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementModel.getTitle());
        announcement.setContent(announcementModel.getContent());
        announcement.setCreator(adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getId, adminId)).getUsername());
        announcement.setSourceId(contestId);
        announcementMapper.insert(announcement);
    }

    @Override
    public boolean updateContestAnnouncement(AnnouncementModel announcementModel, Long contestId, Long adminId) {
        AssertUtils.isTrue(announcementModel.getId() != null
                && contestId != null && adminId != null, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Announcement announcement = new Announcement();
        announcement.setId(announcementModel.getId());
        announcement.setTitle(announcementModel.getTitle());
        announcement.setContent(announcementModel.getContent());
        announcement.setStatus(announcementModel.getStatus());
        announcement.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        announcementMapper.updateById(announcement);
        return true;
    }

    @Override
    public List<Announcement> getContestAnnouncementList(Long contestId) {
        AssertUtils.notNull(contestId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        return announcementMapper.selectList(Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getSourceId, contestId));
    }

    @Override
    public IPage<Announcement> getPublicHomePageAnnouncementList(PageModel pageModel) {
        Page page = new Page(pageModel.getOffset(), pageModel.getLimit());
        return announcementMapper.selectPage(page, Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getSourceId, AnnouncementType.HOME_PAGE.getCode())
                .eq(Announcement::getStatus, CommonItemStatus.ENABLE.getCode())
                .orderByDesc(Announcement::getLmTs));
    }
}
