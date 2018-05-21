package schooling.com.epizy.someone.schooling.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import schooling.com.epizy.someone.schooling.Adapter.libraries_adapter;
import schooling.com.epizy.someone.schooling.Model.library_source;
import schooling.com.epizy.someone.schooling.R;

public class open_source extends Fragment {
    RecyclerView rc; libraries_adapter adapter;
    ArrayList<library_source> list;

    public open_source() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.f_open_source, container, false);
        rc = v.findViewById(R.id.rc_open_source);
        list = new ArrayList<>();
        adapter = new libraries_adapter(this.getContext(), list);
        initData();
        rc.setHasFixedSize(true); rc.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rc.setAdapter(adapter);
        return v;
    }

    private void initData() {
        list.add(new library_source("material-dialog", "https://github.com/afollestad/material-dialogs"));
        list.add(new library_source("CircleImageView", "https://github.com/hdodenhof/CircleImageView"));
        list.add(new library_source("TimeTable", "https://github.com/EunsilJo/TimeTable"));
        list.add(new library_source("MaterialDateTimePicker", "https://github.com/wdullaer/MaterialDateTimePicker"));
    }

}
