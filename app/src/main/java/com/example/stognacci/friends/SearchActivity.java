package com.example.stognacci.friends;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stognacci on 18/03/2016.
 */
public class SearchActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Friend>> {
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private FriendsCustomAdapter mFriendsCustomAdapter;
    private static int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<Friend> mFriendsRetrieved;
    private ListView mListView;
    private EditText mSearchEditText;
    private Button mSearchFriendButton;
    private String matchText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        mListView = (ListView) findViewById(R.id.searchResultList);
        mSearchFriendButton = (Button) findViewById(R.id.searchButton);
        mSearchEditText = (EditText) findViewById(R.id.searchName);
        mContentResolver = getContentResolver();
        mFriendsCustomAdapter = new FriendsCustomAdapter(SearchActivity.this, getSupportFragmentManager());
        mListView.setAdapter(mFriendsCustomAdapter);

        mSearchFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchText = mSearchEditText.getText().toString();
                getSupportLoaderManager().initLoader(LOADER_ID++, null, SearchActivity.this);
                // HideKeyboard after pressing Search
                View view = getCurrentFocus();
                if (view != null) {
                    mSearchEditText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        });

    }

    @Override
    public Loader<List<Friend>> onCreateLoader(int id, Bundle args) {
        return new FriendsSearchListLoader(SearchActivity.this, FriendsContract.URI_TABLE, this.mContentResolver, matchText);
    }


    @Override
    public void onLoadFinished(Loader<List<Friend>> loader, List<Friend> friends) {
        mFriendsCustomAdapter.setData(friends);
        this.mFriendsRetrieved = friends;
        if (mFriendsCustomAdapter.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.search_noFriendsSnackBar, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Friend>> loader) {
        mFriendsCustomAdapter.setData(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("friend_list", (ArrayList) mFriendsRetrieved);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFriendsRetrieved = (List) savedInstanceState.getParcelableArrayList("friend_list");
        if (mFriendsRetrieved != null) {
            mFriendsCustomAdapter.setData(mFriendsRetrieved);
        }
    }
}
