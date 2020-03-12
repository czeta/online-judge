package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

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
    private String inputDescription;
    private String outputDescription;
    private String inputSamples;
    private String outputSamples;
    private String hint;
    private String sourceName;
    private String timeLimit;
    private String memoryLimit;
    private String ioMode;
    private String level;
    private List<String> language;
    private Integer submitCount;
    private Integer acCount;
    private Integer acNum;
    private String creator;
    private Short status;
    private String crtTs;
    private String lmTs;

    public void setLanguage(List<String> language) {
        this.language = language;
    }
    public void setLanguage(String language) {
        this.language = Arrays.asList(language.split(","));
    }
}
