package cn.edu.bit.cs.moecleaner;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import junit.framework.Test;

import java.io.RandomAccessFile;
import java.util.Random;

import cn.edu.bit.cs.moecleaner.databinding.ActivityTestBinding;
import cn.edu.bit.cs.moecleaner.systemmonitor.CpuMonitor;
import cn.edu.bit.cs.moecleaner.ui.customview.LineChart;

/**
 * Created by entalent on 2016/4/14.
 * 测试用Activity
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        try {
            RandomAccessFile file = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq", "r");
            binding.setInfo(file.readLine());
            file.close();
        }catch (Exception e) {
            binding.setInfo(e.getMessage());
        }
    }
}
