package com.appmodel.util;

import com.duowan.mobile.utils.YLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ItghostFan on 2014/12/18.
 */
public class TimeLogger
{
    public static void info(String className, String functionName, String doWhat)
    {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        YLog.info(className, "TimeLogger %s.%s %s time=%s", className, functionName, doWhat, dateFormat.format(date));
    }
}
