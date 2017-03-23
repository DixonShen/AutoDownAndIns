package com.example.dixonshen.autodownandins;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;

import java.io.File;

/**
 * Created by Z003R98D on 3/23/2017.
 */

public class DownloadService extends Service {

    private final String TAG = DownloadService.class.getSimpleName();

    private NotifyUtil notify;
    private int requestCode = (int) SystemClock.uptimeMillis();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        String downloadUrl = bundle.getString("url");
        File dir = new File(Environment.getExternalStorageDirectory() + "/Download");
        if (!dir.exists())
            dir.mkdir();
        File file = new File(dir, "test.apk");
        Intent intent_noti = new Intent();
        intent_noti.setAction(Intent.ACTION_VIEW);
        intent_noti.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, requestCode, intent_noti, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.ic_launcher;
        String ticker = "正在下载TEST应用";   // 失效，不显示！
        NotifyUtil notify7 = new NotifyUtil(this, 7);
        notify7.notify_progress(rightPendIntent, smallIcon, ticker, "下载TEST应用", "正在下载",
                false, false, false, downloadUrl, file.getAbsolutePath());
        notify = notify7;

        return super.onStartCommand(intent, flags, startId);
    }

}
