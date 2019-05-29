package com.example.dropboxtest.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.example.dropboxtest.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AsyncCheckFolders extends AsyncTask<Void,Void,Void> {
    DbxClientV2 client;

    public AsyncCheckFolders(DbxClientV2 client){
        this.client=client;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Boolean folderChecker=false;
        try {
            client.files().listFolder(Constants.Personal_Folder_path);
            Log.v("folderChecker","calıstı");
        } catch (ListFolderErrorException e) {
            String result=e.errorValue.toString();
            Log.v("folderChecker",result);
            folderChecker=true;
        }
        catch (DbxException e) {
            e.printStackTrace();
        }
        Log.v("folderChecker","uçuyorsun melih");
        if(folderChecker){
            try {
                client.files().createFolderV2(Constants.Personal_Folder_path);
                InputStream in=new ByteArrayInputStream(Constants.getPersonalTemplate().getBytes());
                client.files().uploadBuilder(Constants.Personal_Friends_Folder_path).uploadAndFinish(in);

                InputStream in2=new ByteArrayInputStream(Constants.getPersonalGroupTemplate().getBytes());
                client.files().uploadBuilder(Constants.Personal_Groups_Folder_path).uploadAndFinish(in2);
            } catch (DbxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
