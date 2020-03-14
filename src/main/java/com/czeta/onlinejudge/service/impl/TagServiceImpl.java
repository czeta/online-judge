package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.dao.entity.Admin;
import com.czeta.onlinejudge.dao.entity.Tag;
import com.czeta.onlinejudge.dao.mapper.AdminMapper;
import com.czeta.onlinejudge.dao.mapper.ProblemTagMapper;
import com.czeta.onlinejudge.dao.mapper.TagMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.CommonItemStatus;
import com.czeta.onlinejudge.model.result.ProblemTagModel;
import com.czeta.onlinejudge.service.TagService;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName TagServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/14 11:19
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private ProblemTagMapper problemTagMapper;

    @Override
    public void saveNewTag(String tagName, Long adminId) {
        AssertUtils.notNull(adminId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Admin adminInfo = adminMapper.selectById(adminId);
        Tag tag = new Tag();
        tag.setCreator(adminInfo.getUsername());
        tag.setName(tagName);
        try {
            tagMapper.insert(tag);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_NAME);
        }
    }

    @Override
    public List<Tag> getTagInfoList() {
        return tagMapper.selectList(Wrappers.<Tag>lambdaQuery()
                .eq(Tag::getStatus, CommonItemStatus.ENABLE.getCode())
                .orderByDesc(Tag::getLmTs));
    }

    @Override
    public List<ProblemTagModel> getProblemTagByProblemId(Long problemId) {
        AssertUtils.notNull(problemId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        return problemTagMapper.selectProblemTagJoinTag(problemId);
    }
}
