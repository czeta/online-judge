package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.Tag;
import com.czeta.onlinejudge.model.result.ProblemTagModel;

import java.util.List;

/**
 * @InterfaceName TagService
 * @Description 标签服务
 * @Author chenlongjie
 * @Date 2020/3/14 11:18
 * @Version 1.0
 */
public interface TagService {
    void saveNewTag(String name, Long adminId);

    List<Tag> getTagInfoList();

    List<ProblemTagModel> getProblemTagByProblemId(Long problemId);

    List<Long> getProblemIdListByTagId(Integer tagId);
}
