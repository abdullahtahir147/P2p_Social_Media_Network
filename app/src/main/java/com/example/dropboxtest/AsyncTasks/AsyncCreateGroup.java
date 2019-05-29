package com.example.dropboxtest.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.AccessLevel;
import com.dropbox.core.v2.sharing.AddMember;
import com.dropbox.core.v2.sharing.MemberSelector;
import com.dropbox.core.v2.sharing.ShareFolderLaunch;
import com.dropbox.core.v2.sharing.SharedFolderMetadata;
import com.example.dropboxtest.Constants;
import com.example.dropboxtest.Objects.Friend;
import com.example.dropboxtest.Objects.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AsyncCreateGroup extends AsyncTask<Void,Void,Void> {
    private DbxClientV2 client;
    private String groupName;
    private ArrayList<Friend> friendArrayList;
    private Context context;
    private Group group=new Group();
    private Activity activity;
    private ProgressDialog progressDialog;
    public AsyncCreateGroup(DbxClientV2 client,Activity activity,Context context,String groupName,ArrayList<Friend> friendArrayList){
        this.client=client;
        this.groupName=groupName;
        this.friendArrayList=friendArrayList;
        this.activity=activity;
        this.context=context;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        activity.finish();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Creating Group");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        boolean groupFileIsExist=true;
        String groupTxt="";

        try {
            InputStream in;
            in=client.files().downloadBuilder(Constants.Personal_Groups_Folder_path).start().getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder builderResult = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                builderResult.append(line).append('\n');
            }
            groupTxt=builderResult.toString();
        } catch (DownloadErrorException e){
            String message=e.errorValue.toString();
            Log.v("CreateGroup",message);
            groupFileIsExist=false;
        }
        catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!groupFileIsExist){
            InputStream in=new ByteArrayInputStream(Constants.getPersonalGroupTemplate().getBytes());
            try {
                client.files().uploadBuilder(Constants.Personal_Groups_Folder_path).uploadAndFinish(in);
                groupTxt=Constants.getGroupTemplate();
            } catch (DbxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        String groupPath=Constants.Group_Folder_path+"/"+groupName+"@P2PSN";
        String folderId;

        try {
            //create and share folder
            ShareFolderLaunch shareFolderLaunch=client.sharing().shareFolderBuilder(groupPath).start();
            SharedFolderMetadata sharedFolderMetadata=shareFolderLaunch.getCompleteValue();
            folderId=sharedFolderMetadata.getSharedFolderId();
            group.setFolderId(folderId);
            group.setFolderPath(groupPath);
            group.setGroupName(groupName);

            //upload Group.txt

            InputStream in=new ByteArrayInputStream(Constants.getGroupTemplate().getBytes());
            client.files().uploadBuilder(groupPath+"/Group.txt").uploadAndFinish(in);
            

            //add folder members to group
            List<AddMember> members=new ArrayList<>();
            for(int i=0;i<friendArrayList.size();i++){
                AddMember member= new AddMember(MemberSelector.email(friendArrayList.get(i).geteMail()),AccessLevel.EDITOR);
                members.add(member);
            }
            client.sharing().addFolderMember(folderId,members);

        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            JSONObject jsonObject=new JSONObject(groupTxt);
            JSONArray jsonArray=jsonObject.getJSONArray("groups");
            JSONObject newGroup=new JSONObject();
            newGroup.put("groupName", group.getGroupName());
            newGroup.put("groupPath", group.getFolderPath());
            newGroup.put("folderId",group.getFolderId());
            jsonArray.put(newGroup);


            Constants.arrayGropus=new ArrayList<>();
            Group group;
            JSONObject object;
            for(int i=0;i<jsonArray.length();i++){
                group=new Group();
                object=jsonArray.getJSONObject(i);
                group.setGroupName(object.getString("groupName"));
                group.setFolderPath(object.getString("groupPath"));
                group.setFolderId(object.getString("folderId"));
                Constants.arrayGropus.add(group);

            }
            Log.v("CreateGroup", jsonObject.toString());
            Log.v("CreateGroup", "Array="+Constants.arrayGropus.toString());
            InputStream in=new ByteArrayInputStream(jsonObject.toString().getBytes());
            client.files().uploadBuilder(Constants.Personal_Groups_Folder_path).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
            Log.v("CreateGroup", "Finished");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("CreateGroup", "JsonError:"+ e.getMessage());
        } catch (UploadErrorException e) {
            e.printStackTrace();
            Log.v("CreateGroup", "UploadError:"+ e.getMessage());
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
