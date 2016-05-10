package cn.edu.bit.cs.moecleaner.util;

import android.content.pm.PackageManager;

/**
 * Created by entalent on 2016/5/2.
 */
public class PackageInfoUtil {
    public static boolean isValidPackageName(PackageManager pm, String packageName, int flag) {
        try {
            pm.getPackageInfo(packageName, flag);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
