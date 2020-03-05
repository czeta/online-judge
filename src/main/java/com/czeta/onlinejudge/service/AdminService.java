package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.model.param.AdminRegisterModel;

import java.util.List;

/**
 * @InterfaceName AdminService
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 14:37
 * @Version 1.0
 */
public interface AdminService {

    /**
     * 获取普通管理员信息列表
     * @return
     */
    List<Admin> getAdminInfoList();

    /**
     * 获取有关普通管理员信息列表通过用户名关键字
     * @param usernameKey
     * @return
     */
    List<Admin> getAdminInfoListByUsernameKey(String usernameKey);

    /**
     * 添加新的普通管理员
     * @param adminRegisterModel
     */
    void saveNewAdmin(AdminRegisterModel adminRegisterModel);

    /**
     * 重置管理员密码，与用户名一致
     * @param username
     * @return
     */
    boolean resetAdminPasswordByUsername(String username);

    /**
     * 禁用管理员账号
     * @param username
     * @return
     */
    boolean disableAdminAccountByUsername(String username);

}
