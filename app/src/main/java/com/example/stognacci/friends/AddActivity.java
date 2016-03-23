package com.example.stognacci.friends;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by stognacci on 18/03/2016.
 */
public class AddActivity extends AppCompatActivity {

    private final String LOG_TAG = AddActivity.class.getSimpleName();
    private TextView mNameTextView, mEmailTextView, mPhoneTextView;
    private Button mButton;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mContentResolver = AddActivity.this.getContentResolver();

        mNameTextView = (TextView) findViewById(R.id.friendName);
        mEmailTextView = (TextView) findViewById(R.id.friendEmail);
        mPhoneTextView = (TextView) findViewById(R.id.friendPhone);
        mButton = (Button) findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    ContentValues values = new ContentValues();
                    values.put(FriendsContract.FriendsColumns.FRIENDS_NAME, mNameTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_PHONE, mPhoneTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_EMAIL, mEmailTextView.getText().toString());
                    Uri returned = mContentResolver.insert(FriendsContract.URI_TABLE, values);
                    Log.d(LOG_TAG, "record id returned is " + returned.toString());
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    //TODO: use SnackBar instead of Toast
                    //Toast.makeText(AddActivity.this, "Make sure you've entered valid values", Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(android.R.id.content), R.string.snackbar_edit_emptyValues, Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }

    //TODO: insert proper validation for email and phone number
    private boolean isValid() {
        if (mNameTextView.getText().toString().length() == 0 ||
                mPhoneTextView.getText().toString().length() == 0 ||
                mEmailTextView.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean someDataEntered() {
        if (mNameTextView.getText().toString().length() > 0 ||
                mPhoneTextView.getText().toString().length() > 0 ||
                mEmailTextView.getText().toString().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (someDataEntered()) {
            FriendsDialog cancelDialog = new FriendsDialog();
            Bundle args = new Bundle();
            args.putString(FriendsDialog.DIALOG_TYPE, FriendsDialog.CONFIRM_EXIT);
            cancelDialog.setArguments(args);
            cancelDialog.show(getSupportFragmentManager(), "confirm-exit");

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
