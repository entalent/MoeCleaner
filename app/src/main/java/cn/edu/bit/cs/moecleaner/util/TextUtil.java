package cn.edu.bit.cs.moecleaner.util;

/**
 * Created by entalent on 2016/4/16.
 */
public class TextUtil {
    static String[] UNIT_STR = {
            "B", "KB", "MB", "GB", "TB"
    };

    public static int UNIT_BYTE = 0,
                    UNIT_KILOBYTE = 1,
                    UNIT_MEGABYTE = 2,
                    UNIT_GIGABYTE = 3,
                    UNIT_TERABYTE = 4;


    public static final String formatStorageSizeStr (long sizeLong){
        double size = sizeLong;
        int unit = 0;
        while(unit < UNIT_STR.length && size >= 1024){
            size /= 1024.0;
            unit++;
        }
        return String.format("%.1f %s", size, UNIT_STR[unit]);
    }

    public static String formatStorageSizeStr (long sizeLong, int unit) {
        double size = sizeLong;
        size /= (1 << 10 * unit);
        return String.format("%.1f %s", size, UNIT_STR[unit]);
    }
}
