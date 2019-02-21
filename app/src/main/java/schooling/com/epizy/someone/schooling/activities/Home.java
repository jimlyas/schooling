package schooling.com.epizy.someone.schooling.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionMenu;
import org.jraf.android.alibglitch.GlitchEffect;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.R;
import schooling.com.epizy.someone.schooling.fragments.home;
import schooling.com.epizy.someone.schooling.fragments.open_source;
import schooling.com.epizy.someone.schooling.fragments.schedule;
import schooling.com.epizy.someone.schooling.fragments.subjects;
import schooling.com.epizy.someone.schooling.fragments.teachers;
import schooling.com.epizy.someone.schooling.models.teacher_model;
import spencerstudios.com.bungeelib.Bungee;

public class Home extends AppCompatActivity {
    Toolbar tb; FrameLayout fl; DrawerLayout dl; CoordinatorLayout cl; NavigationView nv;
    FragmentManager fm; ActionBarDrawerToggle ActionBarDrawer;
    FloatingActionMenu fab_all;

    @SuppressLint("StaticFieldLeak")
    private static home HomeFragment = new home();
    private DBHelper database;
    private SharedPreferences pref;
    private static schedule ScheduleFragment = new schedule();
    private subjects SubjectFragment = new subjects();
    private teachers TeacherFragment = new teachers();
    private open_source SourceFragment = new open_source();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_home);
        pref = getSharedPreferences("preferences", MODE_PRIVATE);
        database = new DBHelper(this);
        tb = findViewById(R.id.toolbar);
        fl = findViewById(R.id.content_home);
        dl = findViewById(R.id.root_home);
        cl = findViewById(R.id.root_coordinator_home);
        nv = findViewById(R.id.nav_view);
        fm = getSupportFragmentManager();
        fab_all = findViewById(R.id.fab_all);

        fm.beginTransaction().replace(R.id.content_home, HomeFragment).commit();
        fab_all.showMenu(true);


        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true); ab.setHomeAsUpIndicator(R.drawable.ic_menu);

        nv.addHeaderView(LayoutInflater.from(this).inflate(R.layout.nav_header, nv, false));
        nv.getHeaderView(0).setVisibility(View.GONE);
        if (pref.getBoolean("user", false)){
            nv.getHeaderView(0).setVisibility(View.VISIBLE);
            View header = nv.getHeaderView(0);
            ((CircleImageView)header.findViewById(R.id.nav_profile_photo)).setImageURI(Uri.parse(pref.getString("PROFILE",null)));
            ((TextView)header.findViewById(R.id.nav_name)).setText(pref.getString("name", "Not Defined"));
            ((TextView)header.findViewById(R.id.nav_major)).setText(pref.getString("major", "Majoring in"));
        }

        onClickNavigationView();
        settingDrawerLayout();
        FloatingClicked();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pref.getBoolean("user", false)){
            nv.getHeaderView(0).setVisibility(View.VISIBLE);
            View header = nv.getHeaderView(0);
            ((CircleImageView)header.findViewById(R.id.nav_profile_photo)).setImageURI(Uri.parse(pref.getString("PROFILE",null)));
            ((TextView)header.findViewById(R.id.nav_name)).setText(pref.getString("name", "Not Defined"));
            ((TextView)header.findViewById(R.id.nav_major)).setText(pref.getString("major", "Majoring in"));
        }else {
            nv.getHeaderView(0).setVisibility(View.GONE);
        }

    }

    private void FloatingClicked() {
        fab_all.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = fab_all.isOpened();
                switch (tb.getTitle().toString()){
                    case "Schedule":
                        startActivityForResult(new Intent(Home.this, add_schedule.class), 2);
                        Bungee.slideUp(Home.this);
                        break;
                    case "Subjects":
                        startActivityForResult(new Intent(Home.this, add_subject.class), 11);
                        Bungee.slideUp(Home.this);
                        break;
                    case "Teachers": TeacherFragment.mdb.show();
                        break;
                    case "Home":
                        if (check){
                            fab_all.close(true);
                        }else {
                            fab_all.open(true);
                        }
                        break;
                }
            }
        });
    }

    private void settingDrawerLayout() {
        ActionBarDrawer = setupActionBarDrawer();
        dl.addDrawerListener(ActionBarDrawer);
    }

    private ActionBarDrawerToggle setupActionBarDrawer() {
        return new ActionBarDrawerToggle(this, dl, tb, R.string.drawer_open, R.string.drawer_close);
    }

    private void onClickNavigationView() {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                dl.closeDrawers();
                switch (item.getItemId()){

                    case R.id.menu_home:
                        nv.setCheckedItem(R.id.menu_home); fab_all.showMenu(true);
                        Log.d("Transition Fragment :", "Home ");
                        fm.beginTransaction().replace(R.id.content_home, HomeFragment).commit();
                        break;

                    case  R.id.menu_profile:
                        if (getSharedPreferences("preferences", MODE_PRIVATE).getString("PROFILE",null)==null){
                            startActivity(new Intent(Home.this, update_profile.class));
                            Bungee.fade(Home.this);
                        }else {
                            startActivity(new Intent(Home.this, Profile.class));
                            Bungee.slideLeft(Home.this);
                        }
                        break;

                    case R.id.menu_schedule:
                        nv.setCheckedItem(R.id.menu_schedule); fab_all.showMenu(true);
                        Log.d("Transition Fragment :", "Schedule ");
                        fm.beginTransaction().replace(R.id.content_home, ScheduleFragment).commit();
                        break;

                    case R.id.menu_subjects:
                        nv.setCheckedItem(R.id.menu_subjects); fab_all.showMenu(true);
                        Log.d("Transition Fragment :", "Subjects ");
                        fm.beginTransaction().replace(R.id.content_home, SubjectFragment).commit();
                        break;

                    case R.id.menu_teachers:
                        nv.setCheckedItem(R.id.menu_teachers); fab_all.showMenu(true);
                        Log.d("Transition Fragment :", "Teachers ");
                        fm.beginTransaction().replace(R.id.content_home, TeacherFragment).commit();
                        break;

                    case  R.id.menu_code:
                        nv.setCheckedItem(R.id.menu_teachers); fab_all.hideMenu(true);
                        Log.d("Transition Fragment :", "Open Source Libraries ");
                        fm.beginTransaction().replace(R.id.content_home, SourceFragment).commit();
                        break;

                    case R.id.menu_logout:
                        nv.setCheckedItem(R.id.menu_logout); fab_all.hideMenu(true);
                        getSharedPreferences("preferences", MODE_PRIVATE).edit().clear().apply();
                        nv.getHeaderView(0).setVisibility(View.GONE);
                        break;
                }

                if(item.getItemId()!=R.id.menu_profile)
                    tb.setTitle(item.getTitle());

                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ActionBarDrawer.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ActionBarDrawer.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(ActionBarDrawer.onOptionsItemSelected(item)){
            dl.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        GlitchEffect.showGlitch(Home.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Home.this.finish();
            }
        }, 350);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==11&&resultCode==RESULT_OK){
            SubjectFragment.onActivityResult(requestCode, resultCode, data);
        }else if(requestCode==2&&resultCode==RESULT_OK){
            ScheduleFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("RequestCode", String.valueOf(requestCode));
        super.startActivityForResult(intent, requestCode);
    }

    public void ActionFromFAB(final View view) {
        fab_all.close(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (view.getId()){
                    case R.id.fab1:
                        @SuppressLint("InflateParams") final View v = getLayoutInflater().inflate(R.layout.d_add_teacher, null);
                        final EditText input_name = v.findViewById(R.id.input_name_teacher);
                        final EditText input_phone = v.findViewById(R.id.input_phone_teacher);
                        new MaterialDialog.Builder(Home.this).title("Add Teacher").customView(v, false)
                                .positiveText("Add").negativeText("Cancel").autoDismiss(true)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        String name = input_name.getText().toString();
                                        String phone = input_phone.getText().toString();


                                        if(!name.matches("")||!phone.matches("")){
                                            if(database.add_teacher(new teacher_model(name, phone))){
                                                Log.d("Database Operation :", "Adding Teacher");
                                                teacher_model newest = new teacher_model(name, phone);
                                                newest.setId(String.valueOf(database.getTeacherId(newest.name)));
                                            }else{
                                                Toast.makeText(Home.this, "Add Teacher failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Snackbar.make(findViewById(R.id.root_home), "Fill all the fields!", Snackbar.LENGTH_SHORT)
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
                        break;
                    case R.id.fab2:
                        startActivityForResult(new Intent(Home.this, add_subject.class), 11);
                        Bungee.slideUp(Home.this);
                        break;
                    case R.id.fab3:
                        startActivityForResult(new Intent(Home.this, add_schedule.class), 2);
                        Bungee.slideUp(Home.this);
                        break;
                }
            }
        },300);
    }
}
