package schooling.com.epizy.someone.schooling.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
    FloatingActionButton fab;MaterialDialog.Builder mdb; DBHelper database; RecyclerView rc;
    ArrayList<subject_model> list; subject_adapter adapter;
    public subjects() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.f_subjects, container, false);
        fab = v.findViewById(R.id.fab_subject);
        mdb = new MaterialDialog.Builder(Objects.requireNonNull(this.getContext()));
        database = new DBHelper(this.getContext());
        rc = v.findViewById(R.id.rc_subject);
        list = new ArrayList<>();

        rc.setHasFixedSize(true); rc.setLayoutManager(new LinearLayoutManager(this.getContext()));
        database.data_subjects(list);

        adapter = new subject_adapter(this.getContext(), list);
        rc.setAdapter(adapter);

        initSwipe(rc, v);
        setupfloating();

        return v;
    }

    private void initSwipe(RecyclerView rc, final View v) {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                subject_model current = adapter.getItem(index);

                if(direction==ItemTouchHelper.LEFT||direction==ItemTouchHelper.RIGHT){
                    if(database.deletesubject(current.name)){
                        adapter.removeItem(index);
                        Snackbar.make(v.findViewById(R.id.root_f_subject), "Subject Deleted!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rc);
    }

    private void setupfloating() {
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(subjects.this.getContext(), add_subject.class), 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==RESULT_OK){
            list.add(new subject_model(data.getStringExtra("name"), data.getStringExtra("room"), data.getStringExtra("teacher"), data.getStringExtra("note")));
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this.getContext(), "Add subject canceled!", Toast.LENGTH_SHORT).show();
        }
        
    }
}
