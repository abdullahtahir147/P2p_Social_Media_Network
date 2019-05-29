package com.example.dropboxtest;

import android.content.Context;

import com.dropbox.core.android.Auth;

public class CloudDropbox {
    Context context;
    public CloudDropbox(Context context){
        this.context=context;
    }
    public void DropboxAuthentication(){
        Auth.startOAuth2Authentication(context, "b8foyia1h9lcoy7");
    }
}
