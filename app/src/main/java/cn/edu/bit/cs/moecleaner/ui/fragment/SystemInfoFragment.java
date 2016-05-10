package cn.edu.bit.cs.moecleaner.ui.fragment;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import cn.edu.bit.cs.moecleaner.R;
import cn.edu.bit.cs.moecleaner.SystemInfoFragmentBinding;
import cn.edu.bit.cs.moecleaner.systemmonitor.BasicInfo;
import cn.edu.bit.cs.moecleaner.systemmonitor.CpuMonitor;
import cn.edu.bit.cs.moecleaner.systemmonitor.MemoryMonitor;
import cn.edu.bit.cs.moecleaner.systemmonitor.customview.CustomGLSurfaceView;
import cn.edu.bit.cs.moecleaner.ui.customview.LineChartFull;
import cn.edu.bit.cs.moecleaner.util.TextUtil;

/**
 * Created by entalent on 2016/4/14.
 * 主界面第4个Fragment
 * 用于显示系统基本信息
 */
public class SystemInfoFragment extends BaseMoeFragment {
    View rootView;
    static final int MSG_UPDATE_LINECHART_CPU = 0x0,
                    MSG_UPDATE_LINECHART_MEM = 0x1,
                    MSG_INIT_LINECHART_CPU = 0x2,
                    MSG_INIT_LINECHART_MEM = 0x3;

    CustomGLSurfaceView glSurfaceView;

    LineChartFull lineChartCpu, lineChartMemory;

    ActivityManager activityManager;
    ActivityManager.MemoryInfo memoryInfo;

    SystemInfoFragmentBinding binding;

    UpdateThreadRunnable updateThreadRunnable = new UpdateThreadRunnable();
    Thread updateThread = new Thread(updateThreadRunnable);

    String phoneModel, screenResolution, androidVersion, imei, wlanMac, cpuModel, clockRange, memorySize;

