package com.example.ac_twitterclone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private ArrayList<HashMap<String, String>> tweetList;
    private SimpleAdapter adapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;


    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        listView = view.findViewById(R.id.listOfTweets);
        mySwipeRefreshLayout = view.findViewById(R.id.swipeContainer);


        tweetList = new ArrayList<>();
        adapter = new SimpleAdapter(getContext(), tweetList, android.R.layout.simple_list_item_2, new String[]{"tweetUserName", "tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});

        mySwipeRefreshLayout.setOnRefreshListener(this);
        try {

            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.whereNotContainedIn("tweet", tweetList);
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject tweetObject : objects) {
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweetList.add(userTweet);
                        }
                        listView.setAdapter(adapter);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    //Manual refreshing home pages
    @Override
    public void onRefresh() {

        tweetList = new ArrayList<>();
        adapter = new SimpleAdapter(getContext(), tweetList, android.R.layout.simple_list_item_2, new String[]{"tweetUserName", "tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});

        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf")).whereNotContainedIn("tweet", tweetList);
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject tweetObject : objects) {
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweetList.add(userTweet);
                        }
                        listView.setAdapter(adapter);
                        if (mySwipeRefreshLayout.isRefreshing()) {
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    } else {
                        if (mySwipeRefreshLayout.isRefreshing()) {
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
