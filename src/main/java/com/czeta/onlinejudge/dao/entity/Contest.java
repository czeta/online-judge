package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "比赛ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "比赛标题")
    private String title;
    @ApiModelProperty(value = "比赛描述")
    private String description;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "报名规则：公开、认证、密码。分别代表的业务逻辑：(1)公开：不用认证与审核(2)认证：认证+审核(3)密码：成功输入密码即可成功")
    private String signUpRule;
    @ApiModelProperty(value = "密码：当报名规则为密码时，该字段必须不能为null")
    private String password;
    @ApiModelProperty(value = "排名模式：练习、积分、ACM/ICPC。分别代表的业务规则：(1)练习：ac数目降序，wa的次数升序(2)积分：ac数目降序，花费时间升序（罚时错一次罚时20分钟）(3)ACM/ICPC：ac数目降序，花费时间升序（罚时错一次罚时20分钟）")
    private String rankModel;
    @ApiModelProperty(value = "是否实时排名：1表示是，0表示否")
    private Integer realtimeRank;
    @ApiModelProperty(value = "比赛创建者")
    private String creator;
    @ApiModelProperty(value = "比赛状态：1代表正常启用，0代表下线弃用")
    private Short status;
    @ApiModelProperty(value = "比赛创建时间")
    private String crtTs;
    @ApiModelProperty(value = "比赛信息最后修改时间")
    private String lmTs;
}
