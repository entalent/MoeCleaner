package cn.edu.bit.cs.moecleaner;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import cn.edu.bit.cs.moecleaner.systemmonitor.BasicInfo;
import cn.edu.bit.cs.moecleaner.ui.fragment.BaseMoeFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.HomeFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.JunkCleanFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.MemoryBoostFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.SystemInfoFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.ViewPagerManager;
import cn.edu.bit.cs.moecleaner.util.HttpRequest;

/**
 * Created by entalent on 2016/4/14.
 * 主界面，里面有4个Fragment
 */
public class MainActivity extends AppCompatActivity implements ViewPagerManager {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    long previousBackPressTime = 0;

    static final int REQUEST_CODE_CHECK_PHONE_STATE = 0x1,
                    REQUEST_CODE_STORAGE = 0x2;

    final String VERSION = "1.0";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //........................
        if (permissions.length == 0 || grantResults.length == 0) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CHECK_PHONE_STATE:
                break;
            case REQUEST_CODE_STORAGE:
                //读写外部存储权限被拒绝
                if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean getversion = prefs.getBoolean(VERSION, true);
        //get the version code
        if (getversion){
            //if got, it's first time opening, write a new version code.
            SharedPreferences.Editor pEdit = prefs.edit();
            pEdit.putBoolean(VERSION, false);
            pEdit.commit();
            startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
        }


        //startActivity(new Intent(MainActivity.this, TestActivity.class));
        if(Build.VERSION.SDK_INT >= 23) {
            if(PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_CHECK_PHONE_STATE);
            }

            if(PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
            }

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.invalidate();

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        if(mViewPager.getCurrentItem() == 0) {
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    tabLayout.setVisibility(View.GONE);
                } else {
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String info = BasicInfo.getAllSystemInfo(MainActivity.this);
                    String res = HttpRequest.sendPost("http://10.2.70.115/collect_info.php", "info=" + info);
                    System.out.println(res);
                } catch (Exception e) {

                }
            }
        }).start();
    }


    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0);
        }
        long currentBackPressTime = System.currentTimeMillis();
        if(currentBackPressTime - previousBackPressTime < 2000) {
            super.onBackPressed();
        } else {
            previousBackPressTime = currentBackPressTime;
            Toast.makeText(MainActivity.this, getString(R.string.toast_press_back_once_again_to_exit), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCurrentItem(int index) {
        mViewPager.setCurrentItem(index);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public BaseMoeFragment[] fragments = new BaseMoeFragment[4];

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments[0] = new HomeFragment();
            fragments[1] = new JunkCleanFragment();
            fragments[2] = new MemoryBoostFragment();
            fragments[3] = new SystemInfoFragment();
            for(int i = 0; i < 4; i++){
                fragments[i].setViewPagerManager(MainActivity.this);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_home);
                case 1:
                    return getString(R.string.title_junk_clean);
                case 2:
                    return getString(R.string.title_memory_boost);
                case 3:
                    return getString(R.string.title_system_info);
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        mSectionsPagerAdapter = null;
        super.onDestroy();
    }
}
