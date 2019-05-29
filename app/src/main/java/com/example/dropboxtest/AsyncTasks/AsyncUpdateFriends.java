package com.example.dropboxtest.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.example.dropboxtest.Constants;
import com.example.dropboxtest.Objects.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AsyncUpdateFriends extends AsyncTask<Void,Void,Void> {
    private DbxClientV2 client;
    private Context context;
    private ProgressDialog progressDialog;


    public AsyncUpdateFriends(DbxClientV2 client,Context context){
        this.client=client;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Fetching Friends");
        progressDialog.show();

    }


    @Override
    protected Void doInBackground(Void... voids) {
        StringBuilder builderResult=null;
        try {
            InputStream in;
            in=client.files().downloadBuilder(Constants.Personal_Friends_Folder_path).start().getInputStream();
            Constants.User=client.users().getCurrentAccount().getName().getDisplayName();
            Log.v("getname",Constants.User);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            builderResult = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                builderResult.append(line).append('\n');
            }

        } catch (DownloadErrorException e){
            Log.v("DownloadPhase",e.errorValue.toString());

        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Friend> mFriendList=new ArrayList<>();
        try{
            JSONObject jsonObject= new JSONObject(builderResult.toString());
            JSONArray friendList=jsonObject.getJSONArray("friends");
            JSONObject friendElement;
            Friend friend;
            for(int i=0;i<friendList.length();i++){
                friendElement=friendList.getJSONObject(i);
                friend=new Friend();
                friend.seteMail(friendElement.getString("E-Mail"));
                friend.setFolderId(friendElement.getString("Folder-Id"));
                friend.setFolderPath(friendElement.getString("Folder-Path"));
                friend.setName(friendElement.getString("Name"));
                friend.setAccountId(friendElement.getString("Account-Id"));
                //friend.setPpPath(friendElement.get("Pp-Path").toString());
                mFriendList.add(friend);

            }
            if(!mFriendList.isEmpty()){
                Constants.arrayFriends=mFriendList;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
    }
}
