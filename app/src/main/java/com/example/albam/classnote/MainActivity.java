package com.example.albam.classnote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.albam.classnote.util.CustomAdapter;
import com.example.albam.classnote.util.FTPUtils;

import org.apache.commons.net.ftp.FTP;

import java.lang.reflect.Array;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String dir = "";

    private String fileDownload = "";

    private static int PERMISSION_WRITE_TO_EXTERNAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = (ListView) findViewById(R.id.listView);
        reloadList();
        if (list != null) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick (AdapterView <?> adapter, View v,int position, long id){
                    if (FTPUtils.isFile(dir, adapter.getItemAtPosition(position) + "")) {
                        fileDownload = adapter.getItemAtPosition(position) + "";
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_TO_EXTERNAL);
                        } else {
                            startDownload();
                        }
                    } else {
                        dir += "/" + adapter.getItemAtPosition(position);
                        reloadList();
                    }

                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, CreatePostActivity.class));

                }
            });
        }

    }

    private void startDownload() {
        FTPUtils.downloadFile(dir, fileDownload + "", MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_WRITE_TO_EXTERNAL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            }
        }
    }

    @Override
    public void onBackPressed() {
        int lastIndex = dir.lastIndexOf("/");
        if (lastIndex != -1){
            dir = dir.substring(0, lastIndex);
            reloadList();
        } else {
            this.finish();
        }
    }

    private void reloadList() {
        List<String> notes = FTPUtils.getOrError(FTPUtils.getFiles(dir), MainActivity.this);
        CustomAdapter itemsAdapter = new CustomAdapter(MainActivity.this, android.R.layout.simple_list_item_1,FTPUtils.asArray(notes));
        ListView list = (ListView) findViewById(R.id.listView);
        if (list != null) list.setAdapter(itemsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

