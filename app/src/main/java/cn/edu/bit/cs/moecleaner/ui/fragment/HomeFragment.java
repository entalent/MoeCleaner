package cn.edu.bit.cs.moecleaner.ui.fragment;

import android.app.ActivityManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cn.edu.bit.cs.moecleaner.R;
import cn.edu.bit.cs.moecleaner.ui.customview.CircleProgressBar;
import cn.edu.bit.cs.moecleaner.systemmonitor.CpuMonitor;
import cn.edu.bit.cs.moecleaner.systemmonitor.MemoryMonitor;
import cn.edu.bit.cs.moecleaner.systemmonitor.StorageMonitor;
import cn.edu.bit.cs.moecleaner.util.TextUtil;

/**
 * Created by entalent on 2016/4/14.
 */
public class HomeFragment extends BaseMoeFragment {

    View rootView;
    CircleProgressBar memoryProgressBar, storageProgressBar, cpuProgressBar;
    long totalMemory, usedMemory;
    long totalStorage, usedStorage;
    int cpuUsagePercent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootView.findViewById(R.id.item_junk_clean).setOnClickListener(mOnClickListener);
        rootView.findViewById(R.id.item_memory_clean).setOnClickListener(mOnClickListener);
        rootView.findViewById(R.id.item_system_info).setOnClickListener(mOnClickListener);
        rootView.findViewById(R.id.item_settings).setOnClickListener(mOnClickListener);

        memoryProgressBar = (CircleProgressBar) rootView.findViewById(R.id.view3);
        storageProgressBar = (CircleProgressBar) rootView.findViewById(R.id.view2);
        cpuProgressBar = (CircleProgressBar) rootView.findViewById(R.id.view4);
/*
        TypedArray arr = getActivity().getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.colorPrimary, android.R.attr.colorPrimaryDark, android.R.attr.colorAccent
        });
        memoryProgressBar.setCenterCircleColor(arr.getColor(0, ));
*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                initProgressBars();
            }
        }).start();



        return rootView;
    }

    private void initProgressBars() {
        ActivityManager.MemoryInfo memInfo = MemoryMonitor.getBriefMemoryInfo(getActivity());
        usedMemory = memInfo.totalMem - memInfo.availMem;
        totalMemory = memInfo.totalMem;

        long availableStorage = 0;
        totalStorage = 0;
        ArrayList<StatFs> statFsList = StorageMonitor.getAllStatFs(getActivity());
        for(StatFs i : statFsList) {
            totalStorage += ((long) i.getBlockCount()) * i.getBlockSize();
            availableStorage += ((long) i.getAvailableBlocks()) * i.getBlockSize();
        }
        usedStorage = totalStorage - availableStorage;

        storageProgressBar.setMainTitle(TextUtil.formatStorageSizeStr(usedStorage, TextUtil.UNIT_GIGABYTE) + "/" +
                TextUtil.formatStorageSizeStr(totalStorage, TextUtil.UNIT_GIGABYTE));
        storageProgressBar.setProgress((float) usedStorage / totalStorage);

        memoryProgressBar.setMainTitle(TextUtil.formatStorageSizeStr(usedMemory, TextUtil.UNIT_GIGABYTE) + "/" +
                TextUtil.formatStorageSizeStr(totalMemory, TextUtil.UNIT_GIGABYTE));
        memoryProgressBar.setProgress((float) usedMemory / totalMemory);

        cpuUsagePercent = 0;
        try {
            cpuUsagePercent = CpuMonitor.getCpuUsage();
        } catch (Exception e) {
            cpuProgressBar.setVisibility(View.INVISIBLE);
        }

        Message msg = new Message();
        msg.what = MSG_REFRESH_CPU_PROGRESSBAR;
        this.getFragmentHandler().sendMessage(msg);
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Message msg = new Message();
            msg.what = MSG_JUMPED_FROM_HOME_PAGE;
            if(viewPagerManager == null) {
                Toast.makeText(getActivity(), "NULL", Toast.LENGTH_SHORT).show();
                return;
            }
            switch ((String)v.getTag()) {
                case "junk_clean":
                    viewPagerManager.setCurrentItem(1);
                    break;
                case "memory_clean":
                    viewPagerManager.setCurrentItem(2);
                    break;
                case "system_info":
                    viewPagerManager.setCurrentItem(3);
                    break;
                case "settings":
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void handleMessageFromHandler(Message msg) {
        if(msg.what == MSG_REFRESH_CPU_PROGRESSBAR) {
            cpuProgressBar.setSubTitle(cpuUsagePercent + "%");
            cpuProgressBar.setProgress((float) cpuUsagePercent / 100.0f);
        }
    }
}
