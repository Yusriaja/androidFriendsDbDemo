package com.example.stognacci.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(android.R.id.content) == null) {
            FriendsListFragment friendsListFragment = new FriendsListFragment();
            fragmentManager.beginTransaction().add(android.R.id.content, friendsListFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addRecord:
                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addIntent);
                break;
            case R.id.deleteDatabase:
                FriendsDialog friendsDialog = new FriendsDialog();
                Bundle args = new Bundle();
                args.putString(FriendsDialog.DIALOG_TYPE, FriendsDialog.DELETE_DATABASE);
                friendsDialog.setArguments(args);
                friendsDialog.show(getSupportFragmentManager(), "delete-database");
                break;
            case R.id.searchRecords:
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
