package cn.edu.bit.cs.moecleaner.systemmonitor;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by entalent on 2016/4/16.
 */
public class MemoryMonitor {

    public static ActivityManager.MemoryInfo getBriefMemoryInfo(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        return info;
    }
}
