package cn.edu.bit.cs.moecleaner;

import android.content.Intent;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import cn.edu.bit.cs.moecleaner.fragment.BaseMoeFragment;
import cn.edu.bit.cs.moecleaner.fragment.HomeFragment;
import cn.edu.bit.cs.moecleaner.fragment.JunkCleanFragment;
import cn.edu.bit.cs.moecleaner.fragment.MemoryBoostFragment;
import cn.edu.bit.cs.moecleaner.fragment.SystemInfoFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(MainActivity.this, TestActivity.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter.setViewPager(mViewPager);
        mSectionsPagerAdapter.setCurrentItem(0);
        //缓存！！
        mViewPager.setOffscreenPageLimit(4);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    long previousBackPressTime = 0;
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
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPagerManager {
        ViewPager viewPager;

        BaseMoeFragment[] fragments = new BaseMoeFragment[4];

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fragments[0] = new HomeFragment();
                    fragments[0].setViewPagerManager(this);
                    break;
                case 1:
                    fragments[1] = new JunkCleanFragment();
                    fragments[1].setViewPagerManager(this);
                    break;
                case 2:
                    fragments[2] = new MemoryBoostFragment();
                    fragments[2].setViewPagerManager(this);
                    break;
                case 3:
                    fragments[3] = new SystemInfoFragment();
                    fragments[3].setViewPagerManager(this);
                    break;
                default:
                    return null;
            }
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

        @Override
        public void setViewPager(ViewPager vp) {
            this.viewPager = vp;
            vp.setAdapter(this);
        }

        @Override
        public void setCurrentItem(int index) {
            viewPager.setCurrentItem(index);
        }

        @Override
        public void sendMessageToFragment(int index, Message msg) {
            fragments[index].getFragmentHandler().sendMessage(msg);
        }
    }

    public interface ViewPagerManager {
        void setViewPager(ViewPager vp);
        void setCurrentItem(int index);
        void sendMessageToFragment(int index, Message msg);
    }
}
