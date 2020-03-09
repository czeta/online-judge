package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName PageRequest
 * @Description 分页请求泛型类
 * @Author chenlongjie
 * @Date 2020/3/8 17:09
 * @Version 1.0
 */
@ApiModel(description = "分页请求泛型对象")
public class PageModel<T> {
    @ApiModelProperty(value = "请求数据")
    private T paramData;
    @ApiModelProperty(value = "当前页号")
    private Integer offset;
    @ApiModelProperty(value = "单页条数")
    private Integer limit;

    public T getParamData() {
        return paramData;
    }

    public void setParamData(T paramData) {
        this.paramData = paramData;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
