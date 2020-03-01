package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName Contest
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:31
 * @Version 1.0
 */
@Data
public class Contest {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String entryType;
    private String rankModel;
    private String runningStatus;
    private String creator;
    private Short status;
    private String crtTs;
    private String lmTs;
}
