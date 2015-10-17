package com.example.admin.upd;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.admin.upd.Constants.Http;
import static com.example.admin.upd.Constants.dt;
import static com.example.admin.upd.Constants.lastModIntentKey;
import static com.example.admin.upd.Constants.lastModPrefKey;


public class MyActivity extends Activity {
    //    private final long updateInterval = TimeUnit.MINUTES.toMillis(30);
    final long updateInterval = TimeUnit.SECONDS.toMillis(10);
    Button updButton, downButton;
    TextView lastChange, fileName;
    SharedPreferences pref;
    AlarmManager am;
    Date lastMod;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        lastChange = (TextView) findViewById(R.id.textView2);
        fileName = (TextView) findViewById(R.id.textView);

        updButton = (Button) findViewById(R.id.updateButton);
        downButton = (Button) findViewById(R.id.downloadButton);


        pref = this.getSharedPreferences("com.example.admin.upd", MODE_PRIVATE);
        long l = pref.getLong(lastModPrefKey, 0);
        lastMod = new Date(l);

        fileName.setText(getText(R.string.fileNameView) + " 4ki.xls");
        lastChange.setText(String.format("%s %s", getText(R.string.fileChangeView), dt.format(new Date(l))));

        updButton.setOnClickListener(getLastModify());
        downButton.setOnClickListener(DownLoad());

        UpdateLastDate();
        autoUpdater();
    }

    private View.OnClickListener DownLoad() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Http));
                startActivity(browserIntent);
            }
        };
    }

    private View.OnClickListener getLastModify() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateLastDate();
            }
        };
    }

    private void UpdateLastDate() {
        NetworkResult nwRes = new NetworkResult();
        Date t = nwRes.getNetworkResult();

        // save last
        if (t.after(lastMod)) {
            pref.edit().putLong(lastModPrefKey, t.getTime()).apply();
            lastChange.setText(String.format("%s %s", getText(R.string.fileChangeView), dt.format(t)));
            lastMod = t;
        }
    }

    private void autoUpdater() {
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeNotification.class);
        intent.putExtra(lastModIntentKey, lastMod.getTime());
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, updateInterval, pendingIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.my, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
