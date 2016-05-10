package cn.edu.bit.cs.moecleaner.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

import cn.edu.bit.cs.moecleaner.R;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("receive: " + intent.getAction());
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.night_mode);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.peppermint_candy)
                .setLargeIcon(bitmap)
                .setTicker("")
                .setContentTitle("不早啦~")
                .setContentText(context.getString(R.string.app_name) + "提示: 别玩手机了，睡觉吧~")
                .setNumber(1)
                .build();
        notification.flags |= (Notification.FLAG_AUTO_CANCEL);
        notificationManager.notify(1, notification);
    }
}
