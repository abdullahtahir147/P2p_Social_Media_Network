package com.example.dropboxtest.AsyncTasks;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.ListFoldersResult;
import com.example.dropboxtest.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AsyncSyncGroups extends AsyncTask<Void,Void,Void> {
    private DbxClientV2 client;
    public AsyncSyncGroups(DbxClientV2 client){
        this.client=client;

    }
    @Override
    protected Void doInBackground(Void... voids) {
        StringBuilder builderResult;

        try {
            InputStream in;
            in=client.files().downloadBuilder(Constants.Personal_Groups_Folder_path).start().getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            builderResult = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                builderResult.append(line).append('\n');
            }

            JSONObject jsonObject=new JSONObject(builderResult.toString());
            JSONArray jsonArray=jsonObject.getJSONArray("groups");
            ListFoldersResult listFoldersResult=client.sharing().listFolders();
            for(int i=0;i<listFoldersResult.getEntries().size();i++){
                if(listFoldersResult.getEntries().get(i).getName().contains("@P2PSN")){
                    String folderName=listFoldersResult.getEntries().get(i).getName();
                    String groupName=folderName.substring(0,folderName.length()-6);
                    String groupPath=Constants.Group_Folder_path+"/"+folderName;
                    String folderId=listFoldersResult.getEntries().get(i).getSharedFolderId();
                    JSONObject newGroup=new JSONObject();
                    newGroup.put("groupName",groupName);
                    newGroup.put("groupPath",groupPath);
                    newGroup.put("folderId",folderId);
                    jsonArray.put(newGroup);
                    client.files().moveV2("/"+folderName,Constants.Group_Folder_path+"/"+folderName);

                }
            }
            InputStream byteArrayInputStream=new ByteArrayInputStream(jsonObject.toString().getBytes());
            client.files().uploadBuilder(Constants.Personal_Groups_Folder_path).withMode(WriteMode.OVERWRITE).uploadAndFinish(byteArrayInputStream);
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
