package com.example.ac_twitterclone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listOfUsers;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;


    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        listOfUsers = view.findViewById(R.id.listOfUsers);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_checked, arrayList);
        listOfUsers.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listOfUsers.setOnItemClickListener(this);

        try {


            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (users.size() > 0 && e == null) {

                        for (ParseUser user : users) {
                            arrayList.add(user.getUsername());
                        }

                        listOfUsers.setAdapter(arrayAdapter);


                        //Control that no duplicate values are added to the array in column fanOf
                        for (String twitterUser : arrayList) {
                            if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                    listOfUsers.setItemChecked(arrayList.indexOf(twitterUser), true);
                                }
                            }
                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()) {
            FancyToast.makeText(getContext(), arrayList.get(position) + " is now followed", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
            ParseUser.getCurrentUser().add("fanOf", arrayList.get(position));
        } else {
            FancyToast.makeText(getContext(), arrayList.get(position) + " is now unfollowed", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();

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
                    FancyToast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                }
            }
        });
    }
}
