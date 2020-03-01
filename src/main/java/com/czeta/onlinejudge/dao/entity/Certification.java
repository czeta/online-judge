package com.czeta.onlinejudge.dao.entity;

import lombok.Data;

/**
 * @ClassName certification
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 12:57
 * @Version 1.0
 */
@Data
public class Certification {
    private Long id;
    private String name;
    private Short status;
    private String crtTs;
    private String lmTs;
}
