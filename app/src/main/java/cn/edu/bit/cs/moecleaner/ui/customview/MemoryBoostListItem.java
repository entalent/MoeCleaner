package cn.edu.bit.cs.moecleaner.ui.customview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.edu.bit.cs.moecleaner.R;
import cn.edu.bit.cs.moecleaner.ui.fragment.MemoryBoostFragment;
import cn.edu.bit.cs.moecleaner.util.TextUtil;

/**
 * Created by entalent on 2016/5/2.
 */
public class MemoryBoostListItem extends RelativeLayout {
    PackageManager pm;
    TextView appName, pidText, memText;
    ImageView appIcon;

    CheckBox checkBox;

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public MemoryBoostListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_memoryboostlistitem, this);
        pm = getContext().getPackageManager();
        appName = (TextView) findViewById(R.id.text_app_name);
        pidText = (TextView) findViewById(R.id.text_pid);
        memText = (TextView) findViewById(R.id.text_cache_size);
        appIcon = (ImageView) findViewById(R.id.imageView);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

    }

    public void setProcessMemoryInfo(MemoryBoostFragment.ProcessMemoryInfo info) {
        appName.setText(pm.getApplicationLabel(info.packageInfo.applicationInfo));
        pidText.setText("pid = " + info.pid);
        memText.setText(TextUtil.formatStorageSizeStr(info.totalMemory));
        appIcon.setImageDrawable(pm.getApplicationIcon(info.packageInfo.applicationInfo));
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener l) {
        checkBox.setOnCheckedChangeListener(l);
    }


}
