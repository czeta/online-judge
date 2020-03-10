package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

/**
 * @EnumName AnnouncementType
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 13:51
 * @Version 1.0
 */
public enum AnnouncementType implements IEnumItem {
    HOME_PAGE(-1, "首页公告"),
    FAQ(0, "FAQ页面公告");

    private Integer code;
    private String message;

    private AnnouncementType(Integer code, String message) {
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
