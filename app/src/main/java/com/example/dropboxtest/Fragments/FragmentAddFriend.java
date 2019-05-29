package com.example.dropboxtest.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dropboxtest.ApplicationProvider;
import com.example.dropboxtest.CreateGroupActivity;
import com.example.dropboxtest.R;

public class FragmentAddFriend extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_addfriend, container, false);
        getActivity().setTitle("Friend");

        final ApplicationProvider applicationProvider=new ApplicationProvider(v.getContext());
        final EditText editText=v.findViewById(R.id.editTextAddFriend);
        final EditText createGroupEditText=v.findViewById(R.id.editTextCreateGroup);
        Button button=v.findViewById(R.id.buttonAddFriend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editText.getText().toString();
                applicationProvider.addFriend2(email);

            }
        });
        Button createGroup=v.findViewById(R.id.buttonCreateGroup);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!createGroupEditText.getText().equals("")){
                    Intent intent=new Intent(v.getContext(),CreateGroupActivity.class);
                    intent.putExtra("groupName",editText.getText().toString());
                    startActivity(intent);
                }

            }
        });
        return v;
    }

}

