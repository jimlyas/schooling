package schooling.com.epizy.someone.schooling.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.Objects;

import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.models.subject_model;
import schooling.com.epizy.someone.schooling.R;
import schooling.com.epizy.someone.schooling.models.teacher_model;

public class add_subject extends AppCompatActivity {
    Toolbar tb; MaterialDialog.Builder mdb;
    TextView name, room, teacher, note; DBHelper database;
    ArrayList<String> nama; String selected_teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_add_subject);

        tb = findViewById(R.id.toolbar_add_subject);
        name = findViewById(R.id.input_subject_name);
        room = findViewById(R.id.input_subject_room);
        teacher = findViewById(R.id.input_subject_teacher);
        note = findViewById(R.id.input_subject_note);
        mdb = new MaterialDialog.Builder(this);
        database = new DBHelper(this);
        nama = new ArrayList<>();
        database.teachers_name(nama);

        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);

        if (getIntent().getStringExtra("RequestCode")!=null){
            if(getIntent().getStringExtra("RequestCode").equals("12")){
                tb.setTitle("Edit subject's data");
                ((Button)findViewById(R.id.button_add_subject)).setText("Edit");
                name.setText(getIntent().getStringExtra("name"));
                room.setText(getIntent().getStringExtra("room"));
                note.setText(getIntent().getStringExtra("note"));
                teacher.setText(getIntent().getStringExtra("teacher"));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void adding(View view) {
        String buttonType = tb.getTitle().toString();
        if (buttonType.equals("Add Subject")){
            if(name.getText().toString().length()==0||room.getText().toString().length()==0||teacher.getText().toString().length()==0){
                Snackbar.make(findViewById(R.id.root_add_subject), "Fill all the data!", Snackbar.LENGTH_SHORT).show();
            }else if (database.add_subject(new subject_model(name.getText().toString(), room.getText().toString(), teacher.getText().toString(), note.getText().toString()))){
                Log.d("Database Operation :", "Adding Subject");
                Intent data = new Intent();
                data.putExtra("name", name.getText().toString());
                data.putExtra("room", room.getText().toString());
                data.putExtra("teacher", teacher.getText().toString());
                data.putExtra("note", note.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }else{
                Snackbar.make(findViewById(R.id.root_add_subject), "Adding subject failed!", Snackbar.LENGTH_SHORT).show();
            }

        }else{
            if(name.getText().toString().length()==0||room.getText().toString().length()==0||teacher.getText().toString().length()==0){
                Snackbar.make(findViewById(R.id.root_add_subject), "Fill all the data!", Snackbar.LENGTH_SHORT).show();
            }else {
                subject_model newest = new subject_model(name.getText().toString(), room.getText().toString(), teacher.getText().toString(), note.getText().toString());
                newest.setId(getIntent().getStringExtra("id"));
                if(database.update_subject(newest)){
                    Log.d("Database Operation :", "Updating subject's data");
                    Intent data = new Intent();
                    data.putExtra("name", newest.name);
                    data.putExtra("room", newest.room);
                    data.putExtra("teacher", newest.teacher);
                    data.putExtra("note", newest.note);
                    data.putExtra("id", newest.id);
                    data.putExtra("index", getIntent().getStringExtra("index"));
                    setResult(RESULT_OK, data);
                    finish();
                }else{
                    Snackbar.make(findViewById(R.id.root_add_subject), "Updating subject failed!", Snackbar.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void open_dialog(View view) {
        if (nama.size()>0){
            new MaterialDialog.Builder(this).title("Select a Teacher").items(nama).positiveText("Select").negativeText("Cancel")
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            selected_teacher = nama.get(which);
                            return true;
                        }
                    }).callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog); teacher.setText(selected_teacher);
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    super.onNegative(dialog); dialog.dismiss();
                }
            }).alwaysCallSingleChoiceCallback().show();
        }else {
            new MaterialDialog.Builder(this).title("Select a Teacher").content("There is no teacher's data")
                    .positiveText("Add new teacher").negativeText("cancel").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    @SuppressLint("InflateParams") final View v = getLayoutInflater().inflate(R.layout.d_add_teacher, null);
                    final EditText input_name = v.findViewById(R.id.input_name_teacher);
                    final EditText input_phone = v.findViewById(R.id.input_phone_teacher);
                    mdb.title("Add Teacher").customView(v, false).positiveText("Add").negativeText("Cancel").autoDismiss(true);

                    mdb.onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String name = input_name.getText().toString();
                            String phone = input_phone.getText().toString();


                            if(!name.matches("")||!phone.matches("")){
                                if(database.add_teacher(new teacher_model(name, phone))){
                                    Log.d("Database Operation :", "Adding Teacher");
                                    teacher_model newest = new teacher_model(name, phone);
                                    newest.setId(String.valueOf(database.getTeacherId(newest.name)));
                                    nama.clear();
                                    database.teachers_name(nama);
                                }else{
                                    Toast.makeText(add_subject.this, "Add Teacher failed!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Snackbar.make(findViewById(R.id.root_add_subject), "Fill all the fields!", Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            input_name.setText(null); input_phone.setText(null);
                        }
                    }).build().show();

                }
            }).onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            }).autoDismiss(true).build().show();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("RequestCode", requestCode);
        super.startActivityForResult(intent, requestCode);
    }
}
