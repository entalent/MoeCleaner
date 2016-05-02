package cn.edu.bit.cs.moecleaner.cleaner;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.bit.cs.moecleaner.util.FileUtil;

/**
 * Created by entalent on 2016/4/27.
 */
public class CacheInfoProvider {

    Context context;

    ArrayList<PackageCacheItem> cacheInfoList;

    long totalCacheSize;

    public CacheInfoProvider(Context context) {
        this.context = context;
        cacheInfoList = new ArrayList<>();
    }

    public void scanPackageCacheFile(PackageInfo packageInfo) {
        System.out.println(packageInfo.packageName);
        File f1 = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + packageInfo.packageName + "/cache");
        File f2 = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + packageInfo.packageName + "/files");
        PackageCacheItem item = new PackageCacheItem();
        item.packageInfo = packageInfo;
        item.cacheFiles = new ArrayList<File>();
        item.cacheFilesSize = new ArrayList<>();
        if(f1.exists()) {
            item.cacheFiles.add(f1);
            long size1 = FileUtil.getFileOrDirectorySize(f1);
            item.cacheSize += size1;
            item.cacheFilesSize.add(size1);
        }
        if(f2.exists()) {
            item.cacheFiles.add(f2);
            long size2 = FileUtil.getFileOrDirectorySize(f2);
            item.cacheSize += size2;
            item.cacheFilesSize.add(size2);
        }
        if(item.cacheFiles.size() == 0){
            return;
        } else {
            totalCacheSize += item.cacheSize;
            cacheInfoList.add(item);
        }
    }

    /**
     * 每个应用的缓存信息
     */
    public static class PackageCacheItem {
        public PackageInfo packageInfo;
        public ArrayList<File> cacheFiles;
        public ArrayList<Long> cacheFilesSize;
        public long cacheSize;
    }

    public long getTotalCacheSize() {
        return totalCacheSize;
    }

    public ArrayList<PackageCacheItem> getCacheInfoList() {
        return cacheInfoList;
    }
}
