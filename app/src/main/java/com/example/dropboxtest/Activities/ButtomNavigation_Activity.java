package com.example.dropboxtest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dropboxtest.ApplicationProvider;
import com.example.dropboxtest.Fragments.FragmentAddFriend;
import com.example.dropboxtest.Fragments.FragmentHome;
import com.example.dropboxtest.Fragments.FragmentNotifications;
import com.example.dropboxtest.Fragments.FragmentProfile;
import com.example.dropboxtest.R;

public class ButtomNavigation_Activity extends AppCompatActivity {
    ApplicationProvider applicationProvider = new ApplicationProvider(this);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment SelectedFragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    SelectedFragment=new FragmentHome();
                    break;

                case R.id.navigation_dashboard:
                    SelectedFragment=new FragmentAddFriend();
                    break;

                case R.id.navigation_notifications:
                    SelectedFragment=new FragmentNotifications();
                    break;
                case R.id.navigation_profile:
                    SelectedFragment=new FragmentProfile();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,SelectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_buttom_nav, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sendMessage) {

            applicationProvider.updateFriends();
            Intent intent=new Intent(ButtomNavigation_Activity.this,Messenger.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttom_navigation_);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentHome()).commit();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        applicationProvider.checkFolders();
        applicationProvider.syncFriends();
        applicationProvider.syncGroups();
        applicationProvider.updateFriends();


    }

}
