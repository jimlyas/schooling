package schooling.com.epizy.someone.schooling.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import schooling.com.epizy.someone.schooling.Adapter.subject_adapter;
import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.Model.subject_model;
import schooling.com.epizy.someone.schooling.R;
import schooling.com.epizy.someone.schooling.add_subject;

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
        database.data_subjects(list);

        adapter = new subject_adapter(this.getContext(), list);
        rc.setAdapter(adapter);


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK){
            list.add(new subject_model(data.getStringExtra("name"), data.getStringExtra("room"), data.getStringExtra("teacher"), data.getStringExtra("note")));
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this.getContext(), "Add subject canceled!", Toast.LENGTH_SHORT).show();
        }
        
    }
}
