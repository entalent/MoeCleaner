package cn.edu.bit.cs.moecleaner.ui.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.edu.bit.cs.moecleaner.R;
import cn.edu.bit.cs.moecleaner.ui.customview.MemoryBoostListItem;
import cn.edu.bit.cs.moecleaner.util.PackageInfoUtil;
import cn.edu.bit.cs.moecleaner.util.ShellUtil;
import cn.edu.bit.cs.moecleaner.util.TextUtil;

/**
 * Created by entalent on 2016/4/14.
 */
public class MemoryBoostFragment extends BaseMoeFragment {

    Button scanBtn, cleanBtn;
    TextView memSizeText;
    View cleanBtnParent, viewProgress, cleanStatus;
    ListView listView;

    TextView textCurrent;

    ArrayList<ProcessMemoryInfo> processMemoryInfoList;

    long memToClean = 0;

    boolean[] checked;

    OnProcessListItemCheckedChangeListener onCheckedChangeListener = new OnProcessListItemCheckedChangeListener();

    static final int SYSTEM_APP_FLAG = ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_memoryboost, container, false);
        scanBtn = (Button) rootView.findViewById(R.id.button);
        cleanBtn = (Button) rootView.findViewById(R.id.button2);
        cleanBtnParent = rootView.findViewById(R.id.clean_btn_parent);
        memSizeText = (TextView) rootView.findViewById(R.id.cache_size_text);
        viewProgress = rootView.findViewById(R.id.view_progress);
        listView = (ListView) rootView.findViewById(R.id.listView);
        textCurrent = (TextView) rootView.findViewById(R.id.text_current);
        cleanStatus = rootView.findViewById(R.id.clean_status);

        viewProgress.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        cleanStatus.setVisibility(View.INVISIBLE);

        processMemoryInfoList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(ShellUtil.hasRootAccess());
            }
        }).start();



        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunningAppScanTask task = new RunningAppScanTask();
                task.execute();
            }
        });

        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunningAppCleanTask task = new RunningAppCleanTask();
                task.execute();
            }
        });


        return rootView;
    }

    @Override
    public void handleMessageFromHandler(Message msg) {

    }

    public class RunningAppScanTask extends AsyncTask<Void, Long, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewProgress.setVisibility(View.VISIBLE);
            scanBtn.setVisibility(View.GONE);
            cleanStatus.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            updateMemoryText();
        }

        @Override
        protected Long doInBackground(Void... params) {
            processMemoryInfoList.clear();
            PackageManager pm = getContext().getPackageManager();
            ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningAppProcessInfos = am.getRunningServices(100);
            for(ActivityManager.RunningServiceInfo info : runningAppProcessInfos) {
                try {
                    System.out.println(info.process + " " + info.pid);
                    ProcessMemoryInfo pmInfo = new ProcessMemoryInfo();
                    pmInfo.packageInfo = pm.getPackageInfo(info.process, PackageManager.GET_SERVICES);
                    if(info.process.equals(getContext().getPackageName())){
                        continue;
                    }
                    //是系统进程，忽略
                    if((pmInfo.packageInfo.applicationInfo.flags & SYSTEM_APP_FLAG) != 0) {
                        continue;
                    }
                    pmInfo.pid = info.pid;
                    Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[]{info.pid})[0];
                    pmInfo.totalMemory = memoryInfo.getTotalPrivateDirty() * 1024;
                    processMemoryInfoList.add(pmInfo);
                    memToClean += pmInfo.totalMemory;
                    publishProgress();
                } catch (Exception e) {
                    continue;
                }

            }
            return memToClean;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            cleanBtn.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            viewProgress.setVisibility(View.GONE);
            checked = new boolean[processMemoryInfoList.size()];
            for(int i = 0; i < checked.length; i++){
                checked[i] = true;
            }
            listView.setAdapter(new MemoryCleanListAdapter());
            updateMemoryText();
        }
    }

    public class RunningAppCleanTask extends AsyncTask<Void, Long, Long>{
        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
            updateMemoryText();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cleanBtn.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            memToClean = 0;
            updateMemoryText();
            listView.setVisibility(View.INVISIBLE);
            textCurrent.setVisibility(View.VISIBLE);
            textCurrent.setText(getText(R.string.text_clean_mem_finish));
        }

        @Override
        protected Long doInBackground(Void... params) {
            ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
            for(int i = 0; i < checked.length; i++){
                if(!checked[i]) continue;

                try {
                    //第一种方法 android.os.killProcess
                    Process.killProcess(processMemoryInfoList.get(i).pid);
                    //第二种方法 ActivityManager的killBackgroundProcesses
                    am.killBackgroundProcesses(processMemoryInfoList.get(i).packageInfo.packageName);
                    //第三种方法 反射调用forceStopPackage
                    Method forceStopPackage = am.getClass()
                            .getDeclaredMethod("forceStopPackage", String.class);
                    forceStopPackage.setAccessible(true);
                    forceStopPackage.invoke(am, processMemoryInfoList.get(i).packageInfo.packageName);
                } catch (Exception e) {

                }

                try {
                    //第四种方法 获取root权限之后执行kill命令
                    java.lang.Process process = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(process.getOutputStream());
                    os.writeBytes("kill " + processMemoryInfoList.get(i).pid + "\n");
                    os.writeBytes("exit\n");
                    os.flush();
                    process.waitFor();
                    os.close();
                } catch (Exception e) {

                }

                memToClean -= processMemoryInfoList.get(i).totalMemory;
                publishProgress();
            }
            return null;
        }
    }

    public class OnProcessListItemCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            if(position < 0 || position > checked.length)
                return;
            checked[position] = isChecked;
            if(isChecked) {
                memToClean += processMemoryInfoList.get(position).totalMemory;
            } else {
                memToClean -= processMemoryInfoList.get(position).totalMemory;
            }
            updateMemoryText();
        }
    }

    void updateMemoryText() {
        memSizeText.setText(TextUtil.formatStorageSizeStr(memToClean));
    }

    public class MemoryCleanListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return processMemoryInfoList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if(position >= processMemoryInfoList.size()) {
                View view = new MemoryBoostListItem(getContext(), null);
                view.setVisibility(View.INVISIBLE);
                return view;
            } else {
                View convertView = new MemoryBoostListItem(getContext(), null);
                ((MemoryBoostListItem)convertView).setProcessMemoryInfo(processMemoryInfoList.get(position));
                if(checked != null)
                ((MemoryBoostListItem)convertView).getCheckBox().setChecked(checked[position]);
                ((MemoryBoostListItem)convertView).getCheckBox().setTag(position);
                ((MemoryBoostListItem)convertView).setOnCheckedChangeListener(onCheckedChangeListener);

                return convertView;
            }
        }
    }

    public class ProcessMemoryInfo {
        public PackageInfo packageInfo;
        public int pid;
        public long totalMemory;
    }
}
