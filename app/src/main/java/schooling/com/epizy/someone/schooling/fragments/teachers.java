package schooling.com.epizy.someone.schooling.fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Objects;

import schooling.com.epizy.someone.schooling.Adapter.teacher_adapter;
import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.Model.teacher_model;
import schooling.com.epizy.someone.schooling.R;

public class teachers extends Fragment {
    final OvershootInterpolator oi = new OvershootInterpolator();
    FloatingActionButton fab; boolean fab_clicked = true;
    MaterialDialog.Builder mdb; DBHelper database; RecyclerView rc;
    ArrayList<teacher_model> list; teacher_adapter adapter;
    public teachers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_teachers, container, false);
        fab = v.findViewById(R.id.fab_teacher);
        mdb = new MaterialDialog.Builder(Objects.requireNonNull(this.getContext()));
        database = new DBHelper(this.getContext());
        rc = v.findViewById(R.id.rc_teacher);
        list = new ArrayList<>();
        database.data_teachers(list);
        adapter = new teacher_adapter(this.getContext(), list);
        rc.setHasFixedSize(true); rc.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rc.setAdapter(adapter);
        setupDialog();
        setupFloating();
        return v;
    }

    private void setupDialog() {
        @SuppressLint("InflateParams") final View v = getLayoutInflater().inflate(R.layout.d_add_teacher, null);
        final TextView input_name = v.findViewById(R.id.input_name_teacher);
        final TextView input_phone = v.findViewById(R.id.input_phone_teacher);
        mdb.title("Add Teacher").customView(v, false).positiveText("Add").negativeText("Cancel");

        mdb.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                String name = input_name.getText().toString();
                String phone = input_phone.getText().toString();

                if(name.length()==0||phone.length()==0){
                    if(database.add_teacher(new teacher_model(name, phone))){
                        list.add(new teacher_model(name, phone)); adapter.notifyDataSetChanged();
                        Toast.makeText(teachers.this.getContext(), "Teacher Added!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(teachers.this.getContext(), "Adding teacher failed!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(teachers.this.getContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);dialog.dismiss();
            }
        });

        mdb.dismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss(); fab.performClick();
                input_name.setText(null); input_phone.setText(null);
            }
        });
    }

    private void setupFloating() {
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fab_clicked){
                    fab_clicked = false;
                    ViewCompat.animate(fab).rotation(135f).withLayer().setDuration(300).setInterpolator(oi).start();
                    mdb.show();
                }else{
                    fab_clicked = true;
                    ViewCompat.animate(fab).rotation(0f).withLayer().setDuration(300).setInterpolator(oi).start();
                }
            }
        });
    }
}
