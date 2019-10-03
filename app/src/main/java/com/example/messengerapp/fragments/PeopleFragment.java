package com.example.messengerapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.messengerapp.R;


public class PeopleFragment extends Fragment {

    TextView title_toolbar_textview;

    public PeopleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        title_toolbar_textview = getActivity().findViewById(R.id.title_toolbar_textview);
        title_toolbar_textview.setText("People");
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

}
