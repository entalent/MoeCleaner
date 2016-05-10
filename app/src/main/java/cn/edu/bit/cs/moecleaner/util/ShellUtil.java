package cn.edu.bit.cs.moecleaner.util;

import android.inputmethodservice.InputMethodService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by entalent on 2016/4/18.
 */
public class ShellUtil {

    /**
     *
     * @param cmd the command to execute
     * @return every line of the result of the command
     * @throws IOException
     */
    public static final ArrayList<String> execCmd(String cmd) throws IOException {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        ArrayList<String> ret = new ArrayList<>();
        String line = "";
        while((line = br.readLine()) != null) {
            ret.add(line);
        }
        return ret;
    }

    public static final boolean hasRootAccess() {
        System.out.println("test");
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("su -c");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String ret = "", line;
            while(null != (line = br.readLine())){
                ret += line;
            }
            System.out.println("len = " + ret.length());
            System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
