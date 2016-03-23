package com.example.stognacci.friends;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by stognacci on 17/03/2016.
 */
public class EditActivity extends AppCompatActivity {
    private final String LOG_TAG = EditActivity.class.getSimpleName();
    private TextView mNameTextView, mEmailTextView, mPhoneTextView;
    private Button mButton;
    private ContentResolver mContentResolver;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mContentResolver = EditActivity.this.getContentResolver();

        mNameTextView = (TextView) findViewById(R.id.friendName);
        mEmailTextView = (TextView) findViewById(R.id.friendEmail);
        mPhoneTextView = (TextView) findViewById(R.id.friendPhone);

        Intent intent = getIntent();
        final String _id = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_ID);
        final String name = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_NAME);
        String phone = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_PHONE);
        String email = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_EMAIL);

        mNameTextView.setText(name);
        mPhoneTextView.setText(phone);
        mEmailTextView.setText(email);

        mButton = (Button) findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    ContentValues values = new ContentValues();
                    values.put(FriendsContract.FriendsColumns.FRIENDS_NAME, mNameTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_PHONE, mPhoneTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_EMAIL, mEmailTextView.getText().toString());
                    Uri uri = FriendsContract.Friends.buildFriendUri(_id);
                    int returned = mContentResolver.update(uri, values, null, null);
                    Log.d(LOG_TAG, "number of records updated: " + returned);
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.snackbar_edit_emptyValues, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValid() {
        if (mNameTextView.getText().toString().length() == 0 ||
                mPhoneTextView.getText().toString().length() == 0 ||
                mEmailTextView.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
    }
//TODO: When pressing back or back from ActionBar, return to parent activity (not always to main activity)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Log.d(LOG_TAG, "onOptionsItemSelected pressed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
