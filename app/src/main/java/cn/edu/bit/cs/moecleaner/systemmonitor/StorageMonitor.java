package cn.edu.bit.cs.moecleaner.systemmonitor;

import android.content.Context;
import android.os.StatFs;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by entalent on 2016/4/16.
 */
public class StorageMonitor {

    public static final ArrayList<StatFs> getAllStatFs(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Method _getVolumePaths;
        ArrayList<StatFs> stats = new ArrayList<>();
        try {
            _getVolumePaths = storageManager.getClass().getMethod("getVolumePaths");
            String[] ret = (String[]) _getVolumePaths.invoke(storageManager);
            for(String i : ret) {
                try {
                    File file = new File(i);
                    if(!(file.exists() && file.canRead())) {
                        continue;
                    }
                    stats.add(new StatFs(i));
                } catch (Exception e) {
                    continue;
                }
            }
            return stats;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
