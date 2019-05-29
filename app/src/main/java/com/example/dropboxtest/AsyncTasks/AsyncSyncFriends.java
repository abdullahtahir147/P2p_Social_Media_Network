package com.example.dropboxtest.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.ListFoldersResult;
import com.dropbox.core.v2.sharing.SharedFolderMembers;
import com.example.dropboxtest.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AsyncSyncFriends extends AsyncTask<Void,Void,Void> {
    DbxClientV2 client;

    public AsyncSyncFriends(DbxClientV2 client){
        this.client=client;

    }

    @Override
    protected Void doInBackground(Void... voids) {

        StringBuilder builderResult=null;
        try {
            InputStream in;
            in=client.files().downloadBuilder(Constants.Personal_Friends_Folder_path).start().getInputStream();
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


        try {
            JSONObject jsonObject=new JSONObject(builderResult.toString());
            JSONArray jsonFriends=jsonObject.getJSONArray("friends");
            ListFoldersResult listFoldersResult=client.sharing().listFolders();
            Log.v("syncFriend",listFoldersResult.getEntries().toString());
            String email=client.users().getCurrentAccount().getEmail();
            for(int i=0;i<listFoldersResult.getEntries().size();i++){
                Log.v("syncFriend",listFoldersResult.getEntries().get(i).getName());
                if(listFoldersResult.getEntries().get(i).getName().contains("_"+email)){
                    Log.v("syncFriend","girdi");

                    int length=email.length();
                    String folderName=listFoldersResult.getEntries().get(i).getName();
                    String ownerEmail=folderName.substring(0,folderName.length()-(length+1));

                    SharedFolderMembers sharedFolderMembers=client.sharing().listFolderMembers(listFoldersResult.getEntries().get(i).getSharedFolderId());
                    for(int k=0;k<sharedFolderMembers.getUsers().size();k++){
                        if(sharedFolderMembers.getUsers().get(k).getUser().getEmail().equals(ownerEmail)){
                            JSONObject objectFriend=new JSONObject();
                            objectFriend.put("Name",sharedFolderMembers.getUsers().get(k).getUser().getDisplayName());
                            objectFriend.put("E-Mail",sharedFolderMembers.getUsers().get(k).getUser().getEmail());
                            objectFriend.put("Folder-Id",listFoldersResult.getEntries().get(i).getSharedFolderId());
                            objectFriend.put("Account-Id",sharedFolderMembers.getUsers().get(k).getUser().getAccountId());
                            objectFriend.put("Folder-Path",Constants.Friends_Folder_Path+"/"+folderName);
                            jsonFriends.put(objectFriend);
                            client.files().moveV2("/"+folderName,Constants.Friends_Folder_Path+"/"+folderName);
                            Log.v("syncFriend",sharedFolderMembers.getUsers().get(k).getUser().getEmail());


                        }
                    }
                    Log.v("syncFriend",jsonObject.toString());
                    InputStream in=new ByteArrayInputStream(jsonObject.toString().getBytes());
                    client.files().uploadBuilder(Constants.Personal_Friends_Folder_path).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);

                }
            }


        } catch (UploadErrorException e) {
            Log.v("syncFriend",e.getMessage()+"=error");

        } catch (DbxException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
