package schooling.com.epizy.someone.schooling.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Objects;

import schooling.com.epizy.someone.schooling.adapters.subject_adapter;
import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.models.subject_model;
import schooling.com.epizy.someone.schooling.R;

import static android.app.Activity.RESULT_OK;

public class subjects extends Fragment {
    MaterialDialog.Builder mdb; DBHelper database; RecyclerView rc;
    ArrayList<subject_model> list; subject_adapter adapter;
    public subjects() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.f_subjects, container, false);
        mdb = new MaterialDialog.Builder(Objects.requireNonNull(this.getContext()));
        database = new DBHelper(this.getContext());
        rc = v.findViewById(R.id.rc_subject);
        list = new ArrayList<>();

        rc.setHasFixedSize(true); rc.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return v;
    }

    @Override
    public void onStart() {
        if(!list.isEmpty()){
            list.clear();database.data_subjects(list);
        }else{
            database.data_subjects(list);
        }
        adapter = new subject_adapter(this.getContext(), list);
        rc.setAdapter(adapter);
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==11&&resultCode==RESULT_OK){
            subject_model newest = new subject_model(data.getStringExtra("name"), data.getStringExtra("room"), data.getStringExtra("teacher"), data.getStringExtra("note"));
            newest.setId(data.getStringExtra("id"));
            list.add(newest);
            adapter.notifyDataSetChanged();
        }else if(requestCode==12&&resultCode==RESULT_OK){
            subject_model newest = new subject_model(data.getStringExtra("name"), data.getStringExtra("room"), data.getStringExtra("teacher"), data.getStringExtra("note"));
            newest.setId(data.getStringExtra("id"));
            list.set(Integer.valueOf(data.getStringExtra("index")), newest);
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this.getContext(), "Add subject canceled!", Toast.LENGTH_SHORT).show();
        }
        
    }
}
