package cn.edu.bit.cs.moecleaner.ui.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.bit.cs.moecleaner.R;
import cn.edu.bit.cs.moecleaner.cleaner.CacheInfoProvider;
import cn.edu.bit.cs.moecleaner.ui.customview.JunkCleanListItem;
import cn.edu.bit.cs.moecleaner.util.FileUtil;
import cn.edu.bit.cs.moecleaner.util.TextUtil;

/**
 * Created by entalent on 2016/4/14.
 */
public class JunkCleanFragment extends BaseMoeFragment {

    ToggleButton btn;

    CacheInfoProvider cacheInfoProvider;

    TextView totalCacheSizeText, currentDetailText;
    View startCleanBtnRoot;
    View cleanStatusView;
    Button scanBtn, cleanBtn;

    ListView packageCacheListView;

    long scannedCacheSize;

    ExecutorService executorService = Executors.newFixedThreadPool(5);

    Integer cleanThreadCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_junkclean, container, false);
        totalCacheSizeText = (TextView) rootView.findViewById(R.id.cache_size_text);
        cacheInfoProvider = new CacheInfoProvider(getContext());
        startCleanBtnRoot = rootView.findViewById(R.id.clean_btn_parent);
        cleanStatusView = rootView.findViewById(R.id.clean_status);
        packageCacheListView = (ListView) rootView.findViewById(R.id.package_cache_list);
        scanBtn = (Button) rootView.findViewById(R.id.button);
        cleanBtn = (Button) rootView.findViewById(R.id.button2);
        currentDetailText = (TextView) rootView.findViewById(R.id.text_current);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheScanTask task = new CacheScanTask(getContext(), cacheInfoProvider);
                startCleanBtnRoot.setVisibility(View.INVISIBLE);
                cleanStatusView.setVisibility(View.VISIBLE);
                currentDetailText.setVisibility(View.VISIBLE);
                task.execute();
            }
        });
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanThreadCount = 0;
                ArrayList<CacheInfoProvider.PackageCacheItem> list = cacheInfoProvider.getCacheInfoList();
                for(CacheInfoProvider.PackageCacheItem item : list) {
                    for(int i = 0; i < item.cacheFiles.size(); i++){
                        File file = item.cacheFiles.get(i);
                        Long fileSize = item.cacheFilesSize.get(i);
                        CacheCleanTask task = new CacheCleanTask();
                        synchronized (cleanThreadCount) {
                            task.executeOnExecutor(executorService, file, fileSize);
                            cleanThreadCount++;
                        }
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void handleMessageFromHandler(Message msg) {

    }

    class PackageCacheListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return cacheInfoProvider.getCacheInfoList().size() + 1;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if(position >= cacheInfoProvider.getCacheInfoList().size()) {
                if (convertView == null) {
                    convertView = new JunkCleanListItem(getActivity(), null);
                }
                //((JunkCleanListItem) convertView).setPackageCacheItem(cacheInfoProvider.getCacheInfoList().get(position));
                convertView.setVisibility(View.INVISIBLE);
                return convertView;
            } else {
                if (convertView == null) {
                    convertView = new JunkCleanListItem(getActivity(), null);
                }
                ((JunkCleanListItem) convertView).setPackageCacheItem(cacheInfoProvider.getCacheInfoList().get(position));
                convertView.setVisibility(View.VISIBLE);
                return convertView;
            }
        }
    }

    public class CacheScanTask extends AsyncTask<CacheInfoProvider, Object, Long> {
        CacheInfoProvider cacheInfoProvider;
        Context context;
        List<PackageInfo> packageInfos;

        public CacheScanTask(Context context, CacheInfoProvider provider) {
            this.context = context;
            this.cacheInfoProvider = provider;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            packageInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        }

        @Override
        protected Long doInBackground(CacheInfoProvider... params) {
            if(packageInfos == null) {
                return 0l;
            }
            for(PackageInfo packageInfo : packageInfos) {
                this.cacheInfoProvider.scanPackageCacheFile(packageInfo);
                publishProgress(packageInfo.packageName, cacheInfoProvider.getTotalCacheSize());
            }
            return cacheInfoProvider.getTotalCacheSize();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            packageCacheListView.setAdapter(new PackageCacheListAdapter());
            startCleanBtnRoot.setVisibility(View.INVISIBLE);
            cleanBtn.setVisibility(View.VISIBLE);
            currentDetailText.setVisibility(View.GONE);
            scannedCacheSize = cacheInfoProvider.getTotalCacheSize();
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            String packageName = (String) values[0];
            Long cacheSize = (Long) values[1];
            totalCacheSizeText.setText(TextUtil.formatStorageSizeStr(cacheSize));
            currentDetailText.setText("scanning: " + packageName);
        }

        @Override
        protected void onCancelled(Long aLong) {
            super.onCancelled(aLong);
        }
    }

    public class CacheCleanTask extends AsyncTask<Object, Object, Long> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            currentDetailText.setVisibility(View.VISIBLE);
            cleanBtn.setVisibility(View.GONE);
        }

        @Override
        protected Long doInBackground(Object... params) {
            File fileToDelete = (File) params[0];
            Long fileSize = (Long) params[1];
            FileUtil.deleteFileOrDirectory(fileToDelete);
            publishProgress(fileToDelete.getAbsolutePath(), fileSize);
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            String fileName = (String) values[0];
            Long cacheSize = (Long) values[1];
            currentDetailText.setText(fileName);
            scannedCacheSize -= cacheSize;
            totalCacheSizeText.setText(TextUtil.formatStorageSizeStr(scannedCacheSize));
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            synchronized (cleanThreadCount) {
                cleanThreadCount--;
            }
            if(cleanThreadCount == 0) {
                currentDetailText.setText("clean finished");
                cleanBtn.setVisibility(View.GONE);
                packageCacheListView.setVisibility(View.GONE);
            }
        }
    }
}


