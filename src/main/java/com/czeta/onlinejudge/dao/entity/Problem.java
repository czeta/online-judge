package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName Problem
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:13
 * @Version 1.0
 */
@Data
public class Problem {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String input;
    private String output;
    private String sample_input;
    private String sample_output;
    private Long sourceId;
    private String timeLimit;
    private String memoryLimit;
    private String ioMode;
    private String level;
    private Integer submitCount;
    private Integer acCount;
    private Integer acNum;
    private String creator;
    private Short status;
    private String crtTs;
    private String lmTs;
}
