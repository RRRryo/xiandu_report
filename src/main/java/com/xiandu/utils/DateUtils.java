package com.xiandu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 12/15/2020.
 */
public class DateUtils {

    public static String getTodayIsoFormat() {
        Date date = new Date();
        String strDateFormat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(date);
    }
}
