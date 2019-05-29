package com.example.dropboxtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dropboxtest.Objects.Friend;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity implements OnItemClickListener,SearchView.OnQueryTextListener {
    ArrayList<Friend> friendArrayList=Constants.arrayFriends;
    CreateGroupAdapter createGroupAdapter;
    ArrayList<Friend> clickedFriends=new ArrayList<>();
    ApplicationProvider applicationProvider=new ApplicationProvider(this,this);
    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        RecyclerView recyclerView=findViewById(R.id.createGroupRecyclerView);
        createGroupAdapter=new CreateGroupAdapter(friendArrayList,clickedFriends,this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(createGroupAdapter);
        Intent intent=getIntent();
        groupName=intent.getExtras().getString("groupName","null");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem menuItem=menu.findItem(R.id.searchButton);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String input=s.toLowerCase();
        ArrayList<Friend> newList=new ArrayList<>();
        for (Friend friend:friendArrayList) {
            if(friend.getName().toLowerCase().contains(input)){
                newList.add(friend);
            }
        }
        createGroupAdapter.updateList(newList);
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.v("createGroup","clicked "+clickedItemIndex);

    }
}
