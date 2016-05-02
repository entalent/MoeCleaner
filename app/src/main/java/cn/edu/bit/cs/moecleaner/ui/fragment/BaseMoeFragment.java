package cn.edu.bit.cs.moecleaner.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;


/**
 * Created by entalent on 2016/4/15.
 */
public abstract class BaseMoeFragment extends Fragment {

    static final int MSG_REFRESH_CPU_PROGRESSBAR = 0x0,
                    MSG_JUMPED_FROM_HOME_PAGE = 0x1;

    public static ViewPagerManager viewPagerManager;

    FragmentHandler mFragmentHandler = new FragmentHandler(this);

    public void setViewPagerManager(ViewPagerManager viewPagerManager) {
        this.viewPagerManager = viewPagerManager;
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
            final BaseMoeFragment fragment = mFragmentReference.get();
            if(fragment != null) {
                fragment.handleMessageFromHandler(msg);
            }
        }
    }

    public abstract void handleMessageFromHandler(Message msg);
}
