package com.example.dropboxtest.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dropboxtest.ApplicationProvider;
import com.example.dropboxtest.R;

public class FragmentNotifications extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        getActivity().setTitle("Notifications");

        final ApplicationProvider applicationProvider=new ApplicationProvider(v.getContext());

        final EditText editText=v.findViewById(R.id.editText);
        Button ListFolders=v.findViewById(R.id.buttonListFolders);
        ListFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editText.getText().toString();
            }
        });
        Button AddFriend=v.findViewById(R.id.buttonAddFriend);
        AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editText.getText().toString();
            }
        });
        Button CreateFolder=v.findViewById(R.id.buttonCreateFolder);
        CreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editText.getText().toString();
                applicationProvider.addFriend2(email);
            }
        });
        Button UploadString=v.findViewById(R.id.buttonUploadString);
        UploadString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editText.getText().toString();
            }
        });
        Button ShareFolder=v.findViewById(R.id.buttonShareFolder);
        ShareFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path=editText.getText().toString();
            }
        });
        return v;
    }
}
