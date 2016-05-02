package cn.edu.bit.cs.moecleaner.systemmonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.bit.cs.moecleaner.util.ShellUtil;

/**
 * Created by entalent on 2016/4/16.
 */
public class CpuMonitor {

    static final Pattern patternCpuUsage = Pattern.compile("\\d+%");
    static final Pattern patternCpuCount = Pattern.compile("cpu[0-9]+");

    static int cpuCount = 0;
    /**
     *
     * @return CPU usage, a integer between 0 and 100.
     * @throws Exception
     */
    public static final int getCpuUsage() {
        try {
            int usagePercent = 0;
            ArrayList<String> output = ShellUtil.execCmd("top -n 1");
            for (String line : output) {
                if (line.trim().isEmpty()) continue;
                Matcher matcher = patternCpuUsage.matcher(line);
                while (matcher.find()) {
                    String str = line.substring(matcher.start(), matcher.end() - 1);
                    usagePercent += Integer.parseInt(str);
                }
                break;
            }
            return usagePercent;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     *
     * @return the total number of CPU
     * @throws Exception
     */
    public static final int getCpuCount() {
        if(cpuCount <= 0) {
            try {
                ArrayList<String> output = ShellUtil.execCmd("ls /sys/devices/system/cpu");
                for (String line : output) {
                    if (line.trim().isEmpty()) continue;
                    Matcher matcher = patternCpuCount.matcher(line);
                    if (matcher.matches()) {
                        cpuCount++;
                    }
                }
            } catch (Exception e){
                return cpuCount;
            }
        }
        return cpuCount;
    }


    public static final int getCpuMaxFreq(int cpuIndex) {
        try {
            RandomAccessFile file = new RandomAccessFile("/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/cpuinfo_max_freq", "r");
            int ret = Integer.parseInt(file.readLine());
            file.close();
            return ret;
        } catch (Exception e) {
            return 0;
        }
    }

    public static final int getCpuMinFreq(int cpuIndex) {
        try {
            RandomAccessFile file = new RandomAccessFile("/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/cpuinfo_min_freq", "r");
            int ret = Integer.parseInt(file.readLine());
            file.close();
            return ret;
        } catch (Exception e) {
            return 0;
        }
    }

    public static final int getCpuCurFreq(int cpuIndex) {
        try {
            RandomAccessFile file = new RandomAccessFile("/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/scaling_cur_freq", "r");
            int ret = Integer.parseInt(file.readLine());
            file.close();
            return ret;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getCpuName() {
        try {
            RandomAccessFile file = new RandomAccessFile("/proc/cpuinfo", "r");
            String line;
            while ((line = file.readLine()) != null) {
                String[] strs = line.split(":\\s+", 2);
                if (strs[0].contains("Hardware")) {
                    return strs[1];
                }
            }
            return "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }
}
