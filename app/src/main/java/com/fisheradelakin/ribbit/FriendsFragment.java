package com.fisheradelakin.ribbit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/*
 * Created by Fisher on 2/20/15.
 */
public class FriendsFragment extends ListFragment {

    public static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // in the case i don't understand what the return state is doing
        // it's returning the line of code that's commented out.
        // View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if(e == null) {
                    mFriends = friends;

                    String[] usernames = new String[mFriends.size()];

                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            usernames);
                    setListAdapter(adapter);
                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}