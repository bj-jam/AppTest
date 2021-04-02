package com.app.ui.material.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ui.R;
import com.app.ui.material.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MainFragment extends Fragment {


    private static final String ARG_TITLE = "title";
    RecyclerView recyclerview;

    private List<String> mDatas;
    private String mTitle;

    public MainFragment() {
    }

    public static MainFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mTitle = bundle.getString(ARG_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerview = view.findViewById(R.id.recyclerview);
        initData();
        recyclerview.setLayoutManager(new LinearLayoutManager(recyclerview.getContext()));
        recyclerview.setAdapter(new RecyclerAdapter(recyclerview.getContext(), mDatas));
        return view;
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add(mTitle + (char) i);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
