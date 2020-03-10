package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.dao.mapper.*;
import com.czeta.onlinejudge.enums.*;
import com.czeta.onlinejudge.model.param.AdminRegisterModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.service.AdminService;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudge.utils.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @ClassName AdminServiceIml
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 14:39
 * @Version 1.0
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public IPage<Admin> getAdminInfoList(PageModel pageModel) {
        Page page = new Page(pageModel.getOffset(), pageModel.getLimit());
        IPage<Admin> adminIPage = adminMapper.selectPage(page, Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getRoleId, RoleType.COMMON_ADMIN.getCode()));
        adminIPage.setRecords(adminIPage.getRecords()
                .stream()
                .map(s -> {
                    s.setPassword(null);
                    return s;
                }).collect(Collectors.toList()));
        return adminIPage;
    }

    @Override
    public IPage<Admin> getAdminInfoListByUsernameKey(PageModel<String> pageModel) {
        AssertUtils.notNull(pageModel.getParamData(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        Page page = new Page(pageModel.getOffset(), pageModel.getLimit());
        IPage<Admin> adminIPage = adminMapper.selectPage(page, Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getRoleId, RoleType.COMMON_ADMIN.getCode())
                .like(Admin::getUsername, "%" + pageModel.getParamData() + "%"));
        adminIPage.setRecords(adminIPage.getRecords()
                .stream()
                .map(s -> {
                    s.setPassword(null);
                    return s;
                }).collect(Collectors.toList()));
        return adminIPage;
    }

    @Override
    public void saveNewAdmin(AdminRegisterModel adminRegisterModel) {
        AssertUtils.notNull(adminRegisterModel.getUsername(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(adminRegisterModel.getPassword(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, adminRegisterModel.getUsername()));
        AssertUtils.isNull(user, BaseStatusMsg.EXISTED_USERNAME);
        Admin admin = new Admin();
        admin.setUsername(adminRegisterModel.getUsername());
        admin.setPassword(PasswordUtils.encrypt(adminRegisterModel.getPassword(), jwtProperties.getSecret()));
        admin.setRoleId(RoleType.COMMON_ADMIN.getCode());
        admin.setDescription(adminRegisterModel.getDescription());
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_USERNAME);
        }
    }

    @Override
    public boolean resetAdminPasswordByUsername(String username) {
        AssertUtils.notNull(username, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Admin admin = new Admin();
        admin.setPassword(PasswordUtils.encrypt(username, jwtProperties.getSecret()));
        admin.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        adminMapper.update(admin, Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername, username));
        return true;
    }

    @Override
    public boolean disableAdminAccountByUsername(String username) {
        AssertUtils.notNull(username, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Admin admin = new Admin();
        admin.setStatus((short) 0);
        admin.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        adminMapper.update(admin, Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername, username));
        return true;
    }
}
