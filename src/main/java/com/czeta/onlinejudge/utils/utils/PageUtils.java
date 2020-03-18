package com.czeta.onlinejudge.utils.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @ClassName PageUtils
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 11:29
 * @Version 1.0
 */
public class PageUtils {
    public static <S, T> IPage<T> setOpr(IPage<S> s, IPage<T> t, List<T> list) {
        t.setRecords(list);
        t.setTotal(s.getTotal());
        t.setSize(s.getSize());
        t.setCurrent(s.getCurrent());
        t.setPages(s.getPages());
        return t;
    }
}
