package com.czeta.onlinejudge.utils.utils;

import java.text.DecimalFormat;

/**
 * @ClassName NumberUtils
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/17 14:46
 * @Version 1.0
 */
public class NumberUtils {
    /**
     * 计算百分制，保留后两位
     * @param dividend
     * @param divisor
     * @return
     */
    public static String parsePercent(Integer dividend, Integer divisor) {
        if (divisor == 0 || dividend == 0 || dividend > divisor) return "0.00%";
        double num = (dividend * 1.0) / (divisor * 1.0);
        DecimalFormat df = new DecimalFormat("##.00%");
        return df.format(num);
    }
}
