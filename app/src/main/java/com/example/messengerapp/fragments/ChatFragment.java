package com.example.messengerapp.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.messengerapp.ProfileActivity;
import com.example.messengerapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {

    TextView title_toolbar_textview;

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        title_toolbar_textview = getActivity().findViewById(R.id.title_toolbar_textview);
        title_toolbar_textview.setText("Chats");
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

}
