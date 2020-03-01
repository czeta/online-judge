package com.czeta.onlinejudge.dao.entity;

import lombok.Data;

/**
 * @ClassName JudgeType
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:24
 * @Version 1.0
 */
@Data
public class JudgeType {
    private Long id;
    private String name;
    private String reptileName;
    private String reptileUrl;
    private Short status;
    private String crtTs;
    private String lmTs;
}
