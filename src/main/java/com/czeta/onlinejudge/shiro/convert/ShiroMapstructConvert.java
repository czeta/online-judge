package com.czeta.onlinejudge.shiro.convert;

import com.czeta.onlinejudge.dao.entity.Admin;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.shiro.jwt.JwtToken;
import com.czeta.onlinejudge.shiro.model.result.JwtTokenRedisModel;
import com.czeta.onlinejudge.shiro.model.result.LoginUserModel;
import com.czeta.onlinejudge.shiro.model.result.LoginUserRedisModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * @InterfaceName ShiroMapstructConvert
 * @Description Shiro包下使用mapstruct对象属性复制转换器
 * @Author chenlongjie
 * @Date 2020/2/23 12:56
 * @Version 1.0
 */
@Mapper
public interface ShiroMapstructConvert {

    /**
     * 转换器实例
     */
    ShiroMapstructConvert INSTANCE = Mappers.getMapper(ShiroMapstructConvert.class);

    /**
     * JwtToken对象转换成JwtTokenRedisModel
     * @param jwtToken
     * @return
     */
    JwtTokenRedisModel jwtTokenToJwtTokenRedisModel(JwtToken jwtToken);

    /**
     * 系统User对象转换成登录用户对象LoginUserModel
     * @param user
     * @return
     */
    LoginUserModel userToLoginUserModel(User user);

    /**
     * 系统Admin对象转换成登录用户对象LoginUserModel
     * @param admin
     * @return
     */
    LoginUserModel adminToLoginUserModel(Admin admin);

    /**
     * LoginUserModel对象转换成LoginUserRedisModel
     * @param loginUserModel
     * @return
     */
    LoginUserRedisModel loginSysUserVoToLoginUserRedisModel(LoginUserModel loginUserModel);
}
