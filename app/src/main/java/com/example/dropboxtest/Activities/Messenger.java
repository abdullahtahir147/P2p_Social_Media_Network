package com.example.dropboxtest.Activities;

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

import com.example.dropboxtest.Constants;
import com.example.dropboxtest.Objects.Friend;
import com.example.dropboxtest.MessengerRVAdapter;
import com.example.dropboxtest.OnItemClickListener;
import com.example.dropboxtest.R;

import java.util.ArrayList;

public class Messenger extends AppCompatActivity implements OnItemClickListener,SearchView.OnQueryTextListener {


    ArrayList<Friend> friends;
    MessengerRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        friends=Constants.arrayFriends;


        adapter=new MessengerRVAdapter(this,friends,this);
        RecyclerView recyclerView=findViewById(R.id.messengerRecycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

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
    public void onListItemClick(int clickedItemIndex) {
        Log.v("rvtest",Constants.arrayFriends.get(clickedItemIndex).getName());
        Intent intent=new Intent(Messenger.this,MessageActivity.class);
        intent.putExtra("receiver",clickedItemIndex);
        startActivity(intent);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String input=s.toLowerCase();
        ArrayList<Friend> newList=new ArrayList<>();
        for (Friend friend:friends) {
            if(friend.getName().toLowerCase().contains(input)){
                newList.add(friend);
            }
        }
        adapter.updateList(newList);
        return true;
    }
}
