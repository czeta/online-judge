package com.czeta.onlinejudge.utils.enums;

/**
 * @InterfaceName IBaseStatusMsg
 * @Description 基础状态码
 * @Author chenlongjie
 * @Date 2020/2/23 12:56
 * @Version 1.0
 */
public interface IBaseStatusMsg extends IEnumItem<Integer, String> {
    Integer getCode();
    String getMessage();

    public static enum APIEnum implements IBaseStatusMsg {
        SUCCESS(0, "成功"),
        /**<== 服务器端 ==>**/
        FAILED(1001, "失败"),
        SERVER_ERROR(1002, "服务器好像开小差了"),
        METHOD_WITHOUT_IMPLEMENT(1003, "该方法暂未实现"),
        RESP_FIELD_VALID(1004, "返回值校验失败"),
        /**<== 客户端 ==>**/
        PARAM_ERROR(2001, "请求参数错误"),
        PARAM_RESOLVER_ERROR(2002, "非法的请求参数格式"),
        AUTHORITY_EXCEED(2003, "越权访问"),
        LOGIN_AUTHORITY_EXCEED(2004, "登录权限受限");


        private Integer code;
        private String message;

        private APIEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
