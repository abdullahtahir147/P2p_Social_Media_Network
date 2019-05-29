package com.example.dropboxtest.AsyncTasks;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.example.dropboxtest.TaskCompleted;

public class AsyncCreateDirectory extends AsyncTask<Void, String, String> {
    private String path;
    private boolean autoRename;
    private DbxClientV2 client;
    public TaskCompleted taskCompleted;


    public AsyncCreateDirectory(DbxClientV2 client, String path, boolean autoRename, TaskCompleted taskCompleted){
        this.path=path;
        this.autoRename=autoRename;
        this.client=client;
        this.taskCompleted=taskCompleted;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result= null;
        try {
            result = client.files().createFolderV2(path,autoRename).getMetadata().toString();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        taskCompleted.onTaskComplete(result);
    }
}
