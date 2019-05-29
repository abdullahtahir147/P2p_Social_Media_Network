package com.example.dropboxtest.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.example.dropboxtest.Activities.MessageActivity;
import com.example.dropboxtest.Objects.MessageSample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AsyncUpdateMessages extends AsyncTask<Void,Void,Void> {
    DbxClientV2 client;
    String path;
    ArrayList<MessageSample> messageArray;
    public AsyncUpdateMessages(DbxClientV2 client,String path,ArrayList<MessageSample> messageArray){
        this.client=client;
        this.path=path;
        this.messageArray=messageArray;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        InputStream in;
        StringBuilder result=null;
        Log.v("updateMessages",path+"=path");

        try {
            in=client.files().downloadBuilder(path).start().getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            result = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                result.append(line).append('\n');
            }

        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject=null;
        try {
            jsonObject=new JSONObject(result.toString());
            JSONArray jsonArray=jsonObject.getJSONArray("messages");
            messageArray.clear();
            if(jsonArray.length()!=0){
                for(int i=0;i<jsonArray.length();i++){
                    MessageSample messageSample=new MessageSample(jsonArray.getJSONObject(i).getString("message"),jsonArray.getJSONObject(i).getString("time"),jsonArray.getJSONObject(i).getString("sender"));
                    messageArray.add(messageSample);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MessageActivity.notifyAdapter();
    }
}
