package com.example.dropboxtest.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dropboxtest.R;

public class FragmentHome extends Fragment {
    private LinearLayout photos, camera;
    LinearLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        View v= inflater.inflate(R.layout.fragment_home,container,false);
        photos = (LinearLayout) v.findViewById(R.id.btnphotos);
        camera = (LinearLayout) v.findViewById(R.id.btncamera);
        linearLayout = (LinearLayout) v.findViewById(R.id.lin1);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this,'h',Toast.LENGTH_LONG).show();
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });
        return v;
    }
}

