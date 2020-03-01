package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName ProblemTag
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:28
 * @Version 1.0
 */
@Data
public class ProblemTag {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long problemId;
    private Integer tagId;
    private Short status;
    private String crtTs;
    private String lmTs;
}
