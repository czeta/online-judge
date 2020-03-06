package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.Announcement;
import com.czeta.onlinejudge.model.param.AnnouncementModel;

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
     * 获取首页公告列表
     * @return
     */
    List<Announcement> getHomePageAnnouncementList();

    /**
     * 根据公告id获取公告详情
     * @param id
     * @return
     */
    Announcement getHomePageAnnouncementById(Long id);

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
}
