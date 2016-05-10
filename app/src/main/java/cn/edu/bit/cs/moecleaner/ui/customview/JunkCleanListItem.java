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
import cn.edu.bit.cs.moecleaner.cleaner.CacheInfoProvider;
import cn.edu.bit.cs.moecleaner.util.TextUtil;

/**
 * Created by entalent on 2016/5/2.
 */
public class JunkCleanListItem extends RelativeLayout {

    PackageManager pm;
    CacheInfoProvider.PackageCacheItem packageCacheItem;
    TextView appNameText, cacheSizeText;
    ImageView appIconImage;

    public JunkCleanListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_junkcleanlistitem, this);
        appNameText = (TextView) findViewById(R.id.text_app_name);
        cacheSizeText = (TextView) findViewById(R.id.text_cache_size);
        appIconImage = (ImageView) findViewById(R.id.imageView);
        pm = getContext().getPackageManager();
    }

    public void setPackageCacheItem(CacheInfoProvider.PackageCacheItem item) {
        this.packageCacheItem = item;
        appNameText.setText(pm.getApplicationLabel(item.packageInfo.applicationInfo));
        cacheSizeText.setText(TextUtil.formatStorageSizeStr(packageCacheItem.cacheSize));
        appIconImage.setImageDrawable(pm.getApplicationIcon(item.packageInfo.applicationInfo));
    }

    public CacheInfoProvider.PackageCacheItem getPackageCacheItem() {
        return this.packageCacheItem;
    }

}
