package cn.edu.bit.cs.moecleaner;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import junit.framework.Test;

import cn.edu.bit.cs.moecleaner.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    String str = "0";
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        btn = (Button) findViewById(R.id.button);
        binding.setCount(str);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "成功续1秒", Toast.LENGTH_SHORT).show();
                int value = Integer.parseInt(str);
                value++;
                str = Integer.toString(value);
                //要手动更新算哪门子data binding。。。
                binding.setCount(str);
            }
        });
    }
}
