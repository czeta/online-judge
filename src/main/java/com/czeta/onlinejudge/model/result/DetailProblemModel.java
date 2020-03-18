package com.czeta.onlinejudge.model.result;

import com.czeta.onlinejudge.dao.entity.Submit;
import com.czeta.onlinejudge.dao.entity.Tag;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DetailProblemModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 14:32
 * @Version 1.0
 */
@Data
public class DetailProblemModel {
    private Long id;
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private String inputSamples;
    private String outputSamples;
    private Submit hint;
    private String sourceName;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String ioMode;
    private String level;
    private String language;
    private String creator;

    private Integer submitCount;
    private Integer acCount;
    private Integer submitNum;
    private Integer acNum;

    private Map<String, Integer> statistic;

    private String judgeTypeName;
    private Integer problemType;
    private Integer spj;

    private List<ProblemTagModel> tagList;


}
