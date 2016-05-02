package cn.edu.bit.cs.moecleaner.util;

import android.app.PendingIntent;

import java.io.File;
import java.util.Stack;

/**
 * Created by entalent on 2016/4/27.
 */
public class FileUtil {
    /**
     * @param fileToDelete 删除一个文件或一个目录
     * @return 被删除的文件数量
     */
    public static int deleteFileOrDirectory(File fileToDelete) {
        //文件不存在或不可写
        if(!(fileToDelete.exists() && fileToDelete.canWrite())) {
            return 0;
        }
        if(fileToDelete.isFile()) {
            return fileToDelete.delete() ? 1 : 0;
        } else if(fileToDelete.isDirectory()) {
            int deleteCnt = 0;
            File[] files = fileToDelete.listFiles();
            for(File i : files) {
                deleteCnt += deleteFileOrDirectory(i);
            }
            deleteCnt += fileToDelete.delete() ? 1 : 0;
            return deleteCnt;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param file 要统计大小的文件
     * @return 文件或目录的大小
     */
    public static long getFileOrDirectorySize(File file) {
        long fileSize = 0;
        if(!(file.exists() && file.canRead())) {
            return fileSize;
        }
        if(file.isFile()) {
            fileSize = file.length();
        } else if(file.isDirectory()) {
            File[] files = file.listFiles();
            for(File i : files) {
                fileSize += getFileOrDirectorySize(i);
            }
        }
        return fileSize;
    }
}
