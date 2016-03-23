package com.example.stognacci.friends;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//import android.support.v7.app.AlertDialog;

/**
 * Created by stognacci on 17/03/2016.
 */
public class FriendsDialog extends DialogFragment {


    private final String LOG_TAG = FriendsDialog.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE = "command";
    public static final String DELETE_RECORD = "deleteRecord";
    public static final String DELETE_DATABASE = "deleteDatabase";
    public static final String CONFIRM_EXIT = "confirmExit";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        final View view = mLayoutInflater.inflate(R.layout.friend_layout, null);
        String command = getArguments().getString(DIALOG_TYPE);
        TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
        if (command.equals(DELETE_RECORD)) {
            final int _id = getArguments().getInt(FriendsContract.FriendsColumns.FRIENDS_ID);
            String name = getArguments().getString(FriendsContract.FriendsColumns.FRIENDS_NAME);
            popupMessage.setText(getString(R.string.popup_delete_confirm, name));
            builder.setView(view)
                    .setPositiveButton(getString(R.string.popup_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), getString(R.string.delete_confirm_toast), Toast.LENGTH_SHORT).show();
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            Uri uri = FriendsContract.Friends.buildFriendUri(String.valueOf(_id));
                            contentResolver.delete(uri, null, null);
                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else if (command.equals(DELETE_DATABASE)) {
            popupMessage.setText(R.string.popup_deleteAll_confirm);
            builder.setView(view)
                    .setPositiveButton(getString(R.string.popup_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), R.string.deleteAll_confirm_toast, Toast.LENGTH_SHORT).show();
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            Uri uri = FriendsContract.URI_TABLE;
                            contentResolver.delete(uri, null, null);
                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else if (command.equals(CONFIRM_EXIT)) {
            popupMessage.setText(R.string.popup_exitNoSave_confirm);
            builder.setView(view)
                    .setPositiveButton(getString(R.string.popup_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.popup_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else {
            Log.d(LOG_TAG, "Invalid command passed as a parameter");
        }
        return builder.create();
    }

}
