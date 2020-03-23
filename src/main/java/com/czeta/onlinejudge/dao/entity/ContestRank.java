package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName ContestRank
 * @Description 比赛榜单持久化数据
 * @Author chenlongjie
 * @Date 2020/3/22 14:51
 * @Version 1.0
 */
@Data
public class ContestRank {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long contestId;
    private String rankJson;
    private Short status;
    private String crtTs;
    private String lmTs;
}
