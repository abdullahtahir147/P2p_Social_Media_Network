package com.example.dropboxtest;

import android.app.Activity;
import android.content.Context;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.example.dropboxtest.AsyncTasks.AsyncAddFriend;
import com.example.dropboxtest.AsyncTasks.AsyncCheckFolders;
import com.example.dropboxtest.AsyncTasks.AsyncCreateGroup;
import com.example.dropboxtest.AsyncTasks.AsyncSendMessage;
import com.example.dropboxtest.AsyncTasks.AsyncSyncFriends;
import com.example.dropboxtest.AsyncTasks.AsyncSyncGroups;
import com.example.dropboxtest.AsyncTasks.AsyncUpdateFriends;
import com.example.dropboxtest.AsyncTasks.AsyncUpdateMessages;
import com.example.dropboxtest.Objects.Friend;
import com.example.dropboxtest.Objects.MessageSample;

import java.util.ArrayList;

public class ApplicationProvider {

    private DbxClientV2 client;
    private DbxRequestConfig config;
    private Context context;
    private Activity activity;

    public ApplicationProvider(Context context){
        this.config = DbxRequestConfig.newBuilder("dropbox").build();
        this.client = new DbxClientV2(config, Constants.ACCESS_TOKEN);
        this.context=context;

    }
    public ApplicationProvider(Activity activity,Context context){
        this.config = DbxRequestConfig.newBuilder("dropbox").build();
        this.client = new DbxClientV2(config, Constants.ACCESS_TOKEN);
        this.activity=activity;
        this.context=context;


    }

    public void addFriend2(final String email){
        String path=Constants.Friends_Folder_Path;
        AsyncAddFriend asyncAddFriend=new AsyncAddFriend(client,context,path,email);
        asyncAddFriend.execute();

    }
    public void sendMessage(String path, String message, String sender, String time, ArrayList<MessageSample> messageSamples){
        AsyncSendMessage asyncSendMessage=new AsyncSendMessage(client,path,message,sender,time,messageSamples);
        asyncSendMessage.execute();
    }
    public void updateFriends(){
        AsyncUpdateFriends asyncUpdateFriends=new AsyncUpdateFriends(client,context);
        asyncUpdateFriends.execute();
    }
    public void updateMessages(String path,ArrayList<MessageSample> messageSamples){
        AsyncUpdateMessages asyncUpdateMessages=new AsyncUpdateMessages(client,path,messageSamples);
        asyncUpdateMessages.execute();
    }
    public void syncFriends(){
        AsyncSyncFriends asyncSyncFriends=new AsyncSyncFriends(client);
        asyncSyncFriends.execute();
    }
    public void checkFolders(){
        AsyncCheckFolders asyncCheckFolders=new AsyncCheckFolders(client);
        asyncCheckFolders.execute();
    }
    public void createGroup(String name,ArrayList<Friend> arrayFriends){
        AsyncCreateGroup asyncCreateGroup=new AsyncCreateGroup(client,activity,context,name,arrayFriends);
        asyncCreateGroup.execute();
    }
    public void syncGroups(){
        AsyncSyncGroups asyncSyncGroups=new AsyncSyncGroups(client);
        asyncSyncGroups.execute();
    }

}
