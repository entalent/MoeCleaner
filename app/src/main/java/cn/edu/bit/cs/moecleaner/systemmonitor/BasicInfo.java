package cn.edu.bit.cs.moecleaner.systemmonitor;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import cn.edu.bit.cs.moecleaner.util.ShellUtil;

/**
 * Created by entalent on 2016/4/26.
 */
public class BasicInfo {

    public static String getBuildModel() {
        return Build.BRAND + " " + Build.MODEL;
    }

    public static String getMacAddress() {
        try {
            RandomAccessFile file = new RandomAccessFile("/sys/class/net/wlan0/address", "r");
            return file.readLine();
        } catch (Exception e) {
            return "N/A";
        }
    }

    public static String getImeiOrMeid(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            return "N/A";
        }
    }

    public static String getPhoneNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        } catch (Exception e) {
            return "N/A";
        }
    }

    public static String getScreenResolution() {
        try {
            ArrayList<String> res = ShellUtil.execCmd("wm size");
            return res.get(0).substring(res.get(0).indexOf(':') + 2);
        } catch (Exception e) {
            return "N/A";
        }
    }

    public static String getAllSystemInfo(Context context) {
        String jsonStr;
        JSONObject obj = new JSONObject();
        try {
            obj.put("BRAND", Build.BRAND);
            obj.put("MODEL", Build.MODEL);
            obj.put("ANDROID_VERSION", Build.VERSION.RELEASE);
            obj.put("IMEI", getImeiOrMeid(context));
            obj.put("MACADDR", getMacAddress());
            obj.put("PHONENUMBER", getPhoneNumber(context));
        } catch (Exception e) {

        }
        jsonStr = obj.toString();
        return jsonStr;
    }

}
