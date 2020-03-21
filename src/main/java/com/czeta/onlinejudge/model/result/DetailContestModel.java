package com.czeta.onlinejudge.model.result;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName DetailContestModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/19 21:18
 * @Version 1.0
 */
@ApiModel(description = "比赛详情model")
@Data
public class DetailContestModel {
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
    @ApiModelProperty(value = "排名模式：练习、积分、ACM/ICPC。分别代表的业务规则：(1)练习：ac数目降序，wa的次数升序(2)积分：ac数目降序，花费时间升序（罚时错一次罚时20分钟）(3)ACM/ICPC：ac数目降序，花费时间升序（罚时错一次罚时20分钟）")
    private String rankModel;
    @ApiModelProperty(value = "是否实时排名：1表示是，0表示否")
    private Integer realtimeRank;
    @ApiModelProperty(value = "比赛创建者")
    private String creator;

    @ApiModelProperty(value = "比赛权限：是否已经报名并通过")
    private Boolean permission;
    @ApiModelProperty(value = "比赛运行状态：是否已经开始（包括进行中和已结束）")
    private Boolean afterStart;
}
