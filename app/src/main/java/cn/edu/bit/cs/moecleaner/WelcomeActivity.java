package cn.edu.bit.cs.moecleaner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.AppThemeBlue_NoActionBar, true);

        Bundle bundle = this.getIntent().getExtras();
        boolean isWelcome = bundle.getBoolean("welcome", false);

        if(isWelcome) {
            //welcome screen
            setContentView(R.layout.activity_welcome);
            final TextView Welcome_Btn = (TextView) findViewById(R.id.welcome_button);
            Welcome_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            //splash screen
            setContentView(R.layout.activity_splash);
            View rootView = findViewById(R.id.root_view);
            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        }
    }

}
