package com.czeta.onlinejudge.config;

import com.czeta.onlinejudge.shiro.util.JwtTokenWebUtil;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Swagger2Config
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/6 9:48
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    /**
     * 标题
     */
    @Value("${online-judge.swagger.title}")
    private String title;

    /**
     * 基本包
     */
    @Value("${online-judge.swagger.base.package}")
    private String basePackage;

    /**
     * 描述
     */
    @Value("${online-judge.swagger.description}")
    private String description;

    /**
     * URL
     */
    @Value("${online-judge.swagger.url}")
    private String url;

    /**
     * 作者
     */
    @Value("${online-judge.swagger.contact.name}")
    private String contactName;

    /**
     * 作者网址
     */
    @Value("${online-judge.swagger.contact.url}")
    private String contactUrl;

    /**
     * 作者邮箱
     */
    @Value("${online-judge.swagger.contact.email}")
    private String contactEmail;

    /**
     * 版本
     */
    @Value("${online-judge.swagger.version}")
    private String version;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .globalResponseMessage(RequestMethod.GET, setResponseMessageList())
                .globalResponseMessage(RequestMethod.POST, setResponseMessageList())
                .globalOperationParameters(setHeaderToken())
                ;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(url)
                .contact(new Contact(contactName,contactUrl,contactEmail))
                .version(version)
                .build();
    }
    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();

        // token请求头
        String testTokenValue = "";
        ParameterBuilder tokenPar = new ParameterBuilder();
        Parameter tokenParameter = tokenPar
                .name(JwtTokenWebUtil.getTokenName())
                .description("Token Request Header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .defaultValue(testTokenValue)
                .build();
        pars.add(tokenParameter);
        return pars;
    }

    private List<ResponseMessage> setResponseMessageList() {
        final Integer SUCCESS_CODE = IBaseStatusMsg.APIEnum.SUCCESS.getCode();
        final Integer FAILED_CODE = IBaseStatusMsg.APIEnum.FAILED.getCode();
        final Integer PARAM_ERROR = IBaseStatusMsg.APIEnum.PARAM_ERROR.getCode();
        final Integer AUTHORITY_EXCEED_CODE = IBaseStatusMsg.APIEnum.AUTHORITY_EXCEED.getCode();
        final Integer LOGIN_AUTHORITY_EXCEED_CODE = IBaseStatusMsg.APIEnum.LOGIN_AUTHORITY_EXCEED.getCode();
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(SUCCESS_CODE).message("成功").build());
        responseMessageList.add(new ResponseMessageBuilder().code(FAILED_CODE).message("失败：信息根据返回message判断").build());
        responseMessageList.add(new ResponseMessageBuilder().code(PARAM_ERROR).message("请求参数错误：信息根据返回message判断").build());
        responseMessageList.add(new ResponseMessageBuilder().code(AUTHORITY_EXCEED_CODE).message("越权访问：用户没有权限").build());
        responseMessageList.add(new ResponseMessageBuilder().code(LOGIN_AUTHORITY_EXCEED_CODE).message("登录权限受限：请登录").build());
        return responseMessageList;
    }
}
