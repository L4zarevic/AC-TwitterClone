package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Twitter extends AppCompatActivity  {

    //Variables
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TapAdapter tabAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);



        //Switching between tabs
        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TapAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        //

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager, true);







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logoutUserItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(Twitter.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


                break;

            case R.id.sendTweeItem:
                Intent intent = new Intent(Twitter.this, SendTweetActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
