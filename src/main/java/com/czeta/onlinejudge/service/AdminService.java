package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.model.param.AdminRegisterModel;
import com.czeta.onlinejudge.model.param.PageModel;

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
    IPage<Admin> getAdminInfoList(PageModel pageModel);

    /**
     * 获取有关普通管理员信息列表通过用户名关键字
     * @param pageModel
     * @return
     */
    IPage<Admin> getAdminInfoListByUsernameKey(PageModel<String> pageModel);


    /**
     * 根据id获取管理员信息
     * @param adminId
     * @return
     */
    Admin getAdminInfoById(Long adminId);

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
     * 禁用/启用管理员账号
     * @param username
     * @return
     */
    boolean updateAdminAccount(String username, Short status);

}
