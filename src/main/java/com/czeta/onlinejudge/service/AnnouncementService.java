package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.Announcement;
import com.czeta.onlinejudge.model.param.AnnouncementModel;
import com.czeta.onlinejudge.model.param.PageModel;

import java.util.List;

/**
 * @InterfaceName AnnouncementService
 * @Description 公告服务
 * @Author chenlongjie
 * @Date 2020/3/5 11:25
 * @Version 1.0
 */
public interface AnnouncementService {
    /**
     * 根据ID获取公告详情
     * @param id
     * @return
     */
    Announcement getAnnouncementInfoById(Long id);

    /**
     * 获取首页公告列表
     * @return
     */
    IPage<Announcement> getHomePageAnnouncementList(PageModel pageModel);

    /**
     * 添加新的首页公告信息
     * @param announcementModel
     * @param adminId
     */
    void saveNewHomePageAnnouncement(AnnouncementModel announcementModel, Long adminId);

    /**
     * 更新首页公告信息
     * @param announcementModel
     * @return
     */
    boolean updateHomePageAnnouncement(AnnouncementModel announcementModel);

    /**
     * 获取FAQ内容
     * @return
     */
    String getFAQContent();

    /**
     * 修改FAQ内容
     * @param content
     * @return
     */
    boolean updateFAQContent(String content);

    /**
     * 添加比赛公告
     * @param announcementModel
     * @param contestId
     * @param adminId
     */
    void saveNewContestAnnouncement(AnnouncementModel announcementModel, Long contestId, Long adminId);

    /**
     * 更新比赛公告
     * @param announcementModel
     * @param contestId
     * @param adminId
     * @return
     */
    boolean updateContestAnnouncement(AnnouncementModel announcementModel, Long contestId, Long adminId);

    /**
     * 获取比赛公告列表
     * @return
     */
    List<Announcement> getContestAnnouncementList(Long contestId);

    /**
     * 获取公有界面的首页公告列表
     * @param pageModel
     * @return
     */
    IPage<Announcement> getPublicHomePageAnnouncementList(PageModel pageModel);

}
