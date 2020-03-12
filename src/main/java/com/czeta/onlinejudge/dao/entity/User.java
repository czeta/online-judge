package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName User
 * @Description 用户实体
 * @Author chenlongjie
 * @Date 2020/2/29 23:00
 * @Version 1.0
 */
@ApiModel(description = "用户实体")
@Data
public class User {
    @ApiModelProperty(value = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "github地址")
    private String github;
    @ApiModelProperty(value = "博客地址")
    private String blog;
    @ApiModelProperty(value = "头像所在文件地址")
    private String headPortrait;
    @ApiModelProperty(value = "个性签名")
    private String mood;
    @ApiModelProperty(value = "提交题目的次数")
    private Integer submitCount;
    @ApiModelProperty(value = "通过题目的数量")
    private Integer acNum;
    @ApiModelProperty(value = "参加积分赛的数目")
    private Integer ratingNum;
    @ApiModelProperty(value = "积分")
    private Integer ratingScore;
    @ApiModelProperty(value = "积分排名")
    @TableField("`rank`")
    private Integer rank;
    @ApiModelProperty(value = "角色ID")
    private Integer roleId;
    @ApiModelProperty(value = "用户账户状态，1表示正常，0表示禁用")
    private Short status;
    @ApiModelProperty(value = "注册时间")
    private String crtTs;
    @ApiModelProperty(value = "用户信息最后修改时间")
    private String lmTs;
}
