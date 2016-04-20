package cn.edu.bit.cs.moecleaner;

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

import cn.edu.bit.cs.moecleaner.ui.fragment.BaseMoeFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.HomeFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.JunkCleanFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.MemoryBoostFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.SystemInfoFragment;
import cn.edu.bit.cs.moecleaner.ui.fragment.ViewPagerManager;

/**
 * Created by entalent on 2016/4/14.
 * 主界面，里面有4个Fragment
 */
public class MainActivity extends AppCompatActivity implements ViewPagerManager {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    long previousBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
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
                System.out.println(fragments[i].viewPagerManager == null);
            }
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("getItem " + position + " " + fragments[position].toString());
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
