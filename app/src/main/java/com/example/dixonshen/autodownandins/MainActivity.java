package com.example.dixonshen.autodownandins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btn1;
    private EditText et1;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.download1);
        et1 = (EditText) findViewById(R.id.url1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDowbload();
            }
        });

    }

    public void startDowbload(){
        Intent intent = new Intent(this, DownloadService.class);
        String mUrl = et1.getText().toString();
        intent.putExtra("url", mUrl);
        startService(intent);
        // 设置广播接收器，当新版本的apk下载完成后自动弹出安装界面
        IntentFilter intentFilter = new IntentFilter("downloadComplete");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                String path = intent.getStringExtra("downloadFile");
                install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy(){
        // 移除广播接收器
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

}
