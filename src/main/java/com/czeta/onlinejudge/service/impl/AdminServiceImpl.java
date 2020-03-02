package com.czeta.onlinejudge.service.impl;

import com.czeta.onlinejudge.dao.entity.Certification;
import com.czeta.onlinejudge.dao.mapper.CertificationMapper;
import com.czeta.onlinejudge.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName AdminServiceIml
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 14:39
 * @Version 1.0
 */
public class AdminServiceImpl implements AdminService {

    @Autowired
    private CertificationMapper certificationMapper;

    @Override
    public List<Certification> getCertificationTypes() {
        return certificationMapper.selectList(null);
    }
}
