package com.czeta.onlinejudge.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czeta.onlinejudge.dao.entity.User;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserMapper
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:39
 * @Version 1.0
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE user SET submit_count = submit_count + 1 WHERE id = #{userId}")
    int updateSubmitCountIncrementOne(Long userId);

    @Update("UPDATE user SET ac_num = ac_num + 1 WHERE id = #{userId}")
    int updateAcNumIncrementOne(Long userId);
}
