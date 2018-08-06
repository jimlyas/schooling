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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
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
    public MaterialDialog.Builder mdb; DBHelper database; RecyclerView rc;
    ArrayList<teacher_model> list; teacher_adapter adapter;
    public teachers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_teachers, container, false);
        mdb = new MaterialDialog.Builder(Objects.requireNonNull(this.getContext()));
        database = new DBHelper(this.getContext());
        rc = v.findViewById(R.id.rc_teacher);
        list = new ArrayList<>();

        rc.setHasFixedSize(true); rc.setLayoutManager(new LinearLayoutManager(this.getContext()));


        setupDialog();
        return v;
    }

    @Override
    public void onStart() {
        if (!list.isEmpty()){
            list.clear(); database.data_teachers(list);
        }else{
            database.data_teachers(list);
        }
        adapter = new teacher_adapter(this.getContext(), list);
        rc.setAdapter(adapter);
        super.onStart();
    }

    private void setupDialog() {
        @SuppressLint("InflateParams") final View v = getLayoutInflater().inflate(R.layout.d_add_teacher, null);
        final EditText input_name = v.findViewById(R.id.input_name_teacher);
        final EditText input_phone = v.findViewById(R.id.input_phone_teacher);
        mdb.title("Add Teacher").customView(v, false).positiveText("Add").negativeText("Cancel").autoDismiss(true);

        mdb.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                String name = input_name.getText().toString();
                String phone = input_phone.getText().toString();


                if(!name.matches("")||!phone.matches("")){
                    if(database.add_teacher(new teacher_model(name, phone))){
                        Log.d("Database Operation :", "Adding Teacher");
                        teacher_model newest = new teacher_model(name, phone);
                        newest.setId(String.valueOf(database.getTeacherId(newest.name)));
                        list.add(newest); adapter.notifyDataSetChanged();
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
                input_name.setText(null); input_phone.setText(null);
            }
        });
    }
}
