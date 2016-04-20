package cn.edu.bit.cs.moecleaner;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import junit.framework.Test;

import cn.edu.bit.cs.moecleaner.databinding.ActivityTestBinding;

/**
 * Created by entalent on 2016/4/14.
 * 测试用Activity
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_test);

    }
}
