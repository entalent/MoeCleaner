package cn.edu.bit.cs.moecleaner.systemmonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public static final int getCpuUsage() throws Exception {
        int usagePercent = 0;
        ArrayList<String> output = ShellUtil.execCmd("top -n 1");
        for(String line : output) {
            if(line.trim().isEmpty()) continue;
            Matcher matcher = patternCpuUsage.matcher(line);
            while(matcher.find()) {
                String str = line.substring(matcher.start(), matcher.end() - 1);
                usagePercent += Integer.parseInt(str);
            }
            break;
        }
        return usagePercent;
    }

    /**
     *
     * @return the total number of CPU
     * @throws Exception
     */
    public static final int getCpuCount() throws Exception {
        if(cpuCount <= 0) {
            ArrayList<String> output = ShellUtil.execCmd("ls /sys/devices/system/cpu");
            for (String line : output) {
                if (line.trim().isEmpty()) continue;
                Matcher matcher = patternCpuCount.matcher(line);
                if (matcher.matches()) {
                    cpuCount++;
                }
            }
        }
        return cpuCount;
    }


    public static final int getCpuMaxFreq(int cpuIndex) throws Exception {
        String cmd = "cat /sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/cpuinfo_max_freq";
        ArrayList<String> output = ShellUtil.execCmd(cmd);
        String maxFreqStr = output.get(0);
        int maxFreq = 0;
        try {
            maxFreq = Integer.parseInt(maxFreqStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxFreq;
    }

    public static final int getCpuMinFreq(int cpuIndex) throws Exception {
        String cmd = "cat /sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/cpuinfo_min_freq";
        ArrayList<String> output = ShellUtil.execCmd(cmd);
        String minFreqStr = output.get(0);
        int minFreq = 0;
        try {
            minFreq = Integer.parseInt(minFreqStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minFreq;
    }

    public static final int getCpuCurFreq(int cpuIndex) throws Exception {
        String cmd = "cat /sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/scaling_cur_freq";
        ArrayList<String> output = ShellUtil.execCmd(cmd);
        String curFreqStr = output.get(0);
        int curFreq = 0;
        try {
            curFreq = Integer.parseInt(curFreqStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curFreq;
    }

    public static String getCpuName() throws IOException {
        ArrayList<String> output = ShellUtil.execCmd("cat /proc/cpuinfo");
        for(String i : output) {
            String[] strs = i.split(":\\s+", 2);
            if(strs[0].equals("Hardware")) {
                return strs[1];
            }
        }
        return output.get(0).split(":\\s+", 2)[1];
    }
}
