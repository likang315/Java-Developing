package com.atlantis.zeus.base.utils;


import java.util.Calendar;
import java.util.Date;

/**
 * @author likang02@corp.netease.com
 * @date 2021-07-18 16:14
 */
public class DateUtils {
    /**
     * 获取前 x 天的时间。
     */
    public static Date getPastXDateTime(int x) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) - x);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTime();
    }
}
