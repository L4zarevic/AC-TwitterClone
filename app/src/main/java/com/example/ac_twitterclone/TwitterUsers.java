package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Variables
    private Toolbar toolbar;
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);


        toolbar = findViewById(R.id.myToolbar);
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(TwitterUsers.this, android.R.layout.simple_list_item_checked, arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(TwitterUsers.this);


        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (users.size() > 0 && e == null) {

                    for (ParseUser user : users) {
                        arrayList.add(user.getUsername());
                    }

                    listView.setAdapter(arrayAdapter);
                    listView.setVisibility(View.VISIBLE);

                    //Control that no duplicate values are added to the array in column fanOf
                    for (String twitterUser : arrayList) {
                        if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                            if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                listView.setItemChecked(arrayList.indexOf(twitterUser), true);
                            }
                        }
                    }
                }

            }
        });
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
                        Intent intent = new Intent(TwitterUsers.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


                break;

            case R.id.sendTweeItem:
                Intent intent = new Intent(TwitterUsers.this, SendTweetActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()) {
            FancyToast.makeText(TwitterUsers.this, arrayList.get(position) + " is now followed", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
            ParseUser.getCurrentUser().add("fanOf", arrayList.get(position));
        } else {
            FancyToast.makeText(TwitterUsers.this, arrayList.get(position) + " is now unfollowed", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();

            /*Unfollow users*/
            //Removing value from array in column fanOf
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));

            //Adding values from array in list
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");

            //Removing all column fanOf
            ParseUser.getCurrentUser().remove("fanOf");

            //Create new column fanOf with values from list
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);

        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(TwitterUsers.this, "Saved", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                }
            }
        });
    }
}
