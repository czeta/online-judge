package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName Judge
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:29
 * @Version 1.0
 */
@Data
public class Submit {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long problemId;
    private String code;
    private String msg;
    private String time;
    private String memory;
    private String language;
    private String submitStatus;
    private Long sourceId;
    private Long creatorId;
    private String creator;
    private Short status;
    private String crtTs;
    private String lmTs;
}
