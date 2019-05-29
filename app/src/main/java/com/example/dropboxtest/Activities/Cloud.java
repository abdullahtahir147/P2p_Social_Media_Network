package com.example.dropboxtest.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dropbox.core.android.Auth;
import com.example.dropboxtest.AsyncTasks.AsyncCreateDirectoryOld;
import com.example.dropboxtest.CloudDropbox;
import com.example.dropboxtest.Constants;
import com.example.dropboxtest.R;

public class Cloud extends AppCompatActivity {
    public Context context =this;
    private boolean DropboxSelected=false;

    public Context getContext(){
        return context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout dropbox=findViewById(R.id.dropbox_button);
        dropbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropboxSelected=true;
                CloudDropbox cloudDropbox=new CloudDropbox(context);
                cloudDropbox.DropboxAuthentication();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token;
        if(DropboxSelected){
            //Get token from dropBox.
            token= Auth.getOAuth2Token();

            if(token!=null){
                //Saving token
                SharedPreferences sharedPreferences=this.getSharedPreferences("DropBox",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("DropBox_token",token);
                editor.apply();
                AsyncCreateDirectoryOld asyncCreateDirectoryOld =new AsyncCreateDirectoryOld(Constants.Main_File_Path,false,this);
                asyncCreateDirectoryOld.execute();

                if(sharedPreferences.getBoolean("DropBox_Directory_Created",false)){

                }

                Log.v("DToken",token);
            }
        }


    }
}
