package cn.edu.bit.cs.moecleaner.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import cn.edu.bit.cs.moecleaner.MainActivity;

/**
 * Created by entalent on 2016/4/15.
 */
public class BaseMoeFragment extends Fragment {
    MainActivity.ViewPagerManager viewPagerManager;
    FragmentHandler mFragmentHandler = new FragmentHandler(this);

    public void setViewPagerManager(MainActivity.ViewPagerManager viewPagerManager) {
        this.viewPagerManager = viewPagerManager;
        System.out.println(this.viewPagerManager == null);
    }

    public FragmentHandler getFragmentHandler() {
        return mFragmentHandler;
    }

    public static class FragmentHandler extends Handler {
        WeakReference<BaseMoeFragment> mFragmentReference;

        FragmentHandler(BaseMoeFragment fragment) {
            this.mFragmentReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final Fragment fragment = mFragmentReference.get();
            if(fragment != null) {
                //...
            }
        }
    }
}
