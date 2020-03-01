package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName ContestUser
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:33
 * @Version 1.0
 */
@Data
public class ContestUser {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long contestId;
    private Long userId;
    private Short status;
    private String crtTs;
    private String lmTs;
}
