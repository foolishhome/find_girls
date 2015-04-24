package com.appmodel.util;

import android.annotation.SuppressLint;
import android.os.Build;


import com.duowan.mobile.utils.YLog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class CpuInfoUtils {
    public static final int BETTER_CPU_DEVICE_FREQ = 900;
    public static final int BETTER_CPU_DEVICE_FREQ_BOGO = 600;

    public static String getMaxCpuFreq() {
        String result = "";
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            if (!StringUtils.isNullOrEmpty(text)) {
                result = text.trim();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    public static String getMinCpuFreq() {
        String result = "";
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            if (!StringUtils.isNullOrEmpty(text)) {
                result = text.trim();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            if (!StringUtils.isNullOrEmpty(text)) {
                result = text.trim();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            if (!StringUtils.isNullOrEmpty(text)) {
                String[] array = text.split(":\\s+", 2);
                if (array.length > 1) {
                    return array[1];
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String BogoMIPS = "BOGOMIPS";

    public static float getBogoMips() {
        float ret = -1;
        String cpuInfo = readCpuInfo();
        if (!StringUtils.isNullOrEmpty(cpuInfo)) {
            String bogoMips = null;
            BufferedReader br = new BufferedReader(new StringReader(cpuInfo));
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    if (line.toUpperCase().startsWith(BogoMIPS)) {
                        int index = line.indexOf(':');
                        bogoMips = line.substring(index + 1).trim();
                        break;
                    }
                }
                if (line != null) {
                    ret = Float.valueOf(bogoMips);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static String readCpuInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            while(!StringUtils.isNullOrEmpty(text)) {
                sb.append(text).append("\n");
                text = br.readLine();
            }
            br.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    private static final String PROCESSOR = "processor";

    @SuppressLint("DefaultLocale")
    public static boolean isMulCore() {
        int processorCount = 0;
        String cpuInfo = readCpuInfo();
        YLog.info(CpuInfoUtils.class, "cpuInfo: %s", cpuInfo);
        if (!StringUtils.isNullOrEmpty(cpuInfo)) {
            BufferedReader br = new BufferedReader(new StringReader(cpuInfo));
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    if (line.toLowerCase().startsWith(PROCESSOR)) {
                        processorCount++;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        YLog.info(CpuInfoUtils.class, "processor count: %d", processorCount);
        return processorCount > 1;
    }

    public static boolean powerfulCpuDevice() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            YLog.info(CpuInfoUtils.class, "sdk version > 3.0. Build.VERSION.SDK_INT: %d", Build.VERSION.SDK_INT);
            return true;
        }

        if (isMulCore()) {
            YLog.info(CpuInfoUtils.class, "multi core");
            return true;
        }

        String maxCpuFreq = CpuInfoUtils.getMaxCpuFreq();
        YLog.info(CpuInfoUtils.class, "maxCpuFreq: " + maxCpuFreq);
        if (!StringUtils.isNullOrEmpty(maxCpuFreq)) {
            int id = -1;
            try {
                id = Integer.valueOf(maxCpuFreq);
            }
            catch (NumberFormatException e) {
                id = -1;
            }
            YLog.info(CpuInfoUtils.class, "CpuDevice id: " + id);
            if (id != -1) {
                id = id / 1000;
                YLog.info(CpuInfoUtils.class, "powerful=%B", id > CpuInfoUtils.BETTER_CPU_DEVICE_FREQ);
                return id > CpuInfoUtils.BETTER_CPU_DEVICE_FREQ;
            }
        }

        float bogoMips = CpuInfoUtils.getBogoMips();
        YLog.info(CpuInfoUtils.class, "CPU bogoMips: " + bogoMips);
        YLog.info(CpuInfoUtils.class, "powerful=%B", bogoMips > CpuInfoUtils.BETTER_CPU_DEVICE_FREQ);
        return bogoMips > CpuInfoUtils.BETTER_CPU_DEVICE_FREQ_BOGO;
    }
}
