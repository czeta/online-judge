package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ContestModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/16 9:13
 * @Version 1.0
 */
@ApiModel(description = "比赛model")
@Data
public class ContestModel {
    @ApiModelProperty(value = "比赛ID：如果是创建的操作，这部分置为null；如果是更新的操作这部分必须有")
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
    @ApiModelProperty(value = "比赛状态：-1表示下线，0表示已经结束封榜不能进行任何更改，1表示正常")
    private Short status;
}
