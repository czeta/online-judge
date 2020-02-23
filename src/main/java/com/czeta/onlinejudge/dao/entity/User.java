package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
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
    private Integer status;
    private String crtTs;
    private String lmTs;
}
