package cn.edu.bit.cs.moecleaner.systemmonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by entalent on 2016/4/16.
 */
public class CpuMonitor {

    static final Pattern pattern = Pattern.compile("\\d+%");

    public static final int getCpuUsage() throws Exception {
        Process process = Runtime.getRuntime().exec("top -n 1");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        int usagePercent = 0;
        while((line = bufferedReader.readLine()) != null) {
            if(line.trim().isEmpty()) continue;

            Matcher matcher = pattern.matcher(line);
            while(matcher.find()) {
                String str = line.substring(matcher.start(), matcher.end() - 1);
                usagePercent += Integer.parseInt(str);
            }
            break;
        }
        return usagePercent;
    }
}
