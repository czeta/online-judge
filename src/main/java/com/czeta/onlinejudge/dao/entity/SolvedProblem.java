package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName SolvedProblem
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 12:58
 * @Version 1.0
 */
@Data
public class SolvedProblem {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long problemId;
    private Long userId;
    private String submitStatus;
    private Short status;
    private String crtTs;
    private String lmTs;
}
