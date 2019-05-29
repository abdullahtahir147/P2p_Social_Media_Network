package com.example.dropboxtest.AsyncTasks;

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
import com.dropbox.core.v2.sharing.AddFileMemberErrorException;
import com.dropbox.core.v2.sharing.AddMember;
import com.dropbox.core.v2.sharing.MemberSelector;
import com.dropbox.core.v2.sharing.ShareFolderErrorException;
import com.dropbox.core.v2.sharing.UserInfo;
import com.dropbox.core.v2.sharing.UserMembershipInfo;
import com.example.dropboxtest.Constants;
import com.example.dropboxtest.Objects.Friend;

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

public class AsyncAddFriend extends AsyncTask<Void,String,String> {
    private DbxClientV2 client;
    private String path;
    private Context context;
    private String eMail;
    private Friend newFriend;
    ProgressDialog progressDialog;

    public AsyncAddFriend(DbxClientV2 client,Context context,String path,String eMail){
        this.client=client;
        this.context=context;
        this.path=path;
        this.eMail=eMail;
        newFriend=new Friend();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Adding");
        progressDialog.show();

    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.v("addFriend","Started AsyncAddFriend");
        String response;
        String folderId="";
        ArrayList<Friend> mFriends=new ArrayList<>();
        Boolean ListChecker=false;


        StringBuilder builderResult=null;
        //Download Personal/Friends.txt for check
        //step 1

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


        //Step 2
        if(builderResult==null || builderResult.toString().equals("")){
            Log.v("Steps","Step 2 Cancelled");

        }else{
            try {

                JSONObject jsonObject=new JSONObject(builderResult.toString());
                JSONArray friendList=jsonObject.getJSONArray("friends");
                Log.v("Friend.txt",builderResult.toString());
                Log.v("Friend.txt",friendList.length()+"");

                Friend friend;
                if(Constants.arrayFriends.size()!=friendList.length()){
                    if(friendList.length()!=0){
                        ListChecker=true;
                        for(int i=0;i<friendList.length();i++){
                            friend=new Friend();
                            JSONObject friendElement=friendList.getJSONObject(i);
                            friend.seteMail(friendElement.getString("E-Mail"));
                            friend.setFolderId(friendElement.getString("Folder-Id"));
                            friend.setFolderPath(friendElement.getString("Folder-Path"));
                            friend.setName(friendElement.getString("Name"));
                            friend.setAccountId(friendElement.getString("Account-Id"));
                            //friend.setPpPath(friendElement.get("Pp-Path").toString());
                            mFriends.add(friend);
                            Log.v("AddFriendAsync", mFriends.size()+"=size");

                        }
                    }


                }else{
                   mFriends=Constants.arrayFriends;
                }



            } catch (JSONException e) {
                Log.v("DownloadPhase",e.getMessage());
            }

        }




        //Download and parsing completed


        try {
            //can be modified
            String localUserName=client.users().getCurrentAccount().getEmail();
            response=client.sharing().shareFolderBuilder(path+"/"+localUserName+"_"+eMail).start().toString();
            JSONObject jsonObject=new JSONObject(response);
            folderId=jsonObject.getString("shared_folder_id");
            newFriend.setFolderId(folderId);

        }
        catch (ShareFolderErrorException error){
            if( error.errorValue.isBadPath()){
                Log.v("TaskTest52",error.errorValue.toString());
                response="badPath";

            }

        } catch (DbxException e) {
            e.printStackTrace();
            response="error";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Adding folder Memer

        try {
            List<AddMember> array=new ArrayList<>();
            AddMember member= new AddMember(MemberSelector.email(eMail),AccessLevel.EDITOR);
            array.add(member);
            Log.v("folderID","folderid="+folderId);
            client.sharing().addFolderMember(folderId,array);

        } catch (AddFileMemberErrorException e){
            //e.errorValue.
        } catch (DbxException e) {
            e.printStackTrace();
            Log.v("addFriend",e.getMessage());
        }


        try {

            InputStream in=new ByteArrayInputStream(Constants.GetFriendTemplate().getBytes());
            String localUserName=client.users().getCurrentAccount().getEmail();
            client.files().uploadBuilder(path+"/"+localUserName+"_"+eMail+"/messages.txt").uploadAndFinish(in).toString();
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }






        //get user details
        try {
            String currentEmail=client.users().getCurrentAccount().getEmail();
            List<UserMembershipInfo> users=client.sharing().listFolderMembers(folderId).getUsers();

               UserInfo user=users.get(0).getUser();
                //friends to add will be implemented here
                for(int i=0;i<users.size();i++){
                    user=users.get(i).getUser();;
                    if(!user.getEmail().contains(currentEmail)){
                        break;
                    }

                }
                newFriend.setAccountId(user.getAccountId());
                newFriend.setName(user.getDisplayName());
                newFriend.seteMail(user.getEmail());
                newFriend.setFolderPath(Constants.Friends_Folder_Path+"/"+currentEmail+"_"+user.getEmail());
                Log.v("newFriend",newFriend.getAccountId());
                Log.v("newFriend",newFriend.geteMail());
                Log.v("newFriend",newFriend.getName());
                Log.v("newFriend",newFriend.getFolderPath());
                Log.v("newFriend",newFriend.getFolderId());
                //think
                //problems problems
                mFriends.add(newFriend);

        } catch (DbxException e) {
            e.printStackTrace();
        }
        if(ListChecker){
            //full upload
            Log.v("uploadAddFriend","Full");
            Constants.arrayFriends=mFriends;

            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(builderResult.toString());
                JSONArray jsonArray=jsonObject.getJSONArray("friends");
                while(jsonArray.length()>0){
                    jsonArray.remove(0);
                }
                JSONObject objectFriend;
                for(int p=0;p<mFriends.size();p++){
                    objectFriend=new JSONObject();
                    objectFriend.put("Name",mFriends.get(p).getName());
                    objectFriend.put("E-Mail",mFriends.get(p).getName());
                    objectFriend.put("Folder-Id",mFriends.get(p).getFolderId());
                    objectFriend.put("Account-Id",mFriends.get(p).getAccountId());
                    objectFriend.put("Folder-Path",mFriends.get(p).getFolderPath());
                    jsonArray.put(objectFriend);

                }
                InputStream in=new ByteArrayInputStream(jsonObject.toString().getBytes());
                client.files().uploadBuilder(Constants.Personal_Friends_Folder_path).uploadAndFinish(in).toString();

                Log.v("Steps","Deletion Completed");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UploadErrorException e) {
                e.printStackTrace();
            } catch (DbxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else{
            //partial upload
            Log.v("uploadAddFriend","Partial");
            try {
                JSONObject jsonObject=new JSONObject(builderResult.toString());
                JSONArray jsonArray=jsonObject.getJSONArray("friends");
                JSONObject objectNewFriend=new JSONObject();
                objectNewFriend.put("Name",newFriend.getName());
                objectNewFriend.put("E-Mail",newFriend.geteMail());
                objectNewFriend.put("Folder-Id",newFriend.getFolderId());
                objectNewFriend.put("Account-Id",newFriend.getAccountId());
                objectNewFriend.put("Folder-Path",newFriend.getFolderPath());
                jsonArray.put(jsonArray.length(),objectNewFriend);
                Log.v("JsonObject",jsonObject.toString());
                InputStream in=new ByteArrayInputStream(jsonObject.toString().getBytes());
                client.files().uploadBuilder(Constants.Personal_Friends_Folder_path).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
                Log.v("JsonObject","Upload Completed");


            } catch (JSONException e) {
                Log.v("JsonError","1"+e.getMessage());
                e.printStackTrace();
            } catch (UploadErrorException e) {
                Log.v("JsonError","2"+e.getMessage());
                e.printStackTrace();
            } catch (DbxException e) {
                Log.v("JsonError","3"+e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.v("JsonError","4"+e.getMessage());
                e.printStackTrace();
            }


        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }
}

