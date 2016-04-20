package cn.edu.bit.cs.moecleaner.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.edu.bit.cs.moecleaner.R;
import cn.edu.bit.cs.moecleaner.systemmonitor.customview.CustomGLSurfaceView;

/**
 * Created by entalent on 2016/4/14.
 * 主界面第4个Fragment
 * 用于显示系统基本信息
 */
public class SystemInfoFragment extends BaseMoeFragment {

    CustomGLSurfaceView glSurfaceView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_systeminfo, container, false);
        glSurfaceView = new CustomGLSurfaceView(getActivity());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(1, 1);
        ((ViewGroup) rootView).addView(glSurfaceView, lp);
        return rootView;
    }

    @Override
    public void handleMessageFromHandler(Message msg) {

    }
}
