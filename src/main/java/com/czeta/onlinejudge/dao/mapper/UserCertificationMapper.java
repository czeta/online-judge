package com.czeta.onlinejudge.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czeta.onlinejudge.dao.entity.UserCertification;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @InterfaceName UserCertificationMapper
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 16:00
 * @Version 1.0
 */
@Repository
public interface UserCertificationMapper extends BaseMapper<UserCertification> {


    @Select("SELECT * FROM user_certification uc INNER JOIN user u ON uc.user_id=u.id INNER JOIN certification c ON uc.certification_id=c.id WHERE uc.status = 0 ORDER BY uc.lm_ts DESC")
    List<AppliedCertificationModel> selectAppliedCertification();
}
