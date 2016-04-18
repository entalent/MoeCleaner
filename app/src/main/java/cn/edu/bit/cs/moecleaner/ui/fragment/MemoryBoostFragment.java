package cn.edu.bit.cs.moecleaner.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.bit.cs.moecleaner.R;

/**
 * Created by entalent on 2016/4/14.
 */
public class MemoryBoostFragment extends BaseMoeFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_memoryboost, container, false);
        return rootView;
    }

    @Override
    public void handleMessageFromHandler(Message msg) {

    }
}
