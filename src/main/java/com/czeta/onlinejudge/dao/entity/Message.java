package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName Message
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:04
 * @Version 1.0
 */
@Data
public class Message {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String creator;
    private Long userId;
    private Short status;
    private String crtTs;
    private String lmTs;
}
