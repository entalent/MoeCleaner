package cn.edu.bit.cs.moecleaner.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import cn.edu.bit.cs.moecleaner.R;

/**
 * Created by entalent on 2016/4/14.
 */
public class JunkCleanFragment extends BaseMoeFragment {

    ToggleButton btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_junkclean, container, false);
        btn = (ToggleButton) rootView.findViewById(R.id.toggleButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), (getActivity() == null) + "", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void handleMessageFromHandler(Message msg) {
        if(msg.what == MSG_JUMPED_FROM_HOME_PAGE) {
            System.out.println("msg recv" + (getActivity() == null));

        }
    }
}
