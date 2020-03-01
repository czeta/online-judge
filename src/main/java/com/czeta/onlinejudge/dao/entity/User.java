package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @ClassName User
 * @Description 用户实体
 * @Author chenlongjie
 * @Date 2020/2/29 23:00
 * @Version 1.0
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String github;
    private String blog;
    private String headPortrait;
    private String mood;
    private Integer submitCount;
    private Integer acNum;
    private Integer ratingNum;
    private Integer ratingScore;
    @TableField("`rank`")
    private Integer rank;
    private Long roleId;
    private Short status;
    private String crtTs;
    private String lmTs;
}
