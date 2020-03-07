package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName Admin
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:01
 * @Version 1.0
 */
@ApiModel(description = "管理员信息")
@Data
public class Admin {
    @ApiModelProperty(value = "管理员ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "管理员用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "管理员信息描述")
    private String description;
    @ApiModelProperty(value = "角色ID")
    private Integer roleId;
    @ApiModelProperty(value = "账号状态：0表示禁用、1表示启用")
    private Short status;
    @ApiModelProperty(value = "创建时间")
    private String crtTs;
    @ApiModelProperty(value = "信息最后修改时间")
    private String lmTs;
}
