package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.Certification;

import java.util.List;

/**
 * @InterfaceName AdminService
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 14:37
 * @Version 1.0
 */
public interface AdminService {
    List<Certification> getCertificationTypes();
}
