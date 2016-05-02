package cn.edu.bit.cs.moecleaner.util;

/**
 * Created by entalent on 2016/4/16.
 */
public class TextUtil {
    static String[] STORAGE_UNIT_STR = {
            "B", "KB", "MB", "GB", "TB"
    };

    static String[] FREQUENCY_UNIT_STR = {
            "KHz", "MHz", "GHz"
    };


    public static int UNIT_BYTE = 0,
                    UNIT_KILOBYTE = 1,
                    UNIT_MEGABYTE = 2,
                    UNIT_GIGABYTE = 3,
                    UNIT_TERABYTE = 4;


    public static final String formatStorageSizeStr (long sizeLong){
        double size = sizeLong;
        int unit = 0;
        while(unit < STORAGE_UNIT_STR.length && size >= 1024){
            size /= 1024.0;
            unit++;
        }
        return String.format("%.1f %s", size, STORAGE_UNIT_STR[unit]);
    }

    public static String formatStorageSizeStr (long sizeLong, int unit) {
        double size = sizeLong;
        size /= (1 << 10 * unit);
        return String.format("%.1f %s", size, STORAGE_UNIT_STR[unit]);
    }

    public static String formatFrequencyStr(int freqInKHz) {
        int u = 0;
        double freq = freqInKHz;
        while(u < FREQUENCY_UNIT_STR.length - 1 && freqInKHz >= 1000){
            freq /= 1000;
            u++;
        }
        return String.format("%.2f %s", freq, FREQUENCY_UNIT_STR[u]);
    }
}
