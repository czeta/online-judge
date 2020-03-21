package com.czeta.onlinejudge.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czeta.onlinejudge.dao.entity.Contest;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @InterfaceName ContestMapper
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/16 10:18
 * @Version 1.0
 */
@Repository
public interface ContestMapper extends BaseMapper<Contest> {
    @Select("SELECT id FROM Contest WHERE status = 1 AND UNIX_TIMESTAMP(start_time) > #{currentTime}")
    List<Long> selectContestIdsBeforeRunning(Long currentTime);

    @Select("SELECT id FROM Contest WHERE status = 1 AND UNIX_TIMESTAMP(start_time) <= #{currentTime} AND UNIX_TIMESTAMP(end_time) >= #{currentTime}")
    List<Long> selectContestIdsRunning(Long currentTime);

    @Select("SELECT id FROM Contest WHERE status = 1 AND UNIX_TIMESTAMP(end_time) < #{currentTime}")
    List<Long> selectContestIdsAfterRunning(Long currentTime);
}
