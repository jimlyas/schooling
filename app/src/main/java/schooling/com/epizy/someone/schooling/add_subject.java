package schooling.com.epizy.someone.schooling;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;

import schooling.com.epizy.someone.schooling.Model.subject_model;

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
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void add_subject(View view) {
        if(name.getText().toString().length()==0||room.getText().toString().length()==0||teacher.getText().toString().length()==0){
            Snackbar.make(findViewById(R.id.root_add_subject), "Fill all the data!", Snackbar.LENGTH_SHORT).show();
        }else if (database.add_subject(new subject_model(name.getText().toString(), room.getText().toString(), teacher.getText().toString(), note.getText().toString()))){
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
    }

    public void open_dialog(View view) {
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
    }
}