    int currentCpuFreq, currentMemoryUsage;
    int cpuMinFreq, cpuMaxFreq;
    int cpuCoreNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_systeminfo, container, false);
        rootView = binding.getRoot();
        lineChartCpu = (LineChartFull) rootView.findViewById(R.id.chart_cpu_usage);
        lineChartMemory = (LineChartFull) rootView.findViewById(R.id.chart_memory_usage);
        lineChartCpu.setVisibility(View.GONE);
        lineChartMemory.setVisibility(View.GONE);
        return rootView;
    }

    int getMemoryUsagePercent() {
        if(activityManager == null)
            activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        if(memoryInfo == null)
            memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return 100 - (int) (100 * (float) memoryInfo.availMem / memoryInfo.totalMem);
    }

    void setUpBasicInfo () {
        cpuCoreNum = CpuMonitor.getCpuCount();
        cpuCoreNum = Math.max(1, cpuCoreNum);
        cpuMinFreq = CpuMonitor.getCpuMinFreq(0);
        cpuMaxFreq = CpuMonitor.getCpuMaxFreq(0);

        phoneModel = Build.BRAND + " " + Build.MODEL;
        try {
            screenResolution = BasicInfo.getScreenResolution();
        } catch (Exception e) {
            screenResolution = "N/A";
        }
        androidVersion = Build.VERSION.RELEASE;
        imei = BasicInfo.getImeiOrMeid(getContext());
        wlanMac = BasicInfo.getMacAddress();
        try {
            cpuModel = CpuMonitor.getCpuName();
        } catch (Exception e) {
            cpuModel = "N/A";
        }
        clockRange = TextUtil.formatFrequencyStr(cpuMinFreq) + " ~ " + TextUtil.formatFrequencyStr(cpuMaxFreq);
        memorySize = TextUtil.formatStorageSizeStr(MemoryMonitor.getBriefMemoryInfo(getContext()).totalMem);

        binding.setPhoneModel(phoneModel);
        binding.setScreenResolution(screenResolution);
        binding.setAndroidVersion(androidVersion);
        binding.setImei(imei);
        binding.setWlanMac(wlanMac);
        binding.setCpuModel(cpuModel);
        binding.setCpuCores(Integer.toString(cpuCoreNum));
        binding.setClockRange(clockRange);
        binding.setMemorySize(memorySize);

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.setGlRenderer(glSurfaceView.getGlRenderer());
                binding.setGlVendor(glSurfaceView.getGlVendor());
                binding.setGlVersion(glSurfaceView.getGlVersion());
            }
        }, 1000);

    }

    class UpdateThreadRunnable implements Runnable {
        @Override
        public void run() {
            boolean showCpuUsage = true, showMemoryUsage = true;
            try {
                setUpBasicInfo();
            } catch (Exception e) {
                e.printStackTrace();
                showCpuUsage = false;
                showMemoryUsage = false;
                return;
            }

            if(cpuMinFreq >= cpuMaxFreq) {
                showCpuUsage = false;
            }

            System.out.println(cpuMinFreq + " " + cpuMaxFreq + " " + showCpuUsage);

            if(showCpuUsage) {
                rootView.post(new Runnable() {
                    @Override
                    public void run() {
                        //init cpu
                        Message msg = new Message();
                        msg.what = MSG_INIT_LINECHART_CPU;
                        getFragmentHandler().sendMessage(msg);
                    }
                });

            }
            if(showMemoryUsage) {
                rootView.post(new Runnable() {
                    @Override
                    public void run() {
                        //init mem
                        Message msg = new Message();
                        msg.what = MSG_INIT_LINECHART_MEM;
                        getFragmentHandler().sendMessage(msg);
                    }
                });
            }

            while(showCpuUsage || showMemoryUsage) {
                if(showCpuUsage) {
                    try {
                        currentCpuFreq = CpuMonitor.getCpuCurFreq(0);
                        rootView.post(new Runnable() {
                            @Override
                            public void run() {
                                //update cpu
                                Message msg = new Message();
                                msg.what = MSG_UPDATE_LINECHART_CPU;
                                getFragmentHandler().sendMessage(msg);
                            }
                        });
                    } catch (Exception e) {
                        showCpuUsage = false;
                    }

                }
                if(showMemoryUsage) {
                    try {
                        currentMemoryUsage = getMemoryUsagePercent();
                        rootView.post(new Runnable() {
                            @Override
                            public void run() {
                                //update mem
                                Message msg = new Message();
                                msg.what = MSG_UPDATE_LINECHART_MEM;
                                getFragmentHandler().sendMessage(msg);
                            }
                        });
                    } catch (Exception e) {
                        showMemoryUsage = false;
                    }
                }

                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    };

    @Override
    public void handleMessageFromHandler(Message msg) {
        switch (msg.what) {
            case MSG_INIT_LINECHART_CPU:
                lineChartCpu.getLineChart().setYScope(cpuMinFreq, cpuMaxFreq);
                lineChartCpu.setTopText(TextUtil.formatFrequencyStr(cpuMaxFreq));
                lineChartCpu.setBottomText(TextUtil.formatFrequencyStr(cpuMinFreq));
                lineChartCpu.setVisibility(View.VISIBLE);
                break;
            case MSG_INIT_LINECHART_MEM:
                lineChartMemory.getLineChart().setYScope(0, 100);
                lineChartMemory.setVisibility(View.VISIBLE);
                break;
            case MSG_UPDATE_LINECHART_CPU:
                lineChartCpu.getLineChart().addData(currentCpuFreq);
                //lineChartCpu.setVisibility(View.VISIBLE);
                break;
            case MSG_UPDATE_LINECHART_MEM:
                lineChartMemory.getLineChart().addData(currentMemoryUsage);
                //lineChartMemory.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
        try {
            updateThread = new Thread(new UpdateThreadRunnable());
            updateThread.start();
            glSurfaceView = new CustomGLSurfaceView(getActivity());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(1, 1);
            ((ViewGroup) rootView).addView(glSurfaceView, lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
        try {
            updateThread.interrupt();
            updateThread = null;
            ((ViewGroup) rootView).removeView(glSurfaceView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
