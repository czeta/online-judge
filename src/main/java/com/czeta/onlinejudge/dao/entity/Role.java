package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName Role
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:03
 * @Version 1.0
 */
@Data
public class Role {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String permissionCodes;
    private Short status;
    private String crtTs;
    private String lmTs;
}
