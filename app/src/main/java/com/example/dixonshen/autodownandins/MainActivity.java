package com.example.dixonshen.autodownandins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btn1;
    private EditText et1;
    private Context mContext;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        btn1 = (Button) findViewById(R.id.download1);
        et1 = (EditText) findViewById(R.id.url1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(mContext)) {
                    startDowbload();
                } else {
                    Toast.makeText(mContext, "网络连接不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 检测当前的网络（WLAN, 4G/2G）状态
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                } else {

                }
            }
        }
        return false;
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

    private void setResultToToast(final String string){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
