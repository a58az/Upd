package com.example.admin.upd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MyActivity extends Activity {
    final String lastModKey = "com.example.admin.upd.lastMod";
    final SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

    Button updButton, downButton;
    TextView lastChange, fileName;
    SharedPreferences pref;
    private String Http = "http://www.econom.zp.ua:81/images/stories/raspisanie/3ki.xls";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        lastChange = (TextView) findViewById(R.id.textView2);
        fileName = (TextView) findViewById(R.id.textView);

        updButton = (Button) findViewById(R.id.updateButton);
        downButton = (Button) findViewById(R.id.downloadButton);


        pref = this.getSharedPreferences("com.example.admin.upd", MODE_PRIVATE);
        long l = pref.getLong(lastModKey, 0);

        fileName.setText(getText(R.string.fileNameView) + " 3ki.xls");
        lastChange.setText(String.format("%s %s", getText(R.string.fileChangeView),  dt.format(new Date(l))));

        updButton.setOnClickListener(getLastModify());
        downButton.setOnClickListener(DownLoad());
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
                Date t = null;
                AsyncTask<String, Integer, String> task = new Network().execute(Http);
                try {
                    t = new Date(task.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                // save last
                pref.edit().putLong(lastModKey, t != null ? t.getTime() : 0).apply();
                lastChange.setText(String.format("%s %s", getText(R.string.fileChangeView), dt.format(t) ));
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
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
