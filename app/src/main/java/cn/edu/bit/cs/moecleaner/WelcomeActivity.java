package cn.edu.bit.cs.moecleaner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.w.song.widget.scroll.SlidePageView;

/**
 * Created by Jonathan.P on 16/5/2.
 */

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final TextView Welcome_Btn = (TextView) findViewById(R.id.welcome_button);

        Welcome_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
    }

}
