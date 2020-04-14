package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.Announcement;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.service.AnnouncementService;
import com.czeta.onlinejudge.utils.response.APIResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AnnouncementController
 * @Description 公告控制器：首页与FAQ
 * @Author chenlongjie
 * @Date 2020/3/17 12:50
 * @Version 1.0
 */
@Slf4j
@Api(tags = "Announcement Controller")
@RestController
@RequestMapping("/api/anc")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @ApiOperation(value = "分页获得主页公告信息列表", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @PostMapping("/ancInfoList")
    public APIResult<IPage<Announcement>> getHomePageAnnouncementList(@RequestBody PageModel pageModel) {
        return new APIResult<>(announcementService.getPublicHomePageAnnouncementList(pageModel));
    }


    @ApiOperation(value = "根据公告ID获取首页公告信息", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "公告ID", dataType = "Long", paramType = "path", required = true)})
    @ApiResponses({})
    @GetMapping("/ancInfo/{id}")
    public APIResult<Announcement> getHomePageAnnouncement(@PathVariable Long id) {
        return new APIResult<>(announcementService.getAnnouncementInfoById(id));
    }
}
